package by.bsu.wialontransport.crud.repository;

import by.bsu.wialontransport.crud.entity.TrackerEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import java.util.stream.Stream;

public interface TrackerRepository extends EntityWithPasswordRepository<Long, TrackerEntity> {

    Optional<TrackerEntity> findByImei(final String imei);

    @Query("SELECT e FROM TrackerEntity e WHERE e.user.id = :userId")
    Stream<TrackerEntity> findByUserId(final Long userId, final Pageable pageable);

    @Query("SELECT te FROM TrackerEntity te JOIN FETCH te.user WHERE te.id = :id")
    Optional<TrackerEntity> findByIdWithUser(final Long id);

    Optional<TrackerEntity> findByPhoneNumber(final String phoneNumber);

    @Override
    @Modifying
    @Query("UPDATE TrackerEntity e SET e.password = :newEncryptedPassword WHERE e.id = :id")
    int updatePassword(final Long id, final String newEncryptedPassword);
}
