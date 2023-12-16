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

public abstract class AbstractReadService<
        ID,
        ENTITY extends Entity<ID>,
        DTO extends Dto<ID>,
        MAPPER extends Mapper<ENTITY, DTO>,
        REPOSITORY extends JpaRepository<ENTITY, ID>
        > {
    private final MAPPER mapper;
    private final REPOSITORY repository;

    public AbstractReadService(final MAPPER mapper, final REPOSITORY repository) {
        this.mapper = mapper;
        this.repository = repository;
    }

    @Transactional(readOnly = true)
    public Optional<DTO> findById(final ID id) {
        return this.findUnique(repository -> repository.findById(id));
    }

    @Transactional(readOnly = true)
    public List<DTO> findById(final Collection<ID> ids) {
        final List<ENTITY> foundEntities = this.repository.findAllById(ids);
        return mapToList(foundEntities, this.mapper::mapToDto);
    }

    @Transactional(readOnly = true)
    public boolean isExist(final ID id) {
        return this.repository.existsById(id);
    }

    protected final Optional<DTO> findUnique(final Function<REPOSITORY, Optional<ENTITY>> operation) {
        return operation.apply(this.repository).map(this.mapper::mapToDto);
    }

    protected final boolean findBoolean(final Predicate<REPOSITORY> operation) {
        return operation.test(this.repository);
    }

    protected final int findInt(final ToIntFunction<REPOSITORY> operation) {
        return operation.applyAsInt(this.repository);
    }

    protected <D> D findStreamAndCollect(final Function<REPOSITORY, Stream<ENTITY>> operation,
                                         final Collector<ENTITY, ?, D> collector) {
        try (final Stream<ENTITY> stream = operation.apply(this.repository)) {
            return stream.collect(collector);
        }
    }

    protected Stream<DTO> findDtoStream(final Function<REPOSITORY, Stream<ENTITY>> operation) {
        return operation.apply(this.repository).map(this.mapper::mapToDto);
    }

//    protected final List<DTO> findPaged(final BiFunction<REPOSITORY, Pageable, Collection<ENTITY>> operation,
//                                        final int pageNumber,
//                                        final int pageSize) {
//        final Pageable pageable = PageRequest.of(pageNumber, pageSize);
//        return this.find(operation, pageable);
//    }
//
//    protected final List<DTO> findUnPaged(final BiFunction<REPOSITORY, Pageable, Collection<ENTITY>> operation) {
//        final Pageable pageable = unpaged();
//        return this.find(operation, pageable);
//    }
//
//    private List<DTO> find(final BiFunction<REPOSITORY, Pageable, Collection<ENTITY>> operation,
//                           final Pageable pageable) {
//        final Collection<ENTITY> entities = operation.apply(this.repository, pageable);
//        return mapToList(entities, this.mapper::mapToDto);
//    }
}
