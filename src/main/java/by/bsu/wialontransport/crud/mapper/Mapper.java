package by.bsu.wialontransport.crud.mapper;

import by.bsu.wialontransport.crud.dto.Dto;
import by.bsu.wialontransport.crud.entity.Entity;
import by.bsu.wialontransport.util.HibernateUtil;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.modelmapper.spi.MappingContext;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import static by.bsu.wialontransport.util.CollectionUtil.mapAndCollect;
import static java.util.function.Function.identity;
import static java.util.stream.Collectors.toMap;

//TODO: refactor
public abstract class Mapper<ENTITY extends Entity<?>, DTO extends Dto<?>> {
    private final ModelMapper modelMapper;
    private final Class<ENTITY> entityType;
    private final Class<DTO> dtoType;

    public Mapper(final ModelMapper modelMapper, final Class<ENTITY> entityType, final Class<DTO> dtoType) {
        this.modelMapper = modelMapper;
        this.entityType = entityType;
        this.dtoType = dtoType;
        this.configureBiDirectionalMapping();
    }

    public final DTO mapToDto(final ENTITY source) {
        return this.mapNullable(source, this.dtoType);
    }

    public final List<DTO> mapToDtos(final Collection<ENTITY> sources) {
        return this.mapNullableToList(sources, this.dtoType);
    }

    public final ENTITY mapToEntity(final DTO source) {
        return this.mapNullable(source, this.entityType);
    }

    public final List<ENTITY> mapToEntities(final Collection<DTO> sources) {
        return this.mapNullableToList(sources, this.entityType);
    }

    protected abstract DTO createDto(final ENTITY entity);

    protected abstract void mapSpecificFields(final DTO source, final ENTITY destination);

    protected final <D> D map(final Object source, final Class<D> destinationType) {
        return this.modelMapper.map(source, destinationType);
    }

    protected final <D> D mapNullable(final Object source, final Class<D> destinationType) {
        return this.mapIfMatchOrElseNull(source, Objects::nonNull, destinationType);
    }

    protected final <S extends Entity<?>, D extends Dto<?>> D mapLazyProperty(final ENTITY entity,
                                                                              final Function<ENTITY, S> propertyGetter,
                                                                              final Class<D> destinationType) {
        final S source = propertyGetter.apply(entity);
        return this.mapIfMatchOrElseNull(source, HibernateUtil::isLoaded, destinationType);
    }

    protected final <S extends Entity<?>, E extends Dto<?>> List<E> mapLazyCollectionPropertyToList(
            final ENTITY entity,
            final Function<ENTITY, Collection<S>> propertyGetter,
            final Class<E> destinationValueType
    ) {
        return this.mapLazyCollectionProperty(
                entity,
                propertyGetter,
                destinationValueType,
                Collectors::toUnmodifiableList
        );
    }

    protected final <S extends Entity<?>, E extends Dto<?>, K> Map<K, E> mapLazyCollectionPropertyToMap(
            final ENTITY entity,
            final Function<ENTITY, Collection<S>> propertyGetter,
            final Class<E> destinationValueType,
            final Function<E, K> keyExtractor
    ) {
        return this.mapLazyCollectionProperty(
                entity,
                propertyGetter,
                destinationValueType,
                () -> toMap(keyExtractor, identity())
        );
    }

    protected final <P> void mapPropertyAndSet(final DTO source,
                                               final Function<DTO, P> mapper,
                                               final ENTITY destination,
                                               final BiConsumer<ENTITY, P> setter) {
        final P property = mapper.apply(source);
        setter.accept(destination, property);
    }

    private void configureBiDirectionalMapping() {
        this.configureMappingEntityToDto();
        this.configureMappingDtoToEntity();
    }

    private void configureMappingEntityToDto() {
        this.configureOneDirectionalMapping(
                this.entityType,
                this.dtoType,
                EntityToDtoConvertor::new,
                TypeMap::setConverter
        );
    }

    private void configureMappingDtoToEntity() {
        this.configureOneDirectionalMapping(
                this.dtoType,
                this.entityType,
                DtoToEntityPostConvertor::new,
                TypeMap::setPostConverter
        );
    }

    private <S, D> void configureOneDirectionalMapping(final Class<S> sourceType,
                                                       final Class<D> destinationType,
                                                       final Supplier<Converter<S, D>> converterSupplier,
                                                       final BiConsumer<TypeMap<S, D>, Converter<S, D>> converterSetter) {
        final TypeMap<S, D> typeMap = this.modelMapper.createTypeMap(sourceType, destinationType);
        final Converter<S, D> converter = converterSupplier.get();
        converterSetter.accept(typeMap, converter);
    }

    private <S, D> D mapIfMatchOrElseNull(final S source, final Predicate<S> matcher, final Class<D> destinationType) {
        return matcher.test(source) ? this.map(source, destinationType) : null;
    }

    private <E> List<E> mapNullableToList(final Collection<?> sources, final Class<E> destinationElementType) {
        return this.mapIfMatchOrElseNull(
                sources,
                Objects::nonNull,
                destinationElementType,
                Collectors::toUnmodifiableList
        );
    }

    private <S extends Entity<?>, E extends Dto<?>, D> D mapLazyCollectionProperty(
            final ENTITY entity,
            final Function<ENTITY, Collection<S>> propertyGetter,
            final Class<E> destinationElementType,
            final Supplier<Collector<E, ?, D>> collectorSupplier
    ) {
        final Collection<S> sources = propertyGetter.apply(entity);
        return this.mapIfMatchOrElseNull(sources, HibernateUtil::isLoaded, destinationElementType, collectorSupplier);
    }

    private <E, D> D mapIfMatchOrElseNull(final Collection<?> sources,
                                          final Predicate<Collection<?>> matcher,
                                          final Class<E> destinationElementType,
                                          final Supplier<Collector<E, ?, D>> collectorSupplier) {
        return matcher.test(sources)
                ? mapAndCollect(sources, source -> this.map(source, destinationElementType), collectorSupplier)
                : null;
    }

    private static abstract class AbstractConvertor<S, D> implements Converter<S, D> {

        @Override
        public final D convert(final MappingContext<S, D> context) {
            final S source = context.getSource();
            return this.createDestination(source, context);
        }

        protected abstract D createDestination(final S source, final MappingContext<S, D> context);
    }

    private final class EntityToDtoConvertor extends AbstractConvertor<ENTITY, DTO> {

        @Override
        protected DTO createDestination(final ENTITY source, final MappingContext<ENTITY, DTO> context) {
            return Mapper.this.createDto(source);
        }
    }

    private final class DtoToEntityPostConvertor extends AbstractConvertor<DTO, ENTITY> {

        @Override
        protected ENTITY createDestination(final DTO source, final MappingContext<DTO, ENTITY> context) {
            final ENTITY destination = context.getDestination();
            Mapper.this.mapSpecificFields(source, destination);
            return destination;
        }
    }
}
