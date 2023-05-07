package by.bsu.wialontransport.service.searchingservice.factory;

import by.bsu.wialontransport.base.AbstractContextTest;
import by.bsu.wialontransport.crud.dto.SearchingCitiesProcess;
import by.bsu.wialontransport.model.AreaCoordinate;
import by.bsu.wialontransport.model.Coordinate;
import by.bsu.wialontransport.service.searchingcities.factory.SearchingCitiesProcessFactory;
import org.junit.Test;
import org.locationtech.jts.geom.GeometryFactory;
import org.springframework.beans.factory.annotation.Autowired;

import static by.bsu.wialontransport.crud.entity.SearchingCitiesProcessEntity.Status.HANDLING;
import static by.bsu.wialontransport.util.GeometryUtil.createPolygon;
import static org.junit.Assert.assertEquals;

public final class SearchingCitiesProcessFactoryTest extends AbstractContextTest {

    @Autowired
    private SearchingCitiesProcessFactory factory;

    @Autowired
    private GeometryFactory geometryFactory;

    @Test
    public void processShouldBeCreated() {
        final AreaCoordinate givenAreaCoordinate = new AreaCoordinate(
                new Coordinate(5., 5.),
                new Coordinate(10.2, 10.2)
        );
        final double searchStep = 1;

        final SearchingCitiesProcess actual = this.factory.create(givenAreaCoordinate, searchStep);
        final SearchingCitiesProcess expected = SearchingCitiesProcess.builder()
                .bounds(createPolygon(this.geometryFactory, 5, 5, 5, 10.2, 10.2, 10.2, 10.2, 5))
                .searchStep(searchStep)
                .totalPoints(36)
                .status(HANDLING)
                .build();
        assertEquals(expected, actual);
    }
}
