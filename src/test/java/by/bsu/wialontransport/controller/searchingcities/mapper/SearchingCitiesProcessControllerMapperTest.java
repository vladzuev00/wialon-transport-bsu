package by.bsu.wialontransport.controller.searchingcities.mapper;

import by.bsu.wialontransport.base.AbstractSpringBootTest;
import by.bsu.wialontransport.controller.searchingcities.model.SearchingCitiesProcessResponse;
import by.bsu.wialontransport.crud.dto.SearchingCitiesProcess;
import by.bsu.wialontransport.crud.entity.SearchingCitiesProcessEntity.Status;
import by.bsu.wialontransport.model.AreaCoordinate;
import by.bsu.wialontransport.model.AreaCoordinateRequest;
import by.bsu.wialontransport.model.GpsCoordinate;
import by.bsu.wialontransport.model.CoordinateRequest;
import by.bsu.wialontransport.util.GeometryTestUtil;
import org.junit.Test;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.wololo.geojson.Polygon;
import org.wololo.jts2geojson.GeoJSONWriter;

import static by.bsu.wialontransport.crud.entity.SearchingCitiesProcessEntity.Status.HANDLING;
import static by.bsu.wialontransport.util.GeometryTestUtil.createPolygon;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

public final class SearchingCitiesProcessControllerMapperTest extends AbstractSpringBootTest {

    @Autowired
    private SearchingCitiesProcessControllerMapper mapper;

    @Autowired
    private GeometryFactory geometryFactory;

    @Autowired
    private GeoJSONWriter geoJSONWriter;

    @Test
    public void processShouldBeMappedToResponse() {
        final Long givenId = 255L;
        final Geometry givenBounds = createPolygon(
                geometryFactory,
                1, 1, 1, 4, 4, 4, 4, 1
        );
        final double givenSearchStep = 0.5;
        final long givenTotalPoints = 1000;
        final long givenHandledPoints = 100;
        final Status givenStatus = HANDLING;
        final SearchingCitiesProcess givenProcess = SearchingCitiesProcess.builder()
                .id(givenId)
                .bounds(givenBounds)
                .searchStep(givenSearchStep)
                .totalPoints(givenTotalPoints)
                .handledPoints(givenHandledPoints)
                .status(givenStatus)
                .build();

        final SearchingCitiesProcessResponse actual = mapper.mapToResponse(givenProcess);
        final SearchingCitiesProcessResponse expected = SearchingCitiesProcessResponse.builder()
                .id(givenId)
                .bounds(geoJSONWriter.write(givenBounds))
                .searchStep(givenSearchStep)
                .totalPoints(givenTotalPoints)
                .handledPoints(givenHandledPoints)
                .status(givenStatus)
                .build();
        checkEquals(expected, actual);
    }

    @Test
    public void requestShouldBeMappedToAreaCoordinate() {
        final AreaCoordinateRequest givenRequest = new AreaCoordinateRequest(
                new CoordinateRequest(1.1, 2.2),
                new CoordinateRequest(3.3, 4.4)
        );

        final AreaCoordinate actual = mapper.mapToAreaCoordinate(givenRequest);
        final AreaCoordinate expected = new AreaCoordinate(
                new GpsCoordinate(1.1, 2.2),
                new GpsCoordinate(3.3, 4.4)
        );
        assertEquals(expected, actual);
    }

    private static void checkEquals(final SearchingCitiesProcessResponse expected,
                                    final SearchingCitiesProcessResponse actual) {
        assertEquals(expected.getId(), actual.getId());
        GeometryTestUtil.checkEquals((Polygon) expected.getBounds(), (Polygon) actual.getBounds());
        assertEquals(expected.getSearchStep(), actual.getSearchStep(), 0.);
        assertEquals(expected.getTotalPoints(), actual.getTotalPoints());
        assertEquals(expected.getHandledPoints(), actual.getHandledPoints());
        assertSame(expected.getStatus(), actual.getStatus());
    }
}
