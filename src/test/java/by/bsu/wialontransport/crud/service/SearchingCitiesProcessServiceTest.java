package by.bsu.wialontransport.crud.service;

import by.bsu.wialontransport.base.AbstractContextTest;
import by.bsu.wialontransport.crud.dto.SearchingCitiesProcess;
import org.junit.Test;
import org.locationtech.jts.geom.GeometryFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;

import static by.bsu.wialontransport.crud.entity.SearchingCitiesProcessEntity.Status.ERROR;
import static by.bsu.wialontransport.crud.entity.SearchingCitiesProcessEntity.Status.HANDLING;
import static by.bsu.wialontransport.util.EntityUtil.createSearchingCitiesProcess;
import static by.bsu.wialontransport.util.GeometryUtil.createPolygon;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.springframework.data.domain.Pageable.ofSize;

public final class SearchingCitiesProcessServiceTest extends AbstractContextTest {

    @Autowired
    private SearchingCitiesProcessService service;

    @Autowired
    private GeometryFactory geometryFactory;

    @Test
    @Sql(statements = "INSERT INTO searching_cities_processes("
            + "id, bounds, search_step, total_points, handled_points, status) "
            + "VALUES(255, ST_GeomFromText('POLYGON((1 1, 1 4, 4 4, 4 1, 1 1))', 4326), 0.5, 1000, 100, 'HANDLING')")
    public void statusOfProcessShouldBeUpdated() {
        final Long givenId = 255L;
        final SearchingCitiesProcess givenProcess = createSearchingCitiesProcess(givenId);

        this.service.updateStatus(givenProcess, ERROR);

        final SearchingCitiesProcess actual = this.service.findById(givenId).orElseThrow();
        final SearchingCitiesProcess expected = SearchingCitiesProcess.builder()
                .id(255L)
                .bounds(createPolygon(this.geometryFactory, 1, 1, 1, 4, 4, 4, 4, 1))
                .searchStep(0.5)
                .totalPoints(1000)
                .handledPoints(100)
                .status(ERROR)
                .build();
        assertEquals(expected, actual);
    }

    @Test
    @Sql(statements = "INSERT INTO searching_cities_processes("
            + "id, bounds, search_step, total_points, handled_points, status) "
            + "VALUES(255, ST_GeomFromText('POLYGON((1 1, 1 4, 4 4, 4 1, 1 1))', 4326), 0.5, 1000, 100, 'HANDLING')")
    public void handledPointsShouldBeIncreased() {
        final Long givenId = 255L;
        final SearchingCitiesProcess givenProcess = createSearchingCitiesProcess(givenId);

        this.service.increaseHandledPoints(givenProcess, 100);

        final SearchingCitiesProcess actual = this.service.findById(givenId).orElseThrow();
        final SearchingCitiesProcess expected = SearchingCitiesProcess.builder()
                .id(255L)
                .bounds(createPolygon(this.geometryFactory, 1, 1, 1, 4, 4, 4, 4, 1))
                .searchStep(0.5)
                .totalPoints(1000)
                .handledPoints(200)
                .status(HANDLING)
                .build();
        assertEquals(expected, actual);
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
        final List<SearchingCitiesProcess> actual = this.service.findByStatus(HANDLING, 0, 4);
        final List<SearchingCitiesProcess> expected = List.of(
                SearchingCitiesProcess.builder()
                        .id(255L)
                        .bounds(createPolygon(this.geometryFactory, 1, 1, 1, 4, 4, 4, 4, 1))
                        .searchStep(0.5)
                        .totalPoints(1000)
                        .handledPoints(100)
                        .status(HANDLING)
                        .build(),
                SearchingCitiesProcess.builder()
                        .id(256L)
                        .bounds(createPolygon(this.geometryFactory, 1, 1, 1, 4, 4, 4, 4, 1))
                        .searchStep(0.5)
                        .totalPoints(1000)
                        .handledPoints(100)
                        .status(HANDLING)
                        .build()
        );
        assertEquals(expected, actual);
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
    public void processesShouldNotBeFoundByStatus() {
        final List<SearchingCitiesProcess> actual = this.service.findByStatus(ERROR, 0, 4);
        assertTrue(actual.isEmpty());
    }
}
