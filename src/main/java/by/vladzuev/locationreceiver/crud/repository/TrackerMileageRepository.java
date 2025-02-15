package by.vladzuev.locationreceiver.crud.repository;

import by.vladzuev.locationreceiver.crud.entity.TrackerMileageEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface TrackerMileageRepository extends JpaRepository<TrackerMileageEntity, Long> {

    @Modifying
    @Query("UPDATE TrackerMileageEntity me SET me.urban = me.urban + :urbanDelta, me.country = me.country + :countryDelta "
            + "WHERE me.id = (SELECT te.mileage.id FROM TrackerEntity te WHERE te.id = :trackerId)")
    int increaseMileage(final Long trackerId, final double urbanDelta, final double countryDelta);

}
