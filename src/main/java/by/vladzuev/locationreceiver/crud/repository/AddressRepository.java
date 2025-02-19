package by.vladzuev.locationreceiver.crud.repository;

import by.vladzuev.locationreceiver.crud.entity.AddressEntity;
import org.locationtech.jts.geom.Point;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface AddressRepository extends JpaRepository<AddressEntity, Long> {

    @Query("SELECT e FROM AddressEntity e WHERE INTERSECTS(e.boundingBox, :point) AND INTERSECTS(e.geometry, :point)")
    Optional<AddressEntity> findByPoint(final Point point);
}
