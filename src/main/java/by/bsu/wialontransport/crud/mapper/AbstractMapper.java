package by.bsu.wialontransport.crud.mapper;

import by.bsu.wialontransport.crud.dto.AbstractDto;
import by.bsu.wialontransport.crud.entity.AbstractEntity;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;

import java.util.Collection;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Supplier;

import static java.util.Objects.isNull;
import static java.util.stream.Collectors.toList;
import static org.hibernate.Hibernate.isInitialized;

public abstract class AbstractMapper<ENTITY_TYPE extends AbstractEntity<?>, DTO_TYPE extends AbstractDto<?>> {
    private final ModelMapper modelMapper;
    private final Class<ENTITY_TYPE> entityType;
    private final Class<DTO_TYPE> dtoType;

    public AbstractMapper(final ModelMapper modelMapper, final Class<ENTITY_TYPE> entityType,
                          final Class<DTO_TYPE> dtoType) {
        this.modelMapper = modelMapper;
        this.entityType = entityType;
        this.dtoType = dtoType;
        this.configureMapper();
    }

    public final DTO_TYPE mapToDto(final ENTITY_TYPE mapped) {
        return !isNull(mapped) ? this.modelMapper.map(mapped, this.dtoType) : null;
    }

    public final List<DTO_TYPE> mapToDto(final Collection<ENTITY_TYPE> mapped) {
        return !isNull(mapped)
                ? mapped.stream()
                .map(this::mapToDto)
                .collect(toList())
                : null;
    }

    public final ENTITY_TYPE mapToEntity(final DTO_TYPE mapped) {
        return !isNull(mapped) ? this.modelMapper.map(mapped, this.entityType) : null;
    }

    public final List<ENTITY_TYPE> mapToEntity(final Collection<DTO_TYPE> mapped) {
        return !isNull(mapped)
                ? mapped.stream()
                .map(this::mapToEntity)
                .collect(toList())
                : null;
    }

    protected abstract DTO_TYPE createDto(final ENTITY_TYPE entity);

    protected void mapSpecificFields(final DTO_TYPE source, final ENTITY_TYPE destination) {

    }

    protected final <
            PropertyEntityType extends AbstractEntity<?>,
            PropertyDtoType extends AbstractDto<?>
            >
    PropertyDtoType mapPropertyIfLoadedOrElseNull(final ENTITY_TYPE entity,
                                                  final Function<ENTITY_TYPE, PropertyEntityType> propertyGetter,
                                                  final Class<PropertyDtoType> propertyDtoType) {
        final PropertyEntityType propertyEntity = propertyGetter.apply(entity);
        return isLoaded(propertyEntity) ? this.modelMapper.map(propertyEntity, propertyDtoType) : null;
    }

    protected final <
            PropertyEntityType extends AbstractEntity<?>,
            PropertyDtoType extends AbstractDto<?>
            >
    PropertyEntityType mapProperty(final PropertyDtoType mapped, final Class<PropertyEntityType> entityType) {
        return this.modelMapper.map(mapped, entityType);
    }

    protected final <
            PropertyEntityType extends AbstractEntity<?>,
            PropertyDtoType extends AbstractDto<?>,
            MappedCollectionType extends Collection<PropertyEntityType>,
            ResultCollectionType extends Collection<PropertyDtoType>
            >
    ResultCollectionType mapCollectionPropertyIfLoadedOrElseNull(
            final MappedCollectionType mapped,
            final Class<PropertyDtoType> dtoType,
            final Supplier<ResultCollectionType> resultCollectionFactory) {
        if (!isInitialized(mapped)) {
            return null;
        }
        final ResultCollectionType resultCollection = resultCollectionFactory.get();
        for (final PropertyEntityType mappedEntity : mapped) {
            final PropertyDtoType resultDto = this.modelMapper.map(mappedEntity, dtoType);
            resultCollection.add(resultDto);
        }
        return resultCollection;
    }

    protected final <PROPERTY_TYPE> void mapPropertyAndSet(
            final DTO_TYPE source,
            final Function<DTO_TYPE, PROPERTY_TYPE> propertyFounder,
            final ENTITY_TYPE entity,
            final BiConsumer<ENTITY_TYPE, PROPERTY_TYPE> propertySetter
    ) {
        final PROPERTY_TYPE property = propertyFounder.apply(source);
        propertySetter.accept(entity, property);
    }

    private void configureMapper() {
        this.modelMapper.createTypeMap(this.entityType, this.dtoType)
                //TODO: cast by class-object
                .setProvider(request -> this.createDto((ENTITY_TYPE) request.getSource()));
        this.modelMapper.createTypeMap(this.dtoType, this.entityType)
                .setPostConverter(this.createConverterDtoToEntity());
    }

    private Converter<DTO_TYPE, ENTITY_TYPE> createConverterDtoToEntity() {
        return context -> {
            final DTO_TYPE source = context.getSource();
            final ENTITY_TYPE destination = context.getDestination();
            this.mapSpecificFields(source, destination);
            return destination;
        };
    }

    private static boolean isLoaded(final Object object) {
        return object != null && isInitialized(object);
    }
}
