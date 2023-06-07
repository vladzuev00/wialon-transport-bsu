package by.bsu.wialontransport.crud.repository;

import by.bsu.wialontransport.crud.entity.AbstractEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EntityWithPasswordRepository<IdType, EntityType extends AbstractEntity<IdType>>
        extends JpaRepository<EntityType, IdType> {
    void updatePassword(final IdType id, final String encryptedPassword);
}
