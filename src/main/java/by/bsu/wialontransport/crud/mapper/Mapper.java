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
import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import static by.bsu.wialontransport.util.CollectionUtil.mapToCollection;

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

    public final List<DTO> mapToDto(final Collection<ENTITY> sources) {
        return this.mapNullable(sources, this.dtoType);
    }

    public final ENTITY mapToEntity(final DTO source) {
        return this.mapNullable(source, this.entityType);
    }

    public final List<ENTITY> mapToEntity(final Collection<DTO> sources) {
        return this.mapNullable(sources, this.entityType);
    }

    protected abstract DTO createDto(final ENTITY entity);

    protected abstract void mapSpecificFields(final DTO source, final ENTITY destination);

    protected final <D> D mapNullable(final Object source, final Class<D> destinationType) {
        return this.mapIfMatchOrElseNull(source, Objects::nonNull, destinationType);
    }

    protected final <D> List<D> mapNullable(final Collection<?> sources, final Class<D> destinationElementType) {
        return this.mapIfMatchOrElseNull(
                sources,
                Objects::nonNull,
                destinationElementType,
                Collectors::toUnmodifiableList
        );
    }

    protected final <S extends Entity<?>, D extends Dto<?>> D mapLazyProperty(final ENTITY entity,
                                                                              final Function<ENTITY, S> propertyGetter,
                                                                              final Class<D> destinationType) {
        final S source = propertyGetter.apply(entity);
        return this.mapIfMatchOrElseNull(source, HibernateUtil::isLoaded, destinationType);
    }

    protected final <S extends Entity<?>, E extends Dto<?>, C extends Collection<E>> C mapLazyCollectionProperty(
            final Collection<S> sources,
            final Class<E> destinationElementType,
            final Supplier<Collector<E, ?, C>> collectorSupplier
    ) {
        return this.mapIfMatchOrElseNull(sources, HibernateUtil::isLoaded, destinationElementType, collectorSupplier);
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
        return matcher.test(source) ? this.modelMapper.map(source, destinationType) : null;
    }

    private <E, D extends Collection<E>> D mapIfMatchOrElseNull(final Collection<?> sources,
                                                                final Predicate<Collection<?>> matcher,
                                                                final Class<E> destinationElementType,
                                                                final Supplier<Collector<E, ?, D>> collectorSupplier) {
        return matcher.test(sources)
                ? mapToCollection(sources, source -> this.mapNullable(source, destinationElementType), collectorSupplier)
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
