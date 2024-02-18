package by.bsu.wialontransport.crud.repository;

import by.bsu.wialontransport.base.AbstractSpringBootTest;
import by.bsu.wialontransport.crud.entity.SearchingCitiesProcessEntity;
import by.bsu.wialontransport.crud.entity.SearchingCitiesProcessEntity.Status;
import org.junit.Test;
import org.locationtech.jts.geom.GeometryFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;
import java.util.Optional;

import static by.bsu.wialontransport.crud.entity.SearchingCitiesProcessEntity.Status.*;
import static by.bsu.wialontransport.util.GeometryTestUtil.createPolygon;
import static by.bsu.wialontransport.util.PageUtil.mapToIds;
import static by.bsu.wialontransport.util.entity.SearchingCitiesProcessEntityUtil.checkEquals;
import static java.lang.Long.MAX_VALUE;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.springframework.data.domain.Pageable.ofSize;

public final class SearchingCitiesProcessRepositoryTest extends AbstractSpringBootTest {

    @Autowired
    private SearchingCitiesProcessRepository repository;

    @Autowired
    private GeometryFactory geometryFactory;

    @Test
    @Sql("classpath:sql/searching-cities-process/insert-searching-cities-processes.sql")
    public void processShouldBeFoundById() {
        final Long givenId = 255L;

        startQueryCount();
        final Optional<SearchingCitiesProcessEntity> optionalActual = repository.findById(givenId);
        checkQueryCount(1);

        assertTrue(optionalActual.isPresent());
        final SearchingCitiesProcessEntity actual = optionalActual.get();
        final SearchingCitiesProcessEntity expected = SearchingCitiesProcessEntity.builder()
                .id(givenId)
                .bounds(createPolygon(geometryFactory, 1, 1, 1, 4, 4, 4, 4, 1))
                .searchStep(0.5)
                .totalPoints(1000)
                .handledPoints(100)
                .status(HANDLING)
                .build();
        checkEquals(expected, actual);
    }

    @Test
    public void processShouldBeSaved() {
        final SearchingCitiesProcessEntity givenProcess = SearchingCitiesProcessEntity.builder()
                .bounds(createPolygon(geometryFactory, 1, 1, 1, 4, 4, 4, 4, 1))
                .searchStep(0.5)
                .totalPoints(1000)
                .handledPoints(100)
                .status(HANDLING)
                .build();

        startQueryCount();
        repository.save(givenProcess);
        checkQueryCount(1);
    }

    @Test
    @Sql("classpath:sql/searching-cities-process/insert-searching-cities-processes.sql")
    public void statusOfProcessShouldBeUpdated() {
        final Long givenId = 255L;
        final Status givenNewStatus = SUCCESS;

        startQueryCount();
        final int actualCountUpdatedRows = repository.updateStatus(givenId, givenNewStatus);
        checkQueryCount(1);

        final int expectedCountUpdatedRows = 1;
        assertEquals(expectedCountUpdatedRows, actualCountUpdatedRows);

        final SearchingCitiesProcessEntity actual = repository.findById(givenId).orElseThrow();
        final SearchingCitiesProcessEntity expected = SearchingCitiesProcessEntity.builder()
                .id(givenId)
                .bounds(createPolygon(geometryFactory, 1, 1, 1, 4, 4, 4, 4, 1))
                .searchStep(0.5)
                .totalPoints(1000)
                .handledPoints(100)
                .status(givenNewStatus)
                .build();
        checkEquals(expected, actual);
    }

    @Test
    @Sql("classpath:sql/searching-cities-process/insert-searching-cities-processes.sql")
    public void statusOfProcessShouldBeUpdatedBecauseOfNotExistingProcessId() {
        final Long givenId = MAX_VALUE;

        startQueryCount();
        final int actualCountUpdatedRows = repository.updateStatus(givenId, SUCCESS);
        checkQueryCount(1);

        final int expectedCountUpdatedRows = 0;
        assertEquals(expectedCountUpdatedRows, actualCountUpdatedRows);
    }

    @Test
    @Sql("classpath:sql/searching-cities-process/insert-searching-cities-processes.sql")
    public void handledPointsShouldBeIncreased() {
        final Long givenId = 255L;
        final long givenDelta = 100;

        startQueryCount();
        final int actualCountUpdatedRows = repository.increaseHandledPoints(givenId, givenDelta);
        checkQueryCount(1);

        final int expectedCountUpdatedRows = 1;
        assertEquals(expectedCountUpdatedRows, actualCountUpdatedRows);

        final SearchingCitiesProcessEntity actual = repository.findById(givenId).orElseThrow();
        final SearchingCitiesProcessEntity expected = SearchingCitiesProcessEntity.builder()
                .id(givenId)
                .bounds(createPolygon(geometryFactory, 1, 1, 1, 4, 4, 4, 4, 1))
                .searchStep(0.5)
                .totalPoints(1000)
                .handledPoints(200)
                .status(HANDLING)
                .build();
        checkEquals(expected, actual);
    }

    @Test
    @Sql("classpath:sql/searching-cities-process/insert-searching-cities-processes.sql")
    public void handledPointsShouldBeIncreasedBecauseOfNotExistingProcessId() {
        final Long givenId = MAX_VALUE;
        final long givenDelta = 100;

        startQueryCount();
        final int actualCountUpdatedRows = repository.increaseHandledPoints(MAX_VALUE, givenDelta);
        checkQueryCount(1);

        final int expectedCountUpdatedRows = 0;
        assertEquals(expectedCountUpdatedRows, actualCountUpdatedRows);
    }

    @Test
    @Sql("classpath:sql/searching-cities-process/insert-searching-cities-processes.sql")
    public void processesOrderedByIdShouldBeFoundByStatus() {
        final Pageable givenPageable = ofSize(4);

        startQueryCount();
        final Page<SearchingCitiesProcessEntity> actual = repository.findByStatusOrderedById(HANDLING, givenPageable);
        checkQueryCount(1);

        final List<Long> actualIds = mapToIds(actual);
        final List<Long> expectedIds = List.of(255L, 256L);
        assertEquals(expectedIds, actualIds);
    }

    @Test
    @Sql("classpath:sql/searching-cities-process/insert-searching-cities-processes.sql")
    public void processesOrderedByIdShouldNotBeFoundByStatus() {
        final Pageable givenPageable = ofSize(4);

        startQueryCount();
        final Page<SearchingCitiesProcessEntity> actual = repository.findByStatusOrderedById(ERROR, givenPageable);
        checkQueryCount(1);

        assertTrue(actual.isEmpty());
    }
}
