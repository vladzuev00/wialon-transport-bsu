package by.bsu.wialontransport.crud.repository;

import by.bsu.wialontransport.crud.entity.EntityWithPassword;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface EntityWithPasswordRepository<IdType, EntityType extends EntityWithPassword<IdType>>
        extends JpaRepository<EntityType, IdType> {
    void updatePassword(final IdType id, final String encryptedPassword);
}
