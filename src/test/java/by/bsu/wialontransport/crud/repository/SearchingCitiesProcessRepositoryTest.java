package by.bsu.wialontransport.crud.repository;

import by.bsu.wialontransport.base.AbstractContextTest;
import by.bsu.wialontransport.crud.entity.SearchingCitiesProcessEntity;
import org.junit.Test;
import org.locationtech.jts.geom.GeometryFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;
import java.util.Set;

import static by.bsu.wialontransport.crud.entity.SearchingCitiesProcessEntity.Status.*;
import static by.bsu.wialontransport.util.GeometryTestUtil.createPolygon;
import static by.bsu.wialontransport.util.entity.EntityUtil.mapToIds;
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
        super.startQueryCount();
        final SearchingCitiesProcessEntity actual = this.repository.findById(255L).orElseThrow();
        super.checkQueryCount(1);

        final SearchingCitiesProcessEntity expected = SearchingCitiesProcessEntity.builder()
                .id(255L)
                .bounds(createPolygon(this.geometryFactory, 1, 1, 1, 4, 4, 4, 4, 1))
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
                .bounds(createPolygon(this.geometryFactory, 1, 1, 1, 4, 4, 4, 4, 1))
                .searchStep(0.5)
                .totalPoints(1000)
                .handledPoints(100)
                .status(HANDLING)
                .build();

        super.startQueryCount();
        this.repository.save(givenProcess);
        super.checkQueryCount(1);
    }

    @Test
    @Sql("classpath:sql/searching-cities-process/insert-searching-cities-processes.sql")
    public void statusOfProcessShouldBeUpdated() {
        super.startQueryCount();
        final int actualCountUpdatedRows = this.repository.updateStatus(255L, SUCCESS);
        super.checkQueryCount(1);

        final int expectedCountUpdatedRows = 1;
        assertEquals(expectedCountUpdatedRows, actualCountUpdatedRows);

        final SearchingCitiesProcessEntity actual = this.repository.findById(255L).orElseThrow();
        final SearchingCitiesProcessEntity expected = SearchingCitiesProcessEntity.builder()
                .id(255L)
                .bounds(createPolygon(this.geometryFactory, 1, 1, 1, 4, 4, 4, 4, 1))
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
        super.startQueryCount();
        final int actualCountUpdatedRows = this.repository.updateStatus(MAX_VALUE, SUCCESS);
        super.checkQueryCount(1);

        final int expectedCountUpdatedRows = 0;
        assertEquals(expectedCountUpdatedRows, actualCountUpdatedRows);
    }

    @Test
    @Sql("classpath:sql/searching-cities-process/insert-searching-cities-processes.sql")
    public void handledPointsShouldBeIncreased() {
        super.startQueryCount();
        final int actualCountUpdatedRows = this.repository.increaseHandledPoints(255L, 100);
        super.checkQueryCount(1);

        final int expectedCountUpdatedRows = 1;
        assertEquals(expectedCountUpdatedRows, actualCountUpdatedRows);

        final SearchingCitiesProcessEntity actual = this.repository.findById(255L).orElseThrow();
        final SearchingCitiesProcessEntity expected = SearchingCitiesProcessEntity.builder()
                .id(255L)
                .bounds(createPolygon(this.geometryFactory, 1, 1, 1, 4, 4, 4, 4, 1))
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
        super.startQueryCount();
        final int actualCountUpdatedRows = this.repository.increaseHandledPoints(MAX_VALUE, 100);
        super.checkQueryCount(1);

        final int expectedCountUpdatedRows = 0;
        assertEquals(expectedCountUpdatedRows, actualCountUpdatedRows);
    }

    @Test
    @Sql("classpath:sql/searching-cities-process/insert-searching-cities-processes.sql")
    public void processesShouldBeFoundByStatus() {
        super.startQueryCount();
        final List<SearchingCitiesProcessEntity> actual = this.repository.findByStatus(HANDLING, ofSize(4));
        super.checkQueryCount(1);

        final Set<Long> actualIds = mapToIds(actual);
        final Set<Long> expectedIds = Set.of(255L, 256L);
        assertEquals(expectedIds, actualIds);
    }

    @Test
    @Sql("classpath:sql/searching-cities-process/insert-searching-cities-processes.sql")
    public void processesShouldNotBeFoundByStatus() {
        super.startQueryCount();
        final List<SearchingCitiesProcessEntity> actual = this.repository.findByStatus(ERROR, ofSize(4));
        super.checkQueryCount(1);

        assertTrue(actual.isEmpty());
    }
}
