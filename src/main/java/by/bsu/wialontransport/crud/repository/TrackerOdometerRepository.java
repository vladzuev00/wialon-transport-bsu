package by.bsu.wialontransport.crud.repository;

import by.bsu.wialontransport.crud.entity.TrackerMileageEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface TrackerOdometerRepository extends JpaRepository<TrackerMileageEntity, Long> {

    @Modifying
    @Query("UPDATE TrackerOdometerEntity e SET e.")
}
