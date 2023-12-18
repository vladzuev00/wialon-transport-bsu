package by.bsu.wialontransport.crud.service;

import by.bsu.wialontransport.crud.dto.Dto;
import by.bsu.wialontransport.crud.entity.Entity;
import by.bsu.wialontransport.crud.mapper.Mapper;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.ToIntFunction;
import java.util.stream.Collector;
import java.util.stream.Stream;

import static by.bsu.wialontransport.util.CollectionUtil.mapToList;

public abstract class CRUDService<
        ID,
        ENTITY extends Entity<ID>,
        DTO extends Dto<ID>,
        MAPPER extends Mapper<ENTITY, DTO>,
        REPOSITORY extends JpaRepository<ENTITY, ID>
        > {
    private final MAPPER mapper;
    private final REPOSITORY repository;

    public CRUDService(final MAPPER mapper, final REPOSITORY repository) {
        this.mapper = mapper;
        this.repository = repository;
    }

    @Transactional(readOnly = true)
    public Optional<DTO> findById(final ID id) {
        return this.findUnique(repository -> repository.findById(id));
    }

    @Transactional(readOnly = true)
    public List<DTO> findByIds(final Iterable<ID> ids) {
        return mapToList(this.repository.findAllById(ids), this.mapper::mapToDto);
    }

    @Transactional(readOnly = true)
    public boolean isExist(final ID id) {
        return this.repository.existsById(id);
    }

    public DTO update(final DTO dto) {
        this.checkIdDefined(dto);
        final ENTITY initialEntity = this.mapper.mapToEntity(dto);
        final ENTITY updatedEntity = this.repository.save(initialEntity);
        return this.mapper.mapToDto(updatedEntity);
    }

    public void delete(final ID id) {
        this.checkIdDefined(id);
        this.repository.deleteById(id);
    }

    public DTO save(final DTO dto) {
        final ENTITY entityToBeSaved = this.mapToConfiguredEntity(dto);
        final ENTITY savedEntity = this.repository.save(entityToBeSaved);
        return this.mapper.mapToDto(savedEntity);
    }

    public List<DTO> saveAll(final Collection<DTO> dtos) {
        final List<ENTITY> entitiesToBeSaved = this.mapToConfiguredEntities(dtos);
        final List<ENTITY> savedEntities = this.repository.saveAll(entitiesToBeSaved);
        return mapToList(savedEntities, this.mapper::mapToDto);
    }

    protected abstract void configureBeforeSave(final ENTITY entity);

    protected final Optional<DTO> findUnique(final Function<REPOSITORY, Optional<ENTITY>> operation) {
        return operation.apply(this.repository).map(this.mapper::mapToDto);
    }

    protected final boolean findBoolean(final Predicate<REPOSITORY> operation) {
        return operation.test(this.repository);
    }

    protected final int findInt(final ToIntFunction<REPOSITORY> operation) {
        return operation.applyAsInt(this.repository);
    }

    protected <D> D findEntityStreamAndCollect(final Function<REPOSITORY, Stream<ENTITY>> operation,
                                               final Collector<ENTITY, ?, D> collector) {
        try (final Stream<ENTITY> stream = operation.apply(this.repository)) {
            return stream.collect(collector);
        }
    }

    protected Stream<DTO> findDtoStream(final Function<REPOSITORY, Stream<ENTITY>> operation) {
        return operation.apply(this.repository).map(this.mapper::mapToDto);
    }

    private void checkIdDefined(final DTO dto) {
        this.checkIdDefined(dto.getId());
    }

    private void checkIdDefined(final ID id) {
        if (this.isIdNotDefined(id)) {
            throw new IllegalArgumentException("Entity should have defined id");
        }
    }

    private boolean isIdNotDefined(final ID id) {
        if (id == null) {
            return true;
        }
        if (id instanceof final Number idNumber) {
            return idNumber.longValue() == 0;
        }
        return false;
    }

    private ENTITY mapToConfiguredEntity(final DTO dto) {
        final ENTITY entity = this.mapper.mapToEntity(dto);
        this.configureBeforeSave(entity);
        return entity;
    }

    private List<ENTITY> mapToConfiguredEntities(final Collection<DTO> dtos) {
        return mapToList(dtos, this::mapToConfiguredEntity);
    }
}