package by.bsu.wialontransport.crud.repository;

import by.bsu.wialontransport.crud.entity.AddressEntity;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.LineString;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface AddressRepository extends JpaRepository<AddressEntity, Long> {

    //'0 AS clazz_' - because of inheritance strategy for AddressEntity is JOINED:
    //https://stackoverflow.com/questions/49804053/psqlexception-the-column-name-clazz-was-not-found-in-this-resultset
    @Query(value = "SELECT id, bounding_box, center, city_name, country_name, geometry, 0 AS clazz_ "
            + "FROM addresses WHERE ST_Intersects(geometry, ST_SetSRID(ST_Point(:longitude, :latitude), 4326))",
            nativeQuery = true)
    Optional<AddressEntity> findByGpsCoordinates(final double latitude, final double longitude);

    @Query(value = "SELECT id, bounding_box, center, city_name, country_name, geometry, 0 AS clazz_ "
            + "FROM addresses WHERE ST_Equals(addresses.geometry, :geometry)",
            nativeQuery = true)
    Optional<AddressEntity> findByGeometry(final Geometry geometry);

    @Query(value = "SELECT EXISTS(SELECT 1 FROM addresses WHERE ST_Equals(addresses.geometry, :geometry))",
            nativeQuery = true)
    boolean isExistByGeometry(final Geometry geometry);

    //TODO: test
    @Query(value = "SELECT addresses.id, bounding_box, center, city_name, country_name, geometry, 0 AS clazz_ "
            + "FROM cities "
            + "INNER JOIN addresses "
            + "ON cities.address_id = addresses.id "
            + "WHERE ST_Intersects(geometry, :lineString)",
            nativeQuery = true)
    List<AddressEntity> findCityAddressesIntersectedByLineString(final LineString lineString);
}
