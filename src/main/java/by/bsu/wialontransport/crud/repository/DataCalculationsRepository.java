package by.bsu.wialontransport.crud.repository;

import by.bsu.wialontransport.crud.entity.DataCalculationsEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DataCalculationsRepository extends JpaRepository<DataCalculationsEntity, Long> {
    Optional<DataCalculationsEntity> findTrackerLastDataCalculationsByTrackerId(final Long trackerId);
}
