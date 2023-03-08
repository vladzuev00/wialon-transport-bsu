package by.bsu.wialontransport.crud.repository;

import by.bsu.wialontransport.crud.entity.DataEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface DataRepository extends JpaRepository<DataEntity, Long> {

    @Query("SELECT de FROM DataEntity de "
            + "LEFT JOIN FETCH de.parameters "
            + "JOIN FETCH de.tracker "
            + "WHERE de.id = (SELECT tlde.data.id FROM TrackerLastDataEntity tlde WHERE tlde.tracker.id = :trackerId)")
    Optional<DataEntity> findTrackerLastDataByTrackerId(final Long trackerId);
}
