package by.bsu.wialontransport.crud.mapper;

import by.bsu.wialontransport.crud.dto.AbstractDto;
import by.bsu.wialontransport.crud.entity.AbstractEntity;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;

import java.util.Collection;
import java.util.List;

import static java.util.Objects.isNull;
import static java.util.stream.Collectors.toList;

public abstract class AbstractMapper<EntityType extends AbstractEntity<?>, DtoType extends AbstractDto<?>> {
    protected final ModelMapper modelMapper;
    protected final Class<EntityType> entityType;
    protected final Class<DtoType> dtoType;

    public AbstractMapper(final ModelMapper modelMapper, final Class<EntityType> entityType,
                          final Class<DtoType> dtoType) {
        this.modelMapper = modelMapper;
        this.entityType = entityType;
        this.dtoType = dtoType;
        this.configureMapper();
    }

    public DtoType mapToDto(final EntityType mapped) {
        return !isNull(mapped) ? this.modelMapper.map(mapped, this.dtoType) : null;
    }

    public List<DtoType> mapToDto(final Collection<EntityType> mapped) {
        return !isNull(mapped)
                ? mapped.stream()
                .map(this::mapToDto)
                .collect(toList())
                : null;
    }

    public EntityType mapToEntity(DtoType mapped) {
        return !isNull(mapped) ? this.modelMapper.map(mapped, this.entityType) : null;
    }

    public List<EntityType> mapToEntity(Collection<DtoType> mapped) {
        return !isNull(mapped)
                ? mapped.stream()
                .map(this::mapToEntity)
                .collect(toList())
                : null;
    }

    protected abstract DtoType createDto(EntityType entity);

    protected void mapSpecificFields(EntityType source, DtoType destination) {

    }

    protected void mapSpecificFields(DtoType source, EntityType destination) {

    }

    @SuppressWarnings("unchecked")
    private void configureMapper() {
        this.modelMapper.createTypeMap(this.entityType, this.dtoType)
                .setPostConverter(this.createConverterEntityToDto())
                .setProvider(request -> this.createDto((EntityType) request.getSource()));
        this.modelMapper.createTypeMap(this.dtoType, this.entityType)
                .setPostConverter(this.createConverterDtoToEntity());
    }

    private Converter<EntityType, DtoType> createConverterEntityToDto() {
        return context -> {
            final EntityType source = context.getSource();
            final DtoType destination = context.getDestination();
            mapSpecificFields(source, destination);
            return context.getDestination();
        };
    }

    private Converter<DtoType, EntityType> createConverterDtoToEntity() {
        return context -> {
            final DtoType source = context.getSource();
            final EntityType destination = context.getDestination();
            this.mapSpecificFields(source, destination);
            return destination;
        };
    }
}
