package by.bsu.wialontransport.crud.service;

import by.bsu.wialontransport.crud.dto.Dto;
import by.bsu.wialontransport.crud.entity.Entity;
import by.bsu.wialontransport.crud.mapper.Mapper;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;

import static by.bsu.wialontransport.util.CollectionUtil.mapToList;
import static org.springframework.data.domain.Pageable.unpaged;

public abstract class AbstractReadService<
        ID,
        ENTITY extends Entity<ID>,
        DTO extends Dto<ID>,
        MAPPER extends Mapper<ENTITY, DTO>,
        REPOSITORY extends JpaRepository<ENTITY, ID>
        > {
    protected final MAPPER mapper;
    protected final REPOSITORY repository;

    public AbstractReadService(final MAPPER mapper, final REPOSITORY repository) {
        this.mapper = mapper;
        this.repository = repository;
    }

    @Transactional(readOnly = true)
    public Optional<DTO> findById(final ID id) {
        final Optional<ENTITY> optionalEntity = this.repository.findById(id);
        return optionalEntity.map(this.mapper::mapToDto);
    }

    @Transactional(readOnly = true)
    public List<DTO> findById(final Collection<ID> ids) {
        final List<ENTITY> foundEntities = this.repository.findAllById(ids);
        return this.mapper.mapToDtos(foundEntities);
    }

    @Transactional(readOnly = true)
    public boolean isExist(final ID id) {
        return this.repository.existsById(id);
    }

    protected final Optional<DTO> findUnique(final Function<REPOSITORY, Optional<ENTITY>> operation) {
        final Optional<ENTITY> optionalEntity = operation.apply(this.repository);
        return optionalEntity.map(this.mapper::mapToDto);
    }

    protected final List<DTO> find(final Function<REPOSITORY, Collection<ENTITY>> operation) {
        return this.findPage(unpaged(), (repository, pageable) -> operation.apply(repository));
    }

    protected final List<DTO> findPage(final int pageNumber,
                                       final int pageSize,
                                       final BiFunction<REPOSITORY, Pageable, Collection<ENTITY>> operation) {
        final Pageable pageable = PageRequest.of(pageNumber, pageSize);
        return this.findPage(pageable, operation);
    }

    private List<DTO> findPage(final Pageable pageable,
                               final BiFunction<REPOSITORY, Pageable, Collection<ENTITY>> operation) {
        final Collection<ENTITY> entities = operation.apply(this.repository, pageable);
        return mapToList(entities, this.mapper::mapToDto);
    }
}
