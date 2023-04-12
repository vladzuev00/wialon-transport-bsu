package by.bsu.wialontransport.crud.repository;

import by.bsu.wialontransport.crud.entity.AddressEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface AddressRepository extends JpaRepository<AddressEntity, Long> {

    @Query(value = "select * from geocoding where st_intersects(ST_BUFFER(st_setsrid(ST_POINT(?2, ?1), 4326), 0.000002, 8), boundaries)  AND language = ?3",
            nativeQuery = true)
    List<AddressEntity> findByGpsCoordinateAndLanguage(float latitude, float longitude, String language);
}
