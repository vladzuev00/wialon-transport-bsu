package by.bsu.wialontransport.crud.repository;

import by.bsu.wialontransport.crud.entity.EntityWithPassword;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface EntityWithPasswordRepository<ID, ENTITY extends EntityWithPassword<ID>>
        extends JpaRepository<ENTITY, ID> {
    int updatePassword(final ID id, final String newEncryptedPassword);
}
