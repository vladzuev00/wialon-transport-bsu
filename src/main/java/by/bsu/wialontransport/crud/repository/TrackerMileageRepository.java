package by.bsu.wialontransport.crud.repository;

import by.bsu.wialontransport.crud.entity.TrackerMileageEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface TrackerMileageRepository extends JpaRepository<TrackerMileageEntity, Long> {

    @Modifying
    @Query("UPDATE TrackerMileageEntity m SET m.urban = m.urban + :urbanDelta, m.country = m.country + :countryDelta "
            + "WHERE m.id = (SELECT t.mileage.id TrackerEntity t WHERE t.id = :trackerId)")
    void increaseMileage(final Long trackerId, final double urbanDelta, final double countryDelta);

}
