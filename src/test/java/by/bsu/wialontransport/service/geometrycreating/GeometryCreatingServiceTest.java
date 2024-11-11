package by.bsu.wialontransport.service.geometrycreating;

import by.bsu.wialontransport.base.AbstractSpringBootTest;
import by.bsu.wialontransport.model.GpsCoordinate;
import org.junit.Test;
import org.locationtech.jts.geom.CoordinateXY;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.LineString;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.impl.CoordinateArraySequence;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static java.util.Collections.emptyList;
import static org.junit.Assert.assertEquals;

public final class GeometryCreatingServiceTest extends AbstractSpringBootTest {

    @Autowired
    private GeometryCreatingService geometryCreatingService;

    @Autowired
    private GeometryFactory geometryFactory;

    @Test
    public void lineStringShouldBeCreatedByCoordinates() {
        final List<GpsCoordinate> givenCoordinates = List.of(
                new GpsCoordinate(1., 2.),
                new GpsCoordinate(3., 4.),
                new GpsCoordinate(5., 6.),
                new GpsCoordinate(7., 8.)
        );

        final LineString actual = geometryCreatingService.createLineString(givenCoordinates);
        final LineString expected = new LineString(
                new CoordinateArraySequence(
                        new CoordinateXY[]{
                                new CoordinateXY(2., 1.),
                                new CoordinateXY(4., 3.),
                                new CoordinateXY(6., 5.),
                                new CoordinateXY(8., 7.)
                        }
                ),
                geometryFactory
        );
        assertEquals(expected, actual);
    }

    @Test
    public void lineStringShouldBeCreatedByEmptyCoordinates() {
        final List<GpsCoordinate> givenCoordinates = emptyList();

        final LineString actual = geometryCreatingService.createLineString(givenCoordinates);
        final LineString expected = new LineString(new CoordinateArraySequence(new CoordinateXY[]{}), geometryFactory);
        assertEquals(expected, actual);
    }

    @Test
    public void pointShouldBeCreatedByCoordinate() {
        final GpsCoordinate givenCoordinate = new GpsCoordinate(1., 2.);

        final Point actual = geometryCreatingService.createPoint(givenCoordinate);
        final Point expected = geometryFactory.createPoint(new CoordinateXY(2., 1.));
        assertEquals(expected, actual);
    }

}
