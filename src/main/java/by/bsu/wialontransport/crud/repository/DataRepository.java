package by.bsu.wialontransport.crud.repository;

import by.bsu.wialontransport.crud.entity.DataEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface DataRepository extends JpaRepository<DataEntity, Long> {

    //    @Query("SELECT de FROM DataEntity de "
//            + "LEFT JOIN FETCH de.parameters "
//            + "WHERE de.id = (SELECT tlde.data.id FROM TrackerLastDataEntity tlde WHERE tlde.tracker.id = :trackerId)")
    @Query("SELECT de FROM DataEntity de WHERE id = :trackerId")
    Optional<DataEntity> findTrackerLastDataByTrackerId(final Long trackerId);

    //    @Query("SELECT de FROM DataEntity de "
//            + "JOIN FETCH de.tracker te "
//            + "JOIN FETCH de.address "
//            + "WHERE te.user.id = :userId "
//            + "AND (de.date + de.time) BETWEEN :startDate AND :endDate")
    @Query("SELECT de FROM DataEntity de")
    List<DataEntity> findDataWithTrackerAndAddressOfUser(final Long userId,
                                                         final LocalDate startDate,
                                                         final LocalDate endDate);
}
