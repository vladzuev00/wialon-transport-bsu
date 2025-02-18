package by.vladzuev.locationreceiver.crud.repository;

import by.vladzuev.locationreceiver.crud.entity.LocationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.stream.Stream;

public interface DataRepository extends JpaRepository<LocationEntity, Long> {

    @Query(
            "SELECT de FROM LocationEntity de LEFT JOIN FETCH de.parameters WHERE de.id = "
                    + "(SELECT tlde.data.id FROM TrackerLastDataEntity tlde WHERE tlde.tracker.id = :trackerId)"
    )
    Optional<LocationEntity> findTrackerLastDataByTrackerIdFetchingParameters(final Long trackerId);


    @Query(
            "SELECT de FROM LocationEntity de "
                    + "JOIN FETCH de.tracker te "
                    + "JOIN FETCH de.address "
                    + "WHERE te.user.id = :userId "
                    + "AND de.dateTime BETWEEN :startDateTime AND :endDateTime"
    )
    Stream<LocationEntity> findDataByUserIdFetchingTrackerAndAddress(final Long userId,
                                                                     final LocalDateTime startDateTime,
                                                                     final LocalDateTime endDateTime);

    @Query("SELECT e.data.dateTime FROM TrackerLastDataEntity e WHERE e.tracker.id = :trackerId")
    Optional<LocalDateTime> findTrackerLastDataDateTimeByTrackerId(final Long trackerId);

    @Query(
            "SELECT de FROM LocationEntity de WHERE de.id = "
                    + "(SELECT tlde.data.id FROM TrackerLastDataEntity tlde WHERE tlde.tracker.id = :trackerId)"
    )
    Optional<LocationEntity> findTrackerLastDataByTrackerId(final Long trackerId);
}
