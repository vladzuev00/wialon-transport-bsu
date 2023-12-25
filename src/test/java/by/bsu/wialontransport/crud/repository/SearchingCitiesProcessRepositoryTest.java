package by.bsu.wialontransport.crud.repository;

import by.bsu.wialontransport.base.AbstractContextTest;
import by.bsu.wialontransport.crud.entity.SearchingCitiesProcessEntity;
import org.junit.Test;
import org.locationtech.jts.geom.GeometryFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;

import static by.bsu.wialontransport.crud.entity.SearchingCitiesProcessEntity.Status.*;
import static by.bsu.wialontransport.util.GeometryTestUtil.createPolygon;
import static by.bsu.wialontransport.util.PageUtil.mapToIds;
import static by.bsu.wialontransport.util.entity.SearchingCitiesProcessEntityUtil.checkEquals;
import static java.lang.Long.MAX_VALUE;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.springframework.data.domain.Pageable.ofSize;

public final class SearchingCitiesProcessRepositoryTest extends AbstractContextTest {

    @Autowired
    private SearchingCitiesProcessRepository repository;

    @Autowired
    private GeometryFactory geometryFactory;

    @Test
    @Sql("classpath:sql/searching-cities-process/insert-searching-cities-processes.sql")
    public void processShouldBeFoundById() {
        startQueryCount();
        final SearchingCitiesProcessEntity actual = repository.findById(255L).orElseThrow();
        checkQueryCount(1);

        final SearchingCitiesProcessEntity expected = SearchingCitiesProcessEntity.builder()
                .id(255L)
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
        startQueryCount();
        final int actualCountUpdatedRows = repository.updateStatus(255L, SUCCESS);
        checkQueryCount(1);

        final int expectedCountUpdatedRows = 1;
        assertEquals(expectedCountUpdatedRows, actualCountUpdatedRows);

        final SearchingCitiesProcessEntity actual = repository.findById(255L).orElseThrow();
        final SearchingCitiesProcessEntity expected = SearchingCitiesProcessEntity.builder()
                .id(255L)
                .bounds(createPolygon(geometryFactory, 1, 1, 1, 4, 4, 4, 4, 1))
                .searchStep(0.5)
                .totalPoints(1000)
                .handledPoints(100)
                .status(SUCCESS)
                .build();
        checkEquals(expected, actual);
    }

    @Test
    @Sql("classpath:sql/searching-cities-process/insert-searching-cities-processes.sql")
    public void statusOfProcessShouldBeUpdatedBecauseOfNotExistingProcessId() {
        startQueryCount();
        final int actualCountUpdatedRows = repository.updateStatus(MAX_VALUE, SUCCESS);
        checkQueryCount(1);

        final int expectedCountUpdatedRows = 0;
        assertEquals(expectedCountUpdatedRows, actualCountUpdatedRows);
    }

    @Test
    @Sql("classpath:sql/searching-cities-process/insert-searching-cities-processes.sql")
    public void handledPointsShouldBeIncreased() {
        startQueryCount();
        final int actualCountUpdatedRows = repository.increaseHandledPoints(255L, 100);
        checkQueryCount(1);

        final int expectedCountUpdatedRows = 1;
        assertEquals(expectedCountUpdatedRows, actualCountUpdatedRows);

        final SearchingCitiesProcessEntity actual = repository.findById(255L).orElseThrow();
        final SearchingCitiesProcessEntity expected = SearchingCitiesProcessEntity.builder()
                .id(255L)
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
        startQueryCount();
        final int actualCountUpdatedRows = repository.increaseHandledPoints(MAX_VALUE, 100);
        checkQueryCount(1);

        final int expectedCountUpdatedRows = 0;
        assertEquals(expectedCountUpdatedRows, actualCountUpdatedRows);
    }

    @Test
    @Sql("classpath:sql/searching-cities-process/insert-searching-cities-processes.sql")
    public void processesShouldBeFoundByStatus() {
        startQueryCount();
        final Page<SearchingCitiesProcessEntity> actual = repository.findByStatusOrderedById(HANDLING, ofSize(4));
        checkQueryCount(1);

        final List<Long> actualIds = mapToIds(actual);
        final List<Long> expectedIds = List.of(255L, 256L);
        assertEquals(expectedIds, actualIds);
    }

    @Test
    @Sql("classpath:sql/searching-cities-process/insert-searching-cities-processes.sql")
    public void processesShouldNotBeFoundByStatus() {
        startQueryCount();
        final Page<SearchingCitiesProcessEntity> actual = repository.findByStatusOrderedById(ERROR, ofSize(4));
        checkQueryCount(1);

        assertTrue(actual.isEmpty());
    }
}
