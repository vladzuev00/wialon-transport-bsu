package by.bsu.wialontransport.crud.repository;

import by.bsu.wialontransport.base.AbstractContextTest;
import by.bsu.wialontransport.crud.entity.SearchingCitiesProcessEntity;
import org.junit.Test;
import org.locationtech.jts.geom.GeometryFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;
import java.util.Set;

import static by.bsu.wialontransport.crud.entity.SearchingCitiesProcessEntity.Status.HANDLING;
import static by.bsu.wialontransport.crud.entity.SearchingCitiesProcessEntity.Status.SUCCESS;
import static by.bsu.wialontransport.util.EntityUtil.checkEquals;
import static by.bsu.wialontransport.util.GeometryUtil.createPolygon;
import static java.util.stream.Collectors.toSet;
import static org.junit.Assert.assertEquals;
import static org.springframework.data.domain.Pageable.ofSize;

public final class SearchingCitiesProcessRepositoryTest extends AbstractContextTest {

    @Autowired
    private SearchingCitiesProcessRepository repository;

    @Autowired
    private GeometryFactory geometryFactory;

    @Test
    @Sql(statements = "INSERT INTO searching_cities_processes("
            + "id, bounds, search_step, total_points, handled_points, status) "
            + "VALUES(255, ST_GeomFromText('POLYGON((1 1, 1 4, 4 4, 4 1, 1 1))', 4326), 0.5, 1000, 100, 'HANDLING')")
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
    @Sql(statements = "INSERT INTO searching_cities_processes("
            + "id, bounds, search_step, total_points, handled_points, status) "
            + "VALUES(255, ST_GeomFromText('POLYGON((1 1, 1 4, 4 4, 4 1, 1 1))', 4326), 0.5, 1000, 100, 'HANDLING')")
    public void statusOfProcessShouldBeUpdated() {
        super.startQueryCount();
        this.repository.updateStatus(255L, SUCCESS);
        super.checkQueryCount(1);

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
    @Sql(statements = "INSERT INTO searching_cities_processes("
            + "id, bounds, search_step, total_points, handled_points, status) "
            + "VALUES(255, ST_GeomFromText('POLYGON((1 1, 1 4, 4 4, 4 1, 1 1))', 4326), 0.5, 1000, 100, 'HANDLING')")
    public void handledPointsShouldBeIncreased() {
        super.startQueryCount();
        this.repository.increaseHandledPoints(255L, 100);
        super.checkQueryCount(1);

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
    @Sql(statements = "INSERT INTO searching_cities_processes("
            + "id, bounds, search_step, total_points, handled_points, status) "
            + "VALUES(255, ST_GeomFromText('POLYGON((1 1, 1 4, 4 4, 4 1, 1 1))', 4326), 0.5, 1000, 100, 'HANDLING')")
    @Sql(statements = "INSERT INTO searching_cities_processes("
            + "id, bounds, search_step, total_points, handled_points, status) "
            + "VALUES(256, ST_GeomFromText('POLYGON((1 1, 1 4, 4 4, 4 1, 1 1))', 4326), 0.5, 1000, 100, 'HANDLING')")
    @Sql(statements = "INSERT INTO searching_cities_processes("
            + "id, bounds, search_step, total_points, handled_points, status) "
            + "VALUES(257, ST_GeomFromText('POLYGON((1 1, 1 4, 4 4, 4 1, 1 1))', 4326), 0.5, 1000, 100, 'SUCCESS')")
    @Sql(statements = "INSERT INTO searching_cities_processes("
            + "id, bounds, search_step, total_points, handled_points, status) "
            + "VALUES(258, ST_GeomFromText('POLYGON((1 1, 1 4, 4 4, 4 1, 1 1))', 4326), 0.5, 1000, 100, 'ERROR')")
    public void processesShouldBeFoundByStatus() {
        super.startQueryCount();
        final List<SearchingCitiesProcessEntity> actual = this.repository.findByStatus(HANDLING, ofSize(4));
        super.checkQueryCount(1);

        final Set<Long> actualIds = actual.stream()
                .map(SearchingCitiesProcessEntity::getId)
                .collect(toSet());
        final Set<Long> expectedIds = Set.of(255L, 256L);
        assertEquals(expectedIds, actualIds);
    }
}
