package by.vladzuev.locationreceiver.crud.repository;

import by.vladzuev.locationreceiver.crud.entity.AddressEntity;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.LineString;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import java.util.stream.Stream;

public interface AddressRepository extends JpaRepository<AddressEntity, Long> {

    @Query(value = "SELECT id, bounding_box, center, city_name, country_name, geometry "
            + "FROM addresses WHERE ST_Intersects(geometry, ST_SetSRID(ST_Point(:longitude, :latitude), 4326))",
            nativeQuery = true)
    Optional<AddressEntity> findByGpsCoordinates(final double latitude, final double longitude);

    @Query(value = "SELECT id, bounding_box, center, city_name, country_name, geometry "
            + "FROM addresses WHERE ST_Equals(addresses.geometry, :geometry)",
            nativeQuery = true)
    Optional<AddressEntity> findByGeometry(final Geometry geometry);

    @Query(value = "SELECT EXISTS(SELECT 1 FROM addresses WHERE ST_Equals(addresses.geometry, :geometry))",
            nativeQuery = true)
    boolean isExistByGeometry(final Geometry geometry);

    @Query(value = "SELECT addresses.id, bounding_box, center, city_name, country_name, geometry "
            + "FROM cities "
            + "INNER JOIN addresses "
            + "ON cities.address_id = addresses.id "
            + "WHERE ST_Intersects(geometry, :lineString)",
            nativeQuery = true)
    Stream<AddressEntity> findCityAddressesIntersectedByLineString(final LineString lineString);
}
