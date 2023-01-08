package by.bsu.wialontransport.crud.repository;

import by.bsu.wialontransport.crud.entity.DataEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface DataRepository extends JpaRepository<DataEntity, Long> {

    @Query("SELECT e FROM DataEntity e WHERE e.tracker.id = :trackerId")
    Optional<DataEntity> findTrackerLastData(final Long trackerId);
}
