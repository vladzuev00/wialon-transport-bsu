package by.bsu.wialontransport.crud.service;

import by.bsu.wialontransport.crud.dto.Dto;
import by.bsu.wialontransport.crud.entity.Entity;
import by.bsu.wialontransport.crud.mapper.Mapper;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;

public abstract class AbstractCRUDService<
        IdType,
        EntityType extends Entity<IdType>,
        DtoType extends Dto<IdType>,
        MapperType extends Mapper<EntityType, DtoType>,
        RepositoryType extends JpaRepository<EntityType, IdType>
        >
        extends AbstractRUDService<IdType, EntityType, DtoType, MapperType, RepositoryType> {

    public AbstractCRUDService(final MapperType mapper, final RepositoryType repository) {
        super(mapper, repository);
    }

    public DtoType save(final DtoType saved) {
        final EntityType entityToBeSaved = this.mapToConfiguredEntity(saved);
        final EntityType savedEntity = super.repository.save(entityToBeSaved);
        return super.mapper.mapToDto(savedEntity);
    }

    public List<DtoType> saveAll(final Collection<DtoType> saved) {
        final List<EntityType> entitiesToBeSaved = this.mapToConfiguredEntities(saved);
        final List<EntityType> savedEntities = super.repository.saveAll(entitiesToBeSaved);
        return super.mapper.mapToDtos(savedEntities);
    }

    protected EntityType configureBeforeSave(final EntityType source) {
        return source;
    }

    private EntityType mapToConfiguredEntity(final DtoType source) {
        final EntityType sourceEntity = super.mapper.mapToEntity(source);
        return this.configureBeforeSave(sourceEntity);
    }

    private List<EntityType> mapToConfiguredEntities(final Collection<DtoType> source) {
        return source.stream()
                .map(this::mapToConfiguredEntity)
                .toList();
    }
}