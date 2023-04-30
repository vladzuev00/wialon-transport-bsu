package by.bsu.wialontransport.crud.repository;

import by.bsu.wialontransport.base.AbstractContextTest;
import by.bsu.wialontransport.crud.entity.SearchingCitiesProcessEntity;
import org.junit.Test;
import org.locationtech.jts.geom.GeometryFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;

import static by.bsu.wialontransport.crud.entity.SearchingCitiesProcessEntity.Status.HANDLING;
import static by.bsu.wialontransport.util.EntityUtil.checkEquals;
import static by.bsu.wialontransport.util.GeometryUtil.createPolygon;

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
}
