package by.bsu.wialontransport.crud.repository;

import by.bsu.wialontransport.crud.entity.SearchingCitiesProcessEntity;
import by.bsu.wialontransport.crud.entity.SearchingCitiesProcessEntity.Status;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface SearchingCitiesProcessRepository extends JpaRepository<SearchingCitiesProcessEntity, Long> {

    @Modifying
    @Query("UPDATE SearchingCitiesProcessEntity e SET e.status = :newStatus WHERE e.id = :id")
    int updateStatus(final Long id, final Status newStatus);

    @Modifying
    @Query("UPDATE SearchingCitiesProcessEntity e "
            + "SET e.handledPoints = e.handledPoints + :delta "
            + "WHERE e.id = :id")
    int increaseHandledPoints(final Long id, final long delta);

    @Query("SELECT e FROM SearchingCitiesProcessEntity e WHERE e.status = :status ORDER BY e.id")
    Page<SearchingCitiesProcessEntity> findByStatusOrderedById(final Status status, final Pageable pageable);

}
