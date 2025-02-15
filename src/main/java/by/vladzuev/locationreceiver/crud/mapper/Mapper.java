package by.vladzuev.locationreceiver.crud.mapper;

import by.vladzuev.locationreceiver.crud.dto.Dto;
import by.vladzuev.locationreceiver.crud.entity.Entity;
import by.vladzuev.locationreceiver.util.HibernateUtil;
import by.vladzuev.locationreceiver.util.CollectionUtil;
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

import static java.util.function.Function.identity;
import static java.util.stream.Collectors.toMap;
import static java.util.stream.Collectors.toUnmodifiableList;

public abstract class Mapper<ENTITY extends Entity<?>, DTO extends Dto<?>> {
    private final ModelMapper modelMapper;
    private final Class<ENTITY> entityType;
    private final Class<DTO> dtoType;

    public Mapper(final ModelMapper modelMapper, final Class<ENTITY> entityType, final Class<DTO> dtoType) {
        this.modelMapper = modelMapper;
        this.entityType = entityType;
        this.dtoType = dtoType;
        configureBiDirectionalMapping();
    }

    public final DTO mapToDto(final ENTITY source) {
        return mapNullable(source, dtoType);
    }

    public final ENTITY mapToEntity(final DTO source) {
        return mapNullable(source, entityType);
    }

    protected abstract DTO createDto(final ENTITY entity);

    protected abstract void mapSpecificFields(final DTO source, final ENTITY destination);

    protected final <D> D map(final Object source, final Class<D> destinationType) {
        return modelMapper.map(source, destinationType);
    }

    protected final <D> D mapNullable(final Object source, final Class<D> destinationType) {
        return mapIfMatchOrElseNull(source, Objects::nonNull, destinationType);
    }

    protected final <S extends Entity<?>, D extends Dto<?>> D mapLazy(final S source, final Class<D> destinationType) {
        return mapIfMatchOrElseNull(source, HibernateUtil::isFetched, destinationType);
    }

    protected final <S extends Entity<?>, E extends Dto<?>> List<E> mapLazyToList(final Collection<S> source,
                                                                                  final Class<E> destinationValueType) {
        return mapLazyCollection(source, destinationValueType, toUnmodifiableList());
    }

    protected final <S extends Entity<?>, E extends Dto<?>, K> Map<K, E> mapLazyToMap(final Collection<S> source,
                                                                                      final Class<E> destinationValueType,
                                                                                      final Function<E, K> keyExtractor) {
        return mapLazyCollection(source, destinationValueType, toMap(keyExtractor, identity()));
    }

    private void configureBiDirectionalMapping() {
        configureMappingEntityToDto();
        configureMappingDtoToEntity();
    }

    private void configureMappingEntityToDto() {
        configureOneDirectionalMapping(entityType, dtoType, EntityToDtoConvertor::new, TypeMap::setConverter);
    }

    private void configureMappingDtoToEntity() {
        configureOneDirectionalMapping(dtoType, entityType, DtoToEntityPostConvertor::new, TypeMap::setPostConverter);
    }

    private <S, D> void configureOneDirectionalMapping(final Class<S> sourceType,
                                                       final Class<D> destinationType,
                                                       final Supplier<Converter<S, D>> converterSupplier,
                                                       final BiConsumer<TypeMap<S, D>, Converter<S, D>> converterSetter) {
        final TypeMap<S, D> typeMap = modelMapper.createTypeMap(sourceType, destinationType);
        final Converter<S, D> converter = converterSupplier.get();
        converterSetter.accept(typeMap, converter);
    }

    private <S, D> D mapIfMatchOrElseNull(final S source, final Predicate<S> matcher, final Class<D> destinationType) {
        return matcher.test(source) ? map(source, destinationType) : null;
    }

    private <S extends Entity<?>, E extends Dto<?>, D> D mapLazyCollection(final Collection<S> source,
                                                                           final Class<E> destinationElementType,
                                                                           final Collector<E, ?, D> collector) {
        return mapIfMatchOrElseNull(source, HibernateUtil::isFetched, destinationElementType, collector);
    }

    private <E, D> D mapIfMatchOrElseNull(final Collection<?> sources,
                                          final Predicate<Collection<?>> matcher,
                                          final Class<E> destinationElementType,
                                          final Collector<E, ?, D> collector) {
        return matcher.test(sources)
                ? CollectionUtil.mapAndCollect(sources, source -> map(source, destinationElementType), collector)
                : null;
    }

    private static abstract class AbstractConvertor<S, D> implements Converter<S, D> {

        @Override
        public final D convert(final MappingContext<S, D> context) {
            return createDestination(context.getSource(), context);
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
