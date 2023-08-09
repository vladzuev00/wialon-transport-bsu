package by.bsu.wialontransport.service.mileage;

import by.bsu.wialontransport.crud.service.AddressService;
import by.bsu.wialontransport.model.Coordinate;
import by.bsu.wialontransport.model.Mileage;
import by.bsu.wialontransport.model.Track;
import by.bsu.wialontransport.service.calculatingdistance.CalculatingDistanceService;
import by.bsu.wialontransport.service.geometrycreating.GeometryCreatingService;
import by.bsu.wialontransport.service.mileage.model.TrackSlice;
import by.bsu.wialontransport.service.simplifyingtrack.SimplifyingTrackService;
import lombok.RequiredArgsConstructor;
import org.locationtech.jts.geom.LineString;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.prep.PreparedGeometry;

import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Stream;

import static java.util.stream.Collectors.partitioningBy;
import static java.util.stream.Collectors.summingDouble;

@RequiredArgsConstructor
public abstract class AbstractMileageCalculatingService {
    private final SimplifyingTrackService simplifyingTrackService;
    private final GeometryCreatingService geometryCreatingService;
    private final CalculatingDistanceService calculatingDistanceService;
    private final AddressService addressService;

    public final Mileage calculate(final Track track) {
        final Map<Boolean, Double> mileagesByLocatedInCity = this.findMileagesByLocatedInCity(track);
        final double urban = mileagesByLocatedInCity.get(true);
        final double country = mileagesByLocatedInCity.get(false);
        return new Mileage(urban, country);
    }

    protected abstract Stream<TrackSlice> createTrackSliceStream(final Track track,
                                                                 final Predicate<Coordinate> locatedInCity);

    private Map<Boolean, Double> findMileagesByLocatedInCity(final Track track) {
        final List<PreparedGeometry> intersectedCitiesGeometries = this.findCitiesGeometriesIntersectedBySimplifiedTrack(
                track
        );
        return this.createTrackSliceStream(
                        track,
                        coordinate -> this.isAnyGeometryContainCoordinate(coordinate, intersectedCitiesGeometries)
                )
                .collect(
                        partitioningBy(
                                TrackSlice::isLocatedInCity,
                                summingDouble(this::calculateDistance)
                        )
                );
    }

    private List<PreparedGeometry> findCitiesGeometriesIntersectedBySimplifiedTrack(final Track track) {
        final LineString lineString = this.createLineStringBySimplifiedTrack(track);
        return this.addressService.findCitiesPreparedGeometriesIntersectedByLineString(lineString);
    }

    private LineString createLineStringBySimplifiedTrack(final Track track) {
        final Track simplifiedTrack = this.simplifyingTrackService.simplify(track);
        return this.geometryCreatingService.createLineString(simplifiedTrack);
    }

    private boolean isAnyGeometryContainCoordinate(final Coordinate coordinate, final List<PreparedGeometry> geometries) {
        final Point point = this.geometryCreatingService.createPoint(coordinate);
        return geometries.stream()
                .anyMatch(geometry -> geometry.contains(point));
    }

    private double calculateDistance(final TrackSlice trackSlice) {
        final Coordinate first = trackSlice.getFirst();
        final Coordinate second = trackSlice.getSecond();
        return this.calculatingDistanceService.calculate(first, second);
    }
}
