package by.vladzuev.locationreceiver.crud.repository;

import by.vladzuev.locationreceiver.crud.entity.SecuredEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface SecuredEntityRepository<ID, ENTITY extends SecuredEntity<ID>>
        extends JpaRepository<ENTITY, ID> {
    int updatePassword(final ID id, final String newEncryptedPassword);
}
