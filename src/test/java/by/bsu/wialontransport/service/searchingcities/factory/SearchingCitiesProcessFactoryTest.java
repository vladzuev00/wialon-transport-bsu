package by.bsu.wialontransport.service.searchingcities.factory;

import by.bsu.wialontransport.base.AbstractContextTest;
import by.bsu.wialontransport.crud.dto.SearchingCitiesProcess;
import by.bsu.wialontransport.model.AreaCoordinateRequest;
import by.bsu.wialontransport.model.RequestCoordinate;
import org.junit.Test;
import org.locationtech.jts.geom.GeometryFactory;
import org.springframework.beans.factory.annotation.Autowired;

import static by.bsu.wialontransport.crud.entity.SearchingCitiesProcessEntity.Status.HANDLING;
import static by.bsu.wialontransport.util.GeometryTestUtil.createPolygon;
import static org.junit.Assert.assertEquals;

public final class SearchingCitiesProcessFactoryTest extends AbstractContextTest {

    @Autowired
    private SearchingCitiesProcessFactory factory;

    @Autowired
    private GeometryFactory geometryFactory;

    @Test
    public void processShouldBeCreated() {
        final AreaCoordinateRequest givenAreaCoordinate = new AreaCoordinateRequest(
                new RequestCoordinate(5., 5.),
                new RequestCoordinate(10.2, 10.2)
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
