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

    public final EntityType mapToEntity(DtoType mapped) {
        return !isNull(mapped) ? this.modelMapper.map(mapped, this.entityType) : null;
    }

    public final List<EntityType> mapToEntity(Collection<DtoType> mapped) {
        return !isNull(mapped)
                ? mapped.stream()
                .map(this::mapToEntity)
                .collect(toList())
                : null;
    }

    protected final ModelMapper getModelMapper() {
        return this.modelMapper;
    }

    protected abstract DtoType createDto(EntityType entity);

    protected void mapSpecificFields(DtoType source, EntityType destination) {

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
}
