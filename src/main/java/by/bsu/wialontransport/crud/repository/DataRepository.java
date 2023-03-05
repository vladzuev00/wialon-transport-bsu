package by.bsu.wialontransport.crud.repository;

import by.bsu.wialontransport.crud.entity.DataEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface DataRepository extends JpaRepository<DataEntity, Long> {

    @Query(value = "SELECT data.id, date, time, "
            + "latitude_degrees, latitude_minutes, latitude_minute_share, latitude_type, "
            + "longitude_degrees, longitude_minutes, longitude_minute_share, longitude_type, "
            + "speed, course, altitude, amount_of_satellites, reduction_precision, inputs, outputs, analog_inputs, "
            + "driver_key_code, data.tracker_id "
            + "FROM data INNER JOIN trackers_last_data ON data.id = trackers_last_data.data_id "
            + "WHERE trackers_last_data.tracker_id = :trackerId", nativeQuery = true)
    Optional<DataEntity> findTrackerLastDataByTrackerId(final Long trackerId);
}
