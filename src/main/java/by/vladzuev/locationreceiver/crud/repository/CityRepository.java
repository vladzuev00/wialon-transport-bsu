package by.vladzuev.locationreceiver.crud.repository;

import by.vladzuev.locationreceiver.crud.entity.AddressEntity;
import by.vladzuev.locationreceiver.crud.entity.CityEntity;
import org.locationtech.jts.geom.LineString;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.stream.Stream;

public interface CityRepository extends JpaRepository<CityEntity, Long> {

    @Query(
            """
                    SELECT e.address FROM CityEntity e
                    WHERE INTERSECTS(e.address.boundingBox, :lineString)
                    AND INTERSECTS(e.address.geometry, :lineString)"""
    )
    Stream<AddressEntity> streamIntersectedAddresses(final LineString lineString);
}
