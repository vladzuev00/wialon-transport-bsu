package by.bsu.wialontransport.crud.repository;

import by.bsu.wialontransport.crud.entity.TrackerEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface TrackerRepository extends JpaRepository<TrackerEntity, Long> {

    Optional<TrackerEntity> findByImei(final String imei);

    //TODO: test
    @Query("SELECT e FROM TrackerEntity e WHERE e.user.id = :userId")
    List<TrackerEntity> findByUserId(final Long userId, final Pageable pageable);

}
