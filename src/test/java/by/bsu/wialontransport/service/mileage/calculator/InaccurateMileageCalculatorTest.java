package by.bsu.wialontransport.service.mileage.calculator;

import by.bsu.wialontransport.model.GpsCoordinate;
import by.bsu.wialontransport.service.mileage.calculator.MileageCalculator.TrackSlicesCreatingContext;
import by.bsu.wialontransport.service.mileage.model.TrackSlice;
import org.junit.Test;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.prep.PreparedGeometry;

import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.same;
import static org.mockito.Mockito.*;

public final class InaccurateMileageCalculatorTest {
    private final InaccurateMileageCalculator calculator = new InaccurateMileageCalculator(
            null,
            null,
            null,
            null
    );

    @Test
    public void trackSliceLocatedInCityShouldBeCreated() {
        final TrackSlicesCreatingContext givenContext = mock(TrackSlicesCreatingContext.class);

        final GpsCoordinate firstGivenCoordinate = new GpsCoordinate(5.5, 6.6);
        when(givenContext.getFirstCoordinate()).thenReturn(firstGivenCoordinate);

        final GpsCoordinate secondGivenCoordinate = new GpsCoordinate(7.7, 8.8);
        when(givenContext.getSecondCoordinate()).thenReturn(secondGivenCoordinate);

        final Point secondGivenCoordinatePoint = mock(Point.class);
        when(givenContext.getSecondPoint()).thenReturn(secondGivenCoordinatePoint);

        final PreparedGeometry firstGivenCityGeometry = mock(PreparedGeometry.class);
        final PreparedGeometry secondGivenCityGeometry = mock(PreparedGeometry.class);
        final Set<PreparedGeometry> givenGeometries = Set.of(firstGivenCityGeometry, secondGivenCityGeometry);
        when(givenContext.getCityGeometries()).thenReturn(givenGeometries);

        when(firstGivenCityGeometry.contains(same(secondGivenCoordinatePoint))).thenReturn(false);
        when(secondGivenCityGeometry.contains(same(secondGivenCoordinatePoint))).thenReturn(true);

        final Stream<TrackSlice> actual = calculator.createTrackSlices(givenContext);
        final List<TrackSlice> actualAsList = actual.toList();
        final List<TrackSlice> expectedAsList = List.of(
                new TrackSlice(firstGivenCoordinate, secondGivenCoordinate, true)
        );
        assertEquals(expectedAsList, actualAsList);
    }

    @Test
    public void trackSliceNotLocatedInCityShouldBeCreated() {
        final TrackSlicesCreatingContext givenContext = mock(TrackSlicesCreatingContext.class);

        final GpsCoordinate firstGivenCoordinate = new GpsCoordinate(5.5, 6.6);
        when(givenContext.getFirstCoordinate()).thenReturn(firstGivenCoordinate);

        final GpsCoordinate secondGivenCoordinate = new GpsCoordinate(7.7, 8.8);
        when(givenContext.getSecondCoordinate()).thenReturn(secondGivenCoordinate);

        final Point secondGivenCoordinatePoint = mock(Point.class);
        when(givenContext.getSecondPoint()).thenReturn(secondGivenCoordinatePoint);

        final PreparedGeometry firstGivenCityGeometry = mock(PreparedGeometry.class);
        final PreparedGeometry secondGivenCityGeometry = mock(PreparedGeometry.class);
        final Set<PreparedGeometry> givenGeometries = Set.of(firstGivenCityGeometry, secondGivenCityGeometry);
        when(givenContext.getCityGeometries()).thenReturn(givenGeometries);

        when(firstGivenCityGeometry.contains(same(secondGivenCoordinatePoint))).thenReturn(false);
        when(secondGivenCityGeometry.contains(same(secondGivenCoordinatePoint))).thenReturn(false);

        final Stream<TrackSlice> actual = calculator.createTrackSlices(givenContext);
        final List<TrackSlice> actualAsList = actual.toList();
        final List<TrackSlice> expectedAsList = List.of(
                new TrackSlice(firstGivenCoordinate, secondGivenCoordinate, false)
        );
        assertEquals(expectedAsList, actualAsList);

        verify(firstGivenCityGeometry, times(1)).contains(same(secondGivenCoordinatePoint));
        verify(secondGivenCityGeometry, times(1)).contains(same(secondGivenCoordinatePoint));
    }
}
