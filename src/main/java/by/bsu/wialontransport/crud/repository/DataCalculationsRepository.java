package by.bsu.wialontransport.crud.repository;

import by.bsu.wialontransport.crud.entity.DataCalculationsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface DataCalculationsRepository extends JpaRepository<DataCalculationsEntity, Long> {

    @Query("SELECT e FROM DataCalculationsEntity e WHERE e.data.tracker.id = :trackerId")
    Optional<DataCalculationsEntity> findTrackerLastDataCalculationsByTrackerId(final Long trackerId);
}
