package by.bsu.wialontransport.crud.service;

import by.bsu.wialontransport.crud.dto.AbstractDto;
import by.bsu.wialontransport.crud.entity.AbstractEntity;
import by.bsu.wialontransport.crud.mapper.AbstractMapper;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public abstract class AbstractReadService<
        IdType,
        EntityType extends AbstractEntity<IdType>,
        DtoType extends AbstractDto<IdType>,
        MapperType extends AbstractMapper<EntityType, DtoType>,
        RepositoryType extends JpaRepository<EntityType, IdType>> {
    protected final MapperType mapper;
    protected final RepositoryType repository;

    public AbstractReadService(final MapperType mapper, final RepositoryType repository) {
        this.mapper = mapper;
        this.repository = repository;
    }

    public Optional<DtoType> findById(final IdType id) {
        final Optional<EntityType> optionalEntity = this.repository.findById(id);
        return optionalEntity.map(this.mapper::mapToDto);
    }

    public List<DtoType> findById(final Collection<IdType> ids) {
        final List<EntityType> foundEntities = this.repository.findAllById(ids);
        return this.mapper.mapToDto(foundEntities);
    }

    public boolean isExist(final IdType id) {
        return this.repository.existsById(id);
    }
}
