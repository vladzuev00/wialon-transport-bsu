package by.bsu.wialontransport.service.mileage.calculator;

import by.bsu.wialontransport.crud.service.AddressService;
import by.bsu.wialontransport.model.Coordinate;
import by.bsu.wialontransport.model.Mileage;
import by.bsu.wialontransport.service.calculatingdistance.CalculatingDistanceService;
import by.bsu.wialontransport.service.geometrycreating.GeometryCreatingService;
import by.bsu.wialontransport.service.mileage.model.TrackSlice;
import by.bsu.wialontransport.service.simplifyingtrack.SimplifyingCoordinatesService;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.locationtech.jts.geom.LineString;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.prep.PreparedGeometry;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;

import static java.util.stream.Collectors.partitioningBy;
import static java.util.stream.Collectors.summingDouble;
import static java.util.stream.IntStream.range;

@RequiredArgsConstructor
public abstract class MileageCalculator {
    private static final Mileage ZERO_MILEAGE = new Mileage(0, 0);

    private final SimplifyingCoordinatesService simplifyingCoordinatesService;
    private final GeometryCreatingService geometryCreatingService;
    private final CalculatingDistanceService calculatingDistanceService;
    private final AddressService addressService;

    public final Mileage calculate(final List<Coordinate> coordinates) {
        return !isZeroMileage(coordinates) ? calculateNotZeroMileage(coordinates) : ZERO_MILEAGE;
    }

    protected abstract Stream<TrackSlice> createTrackSlices(final TrackSlicesCreatingContext context);

    private static boolean isZeroMileage(final List<Coordinate> coordinates) {
        return coordinates.isEmpty() || coordinates.size() == 1;
    }

    private Mileage calculateNotZeroMileage(final List<Coordinate> coordinates) {
        final Map<Boolean, Double> mileagesByLocatedInCity = calculateMileagesByLocatedInCity(coordinates);
        final double urban = mileagesByLocatedInCity.get(true);
        final double country = mileagesByLocatedInCity.get(false);
        return new Mileage(urban, country);
    }

    private Map<Boolean, Double> calculateMileagesByLocatedInCity(final List<Coordinate> coordinates) {
        final Set<PreparedGeometry> intersectedCityGeometries = findIntersectedCityGeometries(coordinates);
        return range(0, coordinates.size() - 1)
                .mapToObj(i -> createContext(coordinates.get(i), coordinates.get(i + 1), intersectedCityGeometries))
                .flatMap(this::createTrackSlices)
                .collect(
                        partitioningBy(
                                TrackSlice::isLocatedInCity,
                                summingDouble(this::calculateDistance)
                        )
                );
    }

    private Set<PreparedGeometry> findIntersectedCityGeometries(final List<Coordinate> coordinates) {
        final LineString lineString = createLineStringSimplifyingCoordinates(coordinates);
        return addressService.findCitiesPreparedGeometriesIntersectedByLineString(lineString);
    }

    private LineString createLineStringSimplifyingCoordinates(final List<Coordinate> coordinates) {
        final List<Coordinate> simplifiedCoordinates = simplifyingCoordinatesService.simplify(coordinates);
        return geometryCreatingService.createLineString(simplifiedCoordinates);
    }

    private TrackSlicesCreatingContext createContext(final Coordinate firstCoordinate,
                                                     final Coordinate secondCoordinate,
                                                     final Set<PreparedGeometry> cityGeometries) {
        return new TrackSlicesCreatingContext(
                firstCoordinate,
                secondCoordinate,
                cityGeometries,
                geometryCreatingService
        );
    }

    private double calculateDistance(final TrackSlice slice) {
        return calculatingDistanceService.calculate(slice.getFirst(), slice.getSecond());
    }

    @RequiredArgsConstructor
    protected static final class TrackSlicesCreatingContext {

        @Getter
        private final Coordinate firstCoordinate;

        @Getter
        private final Coordinate secondCoordinate;

        @Getter
        private final Set<PreparedGeometry> cityGeometries;

        private final GeometryCreatingService geometryCreatingService;

        public Point getSecondPoint() {
            return geometryCreatingService.createPoint(secondCoordinate);
        }
    }
}
