package by.bsu.wialontransport.crud.service;

import by.bsu.wialontransport.crud.dto.Dto;
import by.bsu.wialontransport.crud.entity.Entity;
import by.bsu.wialontransport.crud.mapper.Mapper;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public abstract class AbstractRUDService<
        ID,
        ENTITY extends Entity<ID>,
        DTO extends Dto<ID>,
        MAPPER extends Mapper<ENTITY, DTO>,
        REPOSITORY extends JpaRepository<ENTITY, ID>
        >
        extends AbstractReadService<ID, ENTITY, DTO, MAPPER, REPOSITORY> {

    public AbstractRUDService(final MAPPER mapper, final REPOSITORY repository) {
        super(mapper, repository);
    }

    public DTO update(final DTO dto) {
        this.checkId(dto);
        final ENTITY initialEntity = super.mapper.mapToEntity(dto);
        final ENTITY updatedEntity = super.repository.save(initialEntity);
        return this.mapper.mapToDto(updatedEntity);
    }

    public void delete(final ID id) {
        this.checkId(id);
        super.repository.deleteById(id);
    }

    private void checkId(final DTO dto) {
        final ID id = dto.getId();
        this.checkId(id);
    }

    private void checkId(final ID id) {
        if (this.isIdNotDefined(id)) {
            throw new IllegalArgumentException("Entity should have id: (not null or 0), but was id: %s.".formatted(id));
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

}
