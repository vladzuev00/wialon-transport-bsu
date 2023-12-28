package by.bsu.wialontransport.service.geometrycreating;

import by.bsu.wialontransport.base.AbstractContextTest;
import by.bsu.wialontransport.model.RequestCoordinate;
import by.bsu.wialontransport.model.Track;
import org.junit.Test;
import org.locationtech.jts.geom.CoordinateXY;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.LineString;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.impl.CoordinateArraySequence;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.junit.Assert.assertEquals;

public final class GeometryCreatingServiceTest extends AbstractContextTest {

    @Autowired
    private GeometryCreatingService geometryCreatingService;

    @Autowired
    private GeometryFactory geometryFactory;

    @Test
    public void lineStringShouldBeCreatedByTrack() {
        final Track givenTrack = new Track(
                List.of(
                        new RequestCoordinate(1., 2.),
                        new RequestCoordinate(3., 4.),
                        new RequestCoordinate(5., 6.),
                        new RequestCoordinate(7., 8.)
                )
        );

        final LineString actual = this.geometryCreatingService.createLineString(givenTrack);
        final LineString expected = new LineString(
                new CoordinateArraySequence(
                        new org.locationtech.jts.geom.Coordinate[]{
                                new CoordinateXY(2., 1.),
                                new CoordinateXY(4., 3.),
                                new CoordinateXY(6., 5.),
                                new CoordinateXY(8., 7.)
                        }
                ),
                this.geometryFactory
        );
        assertEquals(expected, actual);
    }

    @Test
    public void pointShouldBeCreatedByCoordinate() {
        final RequestCoordinate givenCoordinate = new RequestCoordinate(1., 2.);

        final Point actual = this.geometryCreatingService.createPoint(givenCoordinate);
        final Point expected = this.geometryFactory.createPoint(new org.locationtech.jts.geom.Coordinate(2., 1.));
        assertEquals(expected, actual);
    }

}
