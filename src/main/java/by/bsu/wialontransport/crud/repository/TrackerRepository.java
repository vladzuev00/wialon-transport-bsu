package by.bsu.wialontransport.crud.repository;

import by.bsu.wialontransport.crud.entity.TrackerEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TrackerRepository extends JpaRepository<TrackerEntity, Long> {
    Optional<TrackerEntity> findByImei(final String imei);
}
