package by.bsu.wialontransport.crud.repository;

import by.bsu.wialontransport.crud.entity.AddressEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface AddressRepository extends JpaRepository<AddressEntity, Long> {

    //TODO: отсканировать всю карту а потом получать ближайшее население от точки, 135-136 - postgis book
    @Query(value = "SELECT id, bounding_box, center, city_name, country_name "
            + "FROM addresses WHERE ST_INTERSECTS(bounding_box, ST_BUFFER(ST_SETSRID(ST_POINT(:longitude, :latitude), 4326), 0.000002, 8))",
            nativeQuery = true)
    List<AddressEntity> findByGpsCoordinates(final double latitude, final double longitude);

}
