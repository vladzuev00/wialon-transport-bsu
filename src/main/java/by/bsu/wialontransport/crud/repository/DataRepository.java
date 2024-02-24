package by.bsu.wialontransport.crud.repository;

import by.bsu.wialontransport.crud.entity.DataEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.stream.Stream;

public interface DataRepository extends JpaRepository<DataEntity, Long> {

    @Query(
            "SELECT de FROM DataEntity de LEFT JOIN FETCH de.parameters WHERE de.id = "
                    + "(SELECT tlde.data.id FROM TrackerLastDataEntity tlde WHERE tlde.tracker.id = :trackerId)"
    )
    Optional<DataEntity> findTrackerLastDataByTrackerIdFetchingParameters(final Long trackerId);


    @Query(
            "SELECT de FROM DataEntity de "
                    + "JOIN FETCH de.tracker te "
                    + "JOIN FETCH de.address "
                    + "WHERE te.user.id = :userId "
                    + "AND de.dateTime BETWEEN :startDateTime AND :endDateTime"
    )
    Stream<DataEntity> findDataByUserIdFetchingTrackerAndAddress(final Long userId,
                                                                 final LocalDateTime startDateTime,
                                                                 final LocalDateTime endDateTime);

    @Query("SELECT e.data.dateTime FROM TrackerLastDataEntity e WHERE e.tracker.id = :trackerId")
    Optional<LocalDateTime> findTrackerLastDataDateTime(final Long trackerId);
}
