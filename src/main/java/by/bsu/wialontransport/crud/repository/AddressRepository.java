package by.bsu.wialontransport.crud.repository;

import by.bsu.wialontransport.crud.entity.AddressEntity;
import org.locationtech.jts.geom.Geometry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface AddressRepository extends JpaRepository<AddressEntity, Long> {

    @Query(value = "SELECT id, bounding_box, center, city_name, country_name "
            + "FROM addresses WHERE ST_INTERSECTS(bounding_box, ST_SETSRID(ST_POINT(:longitude, :latitude), 4326))",
            nativeQuery = true)
    List<AddressEntity> findByGpsCoordinates(final double latitude, final double longitude);

    //TODO: tests
    @Query(value = "SELECT id, bounding_box, center, city_name, country_name "
            + "FROM addresses WHERE ST_EQUALS(addresses.bounding_box, :boundingBox)", nativeQuery = true)
    Optional<AddressEntity> findAddressByBoundingBox(final Geometry boundingBox);
}
