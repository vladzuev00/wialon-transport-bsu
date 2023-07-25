package by.bsu.wialontransport.crud.repository;

import by.bsu.wialontransport.crud.entity.CityEntity;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.LineString;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CityRepository extends JpaRepository<CityEntity, Long> {

    @Query(value = "SELECT EXISTS("
            + "SELECT 1 FROM cities INNER JOIN addresses ON cities.address_id = addresses.id "
            + "WHERE ST_Equals(addresses.geometry, :geometry)"
            + ")",
            nativeQuery = true)
    boolean isExistByGeometry(final Geometry geometry);

    @Query(value = "SELECT cities.id, address_id, searching_cities_process_id FROM cities "
            + "INNER JOIN addresses "
            + "ON cities.address_id = addresses.id "
            + "WHERE ST_Intersects(geometry, :lineString)",
            nativeQuery = true)
    List<CityEntity> findCitiesIntersectedByLineString(final LineString lineString);

}
