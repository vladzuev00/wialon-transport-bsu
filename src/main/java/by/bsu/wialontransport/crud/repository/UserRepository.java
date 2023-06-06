package by.bsu.wialontransport.crud.repository;

import by.bsu.wialontransport.crud.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, Long> {

    Optional<UserEntity> findByEmail(final String email);

    boolean existsByEmail(final String email);

    @Modifying
    @Query(value = "UPDATE users SET encrypted_password = :newPassword WHERE users.id = :userId", nativeQuery = true)
    void updatePassword(final Long userId, final String newPassword);

}
