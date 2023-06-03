package by.bsu.wialontransport.crud.mapper;

import by.bsu.wialontransport.crud.dto.AbstractDto;
import by.bsu.wialontransport.crud.entity.AbstractEntity;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;

import java.util.Collection;
import java.util.List;
import java.util.function.Supplier;

import static java.util.Objects.isNull;
import static java.util.stream.Collectors.toList;
import static org.hibernate.Hibernate.isInitialized;

public abstract class AbstractMapper<EntityType extends AbstractEntity<?>, DtoType extends AbstractDto<?>> {
    private final ModelMapper modelMapper;
    private final Class<EntityType> entityType;
    private final Class<DtoType> dtoType;

    public AbstractMapper(final ModelMapper modelMapper, final Class<EntityType> entityType,
                          final Class<DtoType> dtoType) {
        this.modelMapper = modelMapper;
        this.entityType = entityType;
        this.dtoType = dtoType;
        this.configureMapper();
    }

    public final DtoType mapToDto(final EntityType mapped) {
        return !isNull(mapped) ? this.modelMapper.map(mapped, this.dtoType) : null;
    }

    public final List<DtoType> mapToDto(final Collection<EntityType> mapped) {
        return !isNull(mapped)
                ? mapped.stream()
                .map(this::mapToDto)
                .collect(toList())
                : null;
    }

    public final EntityType mapToEntity(final DtoType mapped) {
        return !isNull(mapped) ? this.modelMapper.map(mapped, this.entityType) : null;
    }

    public final List<EntityType> mapToEntity(final Collection<DtoType> mapped) {
        return !isNull(mapped)
                ? mapped.stream()
                .map(this::mapToEntity)
                .collect(toList())
                : null;
    }

    protected abstract DtoType createDto(final EntityType entity);

    protected void mapSpecificFields(final DtoType source, final EntityType destination) {

    }

    protected <
            PropertyEntityType extends AbstractEntity<?>,
            PropertyDtoType extends AbstractDto<?>
            >
    PropertyDtoType mapPropertyIfLoadedOrElseNull(final PropertyEntityType mapped,
                                                  final Class<PropertyDtoType> dtoType) {
        return isPropertyLoaded(mapped) ? this.modelMapper.map(mapped, dtoType) : null;
    }

    protected <
            PropertyEntityType extends AbstractEntity<?>,
            PropertyDtoType extends AbstractDto<?>
            >
    PropertyEntityType mapProperty(final PropertyDtoType mapped, final Class<PropertyEntityType> entityType) {
        return this.modelMapper.map(mapped, entityType);
    }

    protected <
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


    @SuppressWarnings("unchecked")
    private void configureMapper() {
        this.modelMapper.createTypeMap(this.entityType, this.dtoType)
                .setProvider(request -> this.createDto((EntityType) request.getSource()));
        this.modelMapper.createTypeMap(this.dtoType, this.entityType)
                .setPostConverter(this.createConverterDtoToEntity());
    }

    private Converter<DtoType, EntityType> createConverterDtoToEntity() {
        return context -> {
            final DtoType source = context.getSource();
            final EntityType destination = context.getDestination();
            this.mapSpecificFields(source, destination);
            return destination;
        };
    }

    private static boolean isPropertyLoaded(final Object property) {
        return property != null && isInitialized(property);
    }
}
