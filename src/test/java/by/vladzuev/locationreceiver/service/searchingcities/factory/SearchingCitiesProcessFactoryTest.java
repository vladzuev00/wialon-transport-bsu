package by.vladzuev.locationreceiver.service.searchingcities.factory;

import by.vladzuev.locationreceiver.base.AbstractSpringBootTest;
import by.vladzuev.locationreceiver.crud.dto.SearchingCitiesProcess;
import by.vladzuev.locationreceiver.model.AreaCoordinate;
import by.vladzuev.locationreceiver.model.GpsCoordinate;
import by.vladzuev.locationreceiver.crud.entity.SearchingCitiesProcessEntity;
import org.junit.Test;
import org.locationtech.jts.geom.GeometryFactory;
import org.springframework.beans.factory.annotation.Autowired;

import static by.vladzuev.locationreceiver.util.GeometryTestUtil.createPolygon;
import static org.junit.Assert.assertEquals;

public final class SearchingCitiesProcessFactoryTest extends AbstractSpringBootTest {

    @Autowired
    private SearchingCitiesProcessFactory factory;

    @Autowired
    private GeometryFactory geometryFactory;

    @Test
    public void processShouldBeCreated() {
        final AreaCoordinate givenAreaCoordinate = new AreaCoordinate(
                new GpsCoordinate(5., 5.),
                new GpsCoordinate(10.2, 10.2)
        );
        final double searchStep = 1;

        final SearchingCitiesProcess actual = factory.create(givenAreaCoordinate, searchStep);
        final SearchingCitiesProcess expected = SearchingCitiesProcess.builder()
                .bounds(createPolygon(geometryFactory, 5, 5, 5, 10.2, 10.2, 10.2, 10.2, 5))
                .searchStep(searchStep)
                .totalPoints(36)
                .status(SearchingCitiesProcessEntity.Status.HANDLING)
                .build();
        assertEquals(expected, actual);
    }
}
