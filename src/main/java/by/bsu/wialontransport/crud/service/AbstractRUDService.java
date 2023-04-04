package by.bsu.wialontransport.crud.service;

import by.bsu.wialontransport.crud.dto.AbstractDto;
import by.bsu.wialontransport.crud.entity.AbstractEntity;
import by.bsu.wialontransport.crud.mapper.AbstractMapper;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import static java.lang.String.format;

@Transactional
public abstract class AbstractRUDService<
        IdType,
        EntityType extends AbstractEntity<IdType>,
        DtoType extends AbstractDto<IdType>,
        MapperType extends AbstractMapper<EntityType, DtoType>,
        RepositoryType extends JpaRepository<EntityType, IdType>>

        extends AbstractReadService<IdType, EntityType, DtoType, MapperType, RepositoryType> {
    private static final String TEMPLATE_EXCEPTION_MESSAGE_CHECKING_ID
            = "Entity should have id: (not null OR 0), but was id: %s.";

    public AbstractRUDService(final MapperType mapper, final RepositoryType repository) {
        super(mapper, repository);
    }

    public DtoType update(final DtoType updated) {
        checkId(updated.getId());
        final EntityType entityToBeUpdated = super.mapper.mapToEntity(updated);
        final EntityType updatedEntity = super.repository.save(entityToBeUpdated);
        return this.mapper.mapToDto(updatedEntity);
    }

    public void delete(final IdType id) {
        checkId(id);
        super.repository.deleteById(id);
    }

    private void checkId(final IdType id) {
        if (this.isIdNotDefined(id)) {
            throw new IllegalArgumentException(format(TEMPLATE_EXCEPTION_MESSAGE_CHECKING_ID, id));
        }
    }

    private boolean isIdNotDefined(final IdType id) {
        if (id == null) {
            return true;
        }
        if (id instanceof Number) {
            return ((Number) id).longValue() == 0;
        }
        return false;
    }

}
