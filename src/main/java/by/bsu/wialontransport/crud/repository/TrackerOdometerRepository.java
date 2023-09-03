package by.bsu.wialontransport.crud.repository;

import by.bsu.wialontransport.crud.entity.TrackerOdometerEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface TrackerOdometerRepository extends JpaRepository<TrackerOdometerEntity, Long> {

    @Modifying
    @Query("UPDATE TrackerOdometerEntity e SET e.")
}
