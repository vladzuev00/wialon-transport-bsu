package by.bsu.wialontransport.crud.repository;

import by.bsu.wialontransport.crud.entity.AddressEntity;
import org.locationtech.jts.geom.Geometry;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface AddressRepository extends JpaRepository<AddressEntity, Long> {

    @Query(value = "SELECT id, bounding_box, center, city_name, country_name, geometry "
            + "FROM addresses WHERE ST_Intersects(geometry, ST_SetSRID(ST_Point(:longitude, :latitude), 4326))",
            nativeQuery = true)
    Optional<AddressEntity> findByGpsCoordinates(final double latitude, final double longitude);

    @Query(value = "SELECT id, bounding_box, center, city_name, country_name, geometry "
            + "FROM addresses WHERE ST_Equals(addresses.geometry, :geometry)",
            nativeQuery = true)
    Optional<AddressEntity> findAddressByGeometry(final Geometry geometry);
}
