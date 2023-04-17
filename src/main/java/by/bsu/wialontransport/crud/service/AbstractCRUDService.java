package by.bsu.wialontransport.crud.service;

import by.bsu.wialontransport.crud.dto.AbstractDto;
import by.bsu.wialontransport.crud.entity.AbstractEntity;
import by.bsu.wialontransport.crud.mapper.AbstractMapper;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;

public abstract class AbstractCRUDService<
        IdType,
        EntityType extends AbstractEntity<IdType>,
        DtoType extends AbstractDto<IdType>,
        MapperType extends AbstractMapper<EntityType, DtoType>,
        RepositoryType extends JpaRepository<EntityType, IdType>>

        extends AbstractRUDService<IdType, EntityType, DtoType, MapperType, RepositoryType> {


    public AbstractCRUDService(final MapperType mapper, final RepositoryType repository) {
        super(mapper, repository);
    }

    public DtoType save(final DtoType saved) {
        final EntityType entityToBeSaved = super.mapper.mapToEntity(saved);
        final EntityType savedEntity = super.repository.save(entityToBeSaved);
        return super.mapper.mapToDto(savedEntity);
    }

    public List<DtoType> saveAll(Collection<DtoType> saved) {
        final List<EntityType> entitiesToBeSaved = super.mapper.mapToEntity(saved);
        final List<EntityType> savedEntities = super.repository.saveAll(entitiesToBeSaved);
        return super.mapper.mapToDto(savedEntities);
    }
}