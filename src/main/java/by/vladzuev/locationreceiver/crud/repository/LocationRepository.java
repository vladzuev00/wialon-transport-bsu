package by.vladzuev.locationreceiver.crud.repository;

import by.vladzuev.locationreceiver.crud.entity.LocationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.stream.Stream;

public interface LocationRepository extends JpaRepository<LocationEntity, Long> {

    @Query(
            """
                    SELECT le FROM LocationEntity le
                    JOIN FETCH le.tracker te
                    JOIN FETCH le.address
                    WHERE te.user.id = :userId AND le.dateTime BETWEEN :startDateTime AND :endDateTime"""
    )
    Stream<LocationEntity> streamAllByUserIdFetchingTrackerAndAddress(final Long userId,
                                                                      final LocalDateTime startDateTime,
                                                                      final LocalDateTime endDateTime);

    @Query(
            """
                    SELECT le FROM LocationEntity le
                    WHERE le.id = (SELECT lle.location.id FROM LastLocationEntity lle WHERE lle.tracker.id = :trackerId)"""
    )
    Optional<LocationEntity> findTrackerLast(final Long trackerId);

    @Query(
            """
                    SELECT le FROM LocationEntity le
                    LEFT JOIN FETCH le.parameters
                    WHERE le.id = (SELECT lle.location.id FROM LastLocationEntity lle WHERE lle.tracker.id = :trackerId)"""
    )
    Optional<LocationEntity> findTrackerLastFetchingParameters(final Long trackerId);
}
