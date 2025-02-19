package by.vladzuev.locationreceiver.crud.repository;

import by.vladzuev.locationreceiver.crud.entity.LocationEntity;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface TrackerLastDataRepository {

//    @Query(
//            "SELECT de FROM LocationEntity de LEFT JOIN FETCH de.parameters WHERE de.id = "
//                    + "(SELECT tlde.data.id FROM TrackerLastDataEntity tlde WHERE tlde.tracker.id = :trackerId)"
//    )
//    Optional<LocationEntity> findTrackerLastDataByTrackerIdFetchingParameters(final Long trackerId);

//     @Query("SELECT e.data.dateTime FROM TrackerLastDataEntity e WHERE e.tracker.id = :trackerId")
//    Optional<LocalDateTime> findTrackerLastDataDateTimeByTrackerId(final Long trackerId);

    //@Query(
    //            "SELECT de FROM LocationEntity de WHERE de.id = "
    //                    + "(SELECT tlde.data.id FROM TrackerLastDataEntity tlde WHERE tlde.tracker.id = :trackerId)"
    //    )
    //    Optional<LocationEntity> findTrackerLastDataByTrackerId(final Long trackerId);
}
