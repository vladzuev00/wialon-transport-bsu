package by.vladzuev.locationreceiver.crud.repository;

import by.vladzuev.locationreceiver.crud.entity.TrackerEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface TrackerRepository extends EntityWithPasswordRepository<Long, TrackerEntity> {

    @Query
    Optional<TrackerEntity> findByImei(final String imei);

    @Query("SELECT e FROM TrackerEntity e WHERE e.user.id = :userId ORDER BY e.imei")
    Page<TrackerEntity> findByUserIdOrderedByImei(final Long userId, final Pageable pageable);

    @Query("SELECT e FROM TrackerEntity e JOIN FETCH e.user WHERE e.id = :id")
    Optional<TrackerEntity> findByIdFetchingUser(final Long id);

    Optional<TrackerEntity> findByPhoneNumber(final String phoneNumber);

    @Override
    @Modifying
    @Query("UPDATE TrackerEntity e SET e.password = :newEncryptedPassword WHERE e.id = :id")
    int updatePassword(final Long id, final String newEncryptedPassword);

    @Query("SELECT e FROM TrackerEntity e JOIN FETCH e.mileage WHERE e.id = :id")
    Optional<TrackerEntity> findByIdFetchingMileage(final Long id);

    //TODO: test
    @Query
    boolean existsByImei(final String imei);

    //TODO: test
    @Query
    boolean existsByPhoneNumber(final String phoneNumber);
}
