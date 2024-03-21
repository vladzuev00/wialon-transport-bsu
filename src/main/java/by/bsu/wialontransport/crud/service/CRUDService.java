package by.bsu.wialontransport.crud.service;

import by.bsu.wialontransport.crud.dto.Dto;
import by.bsu.wialontransport.crud.entity.Entity;
import by.bsu.wialontransport.crud.mapper.Mapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
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

@Transactional
@RequiredArgsConstructor
public abstract class CRUDService<
        ID,
        ENTITY extends Entity<ID>,
        DTO extends Dto<ID>,
        MAPPER extends Mapper<ENTITY, DTO>,
        REPOSITORY extends JpaRepository<ENTITY, ID>
        > {
    private final MAPPER mapper;
    private final REPOSITORY repository;

    @Transactional(readOnly = true)
    public Optional<DTO> findById(final ID id) {
        return findUniqueDto(repository -> repository.findById(id));
    }

    @Transactional(readOnly = true)
    public List<DTO> findByIds(final Iterable<ID> ids) {
        return mapToList(repository.findAllById(ids), mapper::mapToDto);
    }

    @Transactional(readOnly = true)
    public boolean isExist(final ID id) {
        return repository.existsById(id);
    }

    public DTO update(final DTO dto) {
        checkIdDefined(dto);
        final ENTITY initialEntity = mapper.mapToEntity(dto);
        final ENTITY updatedEntity = repository.save(initialEntity);
        return mapper.mapToDto(updatedEntity);
    }

    public void delete(final ID id) {
        checkDefined(id);
        repository.deleteById(id);
    }

    public DTO save(final DTO dto) {
        final ENTITY entityToBeSaved = mapToConfiguredEntity(dto);
        final ENTITY savedEntity = repository.save(entityToBeSaved);
        return mapper.mapToDto(savedEntity);
    }

    public List<DTO> saveAll(final Collection<DTO> dtos) {
        final List<ENTITY> entitiesToBeSaved = mapToConfiguredEntities(dtos);
        final List<ENTITY> savedEntities = repository.saveAll(entitiesToBeSaved);
        return mapToList(savedEntities, mapper::mapToDto);
    }

    protected abstract void configureBeforeSave(final ENTITY entity);

    protected final Optional<DTO> findUniqueDto(final Function<REPOSITORY, Optional<ENTITY>> operation) {
        return execute(operation).map(mapper::mapToDto);
    }

    protected final <T> T execute(final Function<REPOSITORY, T> operation) {
        return operation.apply(repository);
    }

    protected final boolean findBoolean(final Predicate<REPOSITORY> operation) {
        return operation.test(repository);
    }

    protected final int findInt(final ToIntFunction<REPOSITORY> operation) {
        return operation.applyAsInt(repository);
    }

    protected <D> D findEntityStreamAndCollect(final Function<REPOSITORY, Stream<ENTITY>> operation,
                                               final Collector<ENTITY, ?, D> collector) {
        try (final Stream<ENTITY> stream = execute(operation)) {
            return stream.collect(collector);
        }
    }

    protected Stream<DTO> findDtoStream(final Function<REPOSITORY, Stream<ENTITY>> operation) {
        return execute(operation).map(mapper::mapToDto);
    }

    protected Page<DTO> findDtoPage(final Function<REPOSITORY, Page<ENTITY>> operation) {
        return execute(operation).map(mapper::mapToDto);
    }

    private void checkIdDefined(final DTO dto) {
        checkDefined(dto.getId());
    }

    private void checkDefined(final ID id) {
        if (isIdNotDefined(id)) {
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
        final ENTITY entity = mapper.mapToEntity(dto);
        configureBeforeSave(entity);
        return entity;
    }

    private List<ENTITY> mapToConfiguredEntities(final Collection<DTO> dtos) {
        return mapToList(dtos, this::mapToConfiguredEntity);
    }
}