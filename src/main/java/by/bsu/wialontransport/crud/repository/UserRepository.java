package by.bsu.wialontransport.crud.repository;

import by.bsu.wialontransport.crud.entity.UserEntity;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UserRepository extends EntityWithPasswordRepository<Long, UserEntity> {

    Optional<UserEntity> findByEmail(final String email);

    boolean existsByEmail(final String email);

    @Override
    @Modifying
    @Query(value = "UPDATE UserEntity e SET e.password = :newPassword WHERE e.id = :userId")
    void updatePassword(final Long userId, final String newPassword);

}
