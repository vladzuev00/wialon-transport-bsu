package by.vladzuev.locationreceiver.service.mileage.calculator;

import by.vladzuev.locationreceiver.crud.service.AddressService;
import by.vladzuev.locationreceiver.model.GpsCoordinate;
import by.vladzuev.locationreceiver.model.Mileage;
import by.vladzuev.locationreceiver.service.calculatingdistance.CalculatingDistanceService;
import by.vladzuev.locationreceiver.service.geometrycreating.GeometryCreatingService;
import by.vladzuev.locationreceiver.service.mileage.model.TrackSlice;
import by.vladzuev.locationreceiver.service.coordinatessimplifier.SimplifyingCoordinatesService;
import by.vladzuev.locationreceiver.util.GeometryTestUtil;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.locationtech.jts.geom.*;
import org.locationtech.jts.geom.prep.PreparedGeometry;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

import static java.util.Collections.emptyList;
import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.same;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public final class MileageCalculatorTest {
    private static final int GEOMETRY_FACTORY_SRID = 4326;

    private final GeometryFactory geometryFactory = new GeometryFactory(new PrecisionModel(), GEOMETRY_FACTORY_SRID);

    @Mock
    private SimplifyingCoordinatesService mockedSimplifyingCoordinatesService;

    @Mock
    private GeometryCreatingService mockedGeometryCreatingService;

    @Mock
    private CalculatingDistanceService mockedCalculatingDistanceService;

    @Mock
    private AddressService mockedAddressService;

    private TestMileageCalculator calculator;

    @Before
    public void initializeCalculator() {
        calculator = new TestMileageCalculator(
                mockedSimplifyingCoordinatesService,
                mockedGeometryCreatingService,
                mockedCalculatingDistanceService,
                mockedAddressService
        );
    }

    @Test
    public void mileageShouldBeCalculated() {
        final GpsCoordinate firstGivenCoordinate = new GpsCoordinate(1.1, 1.1);
        final GpsCoordinate secondGivenCoordinate = new GpsCoordinate(3.3, 3.3);
        final GpsCoordinate thirdGivenCoordinate = new GpsCoordinate(5.5, 5.5);
        final GpsCoordinate fourthGivenCoordinate = new GpsCoordinate(7.7, 7.7);
        final List<GpsCoordinate> givenCoordinates = List.of(
                firstGivenCoordinate,
                secondGivenCoordinate,
                thirdGivenCoordinate,
                fourthGivenCoordinate
        );

        final List<GpsCoordinate> givenSimplifiedCoordinates = List.of(
                firstGivenCoordinate,
                secondGivenCoordinate
        );
        when(mockedSimplifyingCoordinatesService.simplify(same(givenCoordinates)))
                .thenReturn(givenSimplifiedCoordinates);

        final LineString givenLineString = mock(LineString.class);
        when(mockedGeometryCreatingService.createLineString(same(givenSimplifiedCoordinates)))
                .thenReturn(givenLineString);

        final Set<PreparedGeometry> givenCityGeometries = Set.of(
                GeometryTestUtil.createPreparedPolygon(
                        geometryFactory,
                        0, 0, 0, 6, 6, 6, 0, 6
                )
        );
        when(mockedAddressService.findCitiesPreparedGeometriesIntersectedByLineString(same(givenLineString)))
                .thenReturn(givenCityGeometries);

        final Point secondGivenCoordinatePoint = createPoint(secondGivenCoordinate);
        when(mockedGeometryCreatingService.createPoint(same(secondGivenCoordinate)))
                .thenReturn(secondGivenCoordinatePoint);

        final Point thirdGivenCoordinatePoint = createPoint(thirdGivenCoordinate);
        when(mockedGeometryCreatingService.createPoint(same(thirdGivenCoordinate)))
                .thenReturn(thirdGivenCoordinatePoint);

        final Point fourthGivenCoordinatePoint = createPoint(fourthGivenCoordinate);
        when(mockedGeometryCreatingService.createPoint(same(fourthGivenCoordinate)))
                .thenReturn(fourthGivenCoordinatePoint);

        final double firstDistance = 20.4;
        when(mockedCalculatingDistanceService.calculate(same(firstGivenCoordinate), same(secondGivenCoordinate)))
                .thenReturn(firstDistance);

        final double secondDistance = 50.5;
        when(mockedCalculatingDistanceService.calculate(same(secondGivenCoordinate), same(thirdGivenCoordinate)))
                .thenReturn(secondDistance);

        final double thirdDistance = 30.4;
        when(mockedCalculatingDistanceService.calculate(same(thirdGivenCoordinate), same(fourthGivenCoordinate)))
                .thenReturn(thirdDistance);

        final Mileage actual = calculator.calculate(givenCoordinates);
        final Mileage expected = new Mileage(firstDistance + secondDistance, thirdDistance);
        assertEquals(expected, actual);
    }

    @Test
    public void zeroMileageShouldBeCalculatedByOneCoordinate() {
        final List<GpsCoordinate> givenCoordinates = List.of(new GpsCoordinate(7.7, 7.7));

        final Mileage actual = calculator.calculate(givenCoordinates);
        final Mileage expected = new Mileage(0, 0);
        assertEquals(expected, actual);
    }

    @Test
    public void zeroMileageShouldBeCalculatedByEmptyCoordinates() {
        final List<GpsCoordinate> givenCoordinates = emptyList();

        final Mileage actual = calculator.calculate(givenCoordinates);
        final Mileage expected = new Mileage(0, 0);
        assertEquals(expected, actual);
    }

    private Point createPoint(final GpsCoordinate coordinate) {
        return geometryFactory.createPoint(
                new CoordinateXY(
                        coordinate.getLatitude(),
                        coordinate.getLatitude()
                )
        );
    }

    public static final class TestMileageCalculator extends MileageCalculator {

        public TestMileageCalculator(final SimplifyingCoordinatesService simplifyingCoordinatesService,
                                     final GeometryCreatingService geometryCreatingService,
                                     final CalculatingDistanceService calculatingDistanceService,
                                     final AddressService addressService) {
            super(simplifyingCoordinatesService, geometryCreatingService, calculatingDistanceService, addressService);
        }

        @Override
        protected Stream<TrackSlice> createTrackSlices(final TrackSlicesCreatingContext context) {
            final TrackSlice trackSlice = createTrackSlice(context);
            return Stream.of(trackSlice);
        }

        private static TrackSlice createTrackSlice(final TrackSlicesCreatingContext context) {
            return new TrackSlice(
                    context.getFirstCoordinate(),
                    context.getSecondCoordinate(),
                    isAnyGeometryContainSecondPoint(context)
            );
        }

        private static boolean isAnyGeometryContainSecondPoint(final TrackSlicesCreatingContext context) {
            return context.getCityGeometries()
                    .stream()
                    .anyMatch(geometry -> geometry.contains(context.getSecondPoint()));
        }
    }
}
