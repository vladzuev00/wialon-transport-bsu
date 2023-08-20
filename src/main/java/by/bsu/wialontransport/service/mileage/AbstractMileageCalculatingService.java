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
                                                                 final List<PreparedGeometry> cityGeometries,
                                                                 final GeometryCreatingService geometryCreatingService);

    private Map<Boolean, Double> findMileagesByLocatedInCity(final Track track) {
        final List<PreparedGeometry> intersectedCityGeometries = this.findCityGeometriesIntersectedBySimplifiedTrack(track);
        return this.createTrackSliceStream(track, intersectedCityGeometries, this.geometryCreatingService)
                .collect(
                        partitioningBy(
                                TrackSlice::isLocatedInCity,
                                summingDouble(this::calculateDistance)
                        )
                );
    }

    private List<PreparedGeometry> findCityGeometriesIntersectedBySimplifiedTrack(final Track track) {
        final LineString lineString = this.createLineStringBySimplifiedTrack(track);
        return this.addressService.findCitiesPreparedGeometriesIntersectedByLineString(lineString);
    }

    private LineString createLineStringBySimplifiedTrack(final Track track) {
        final Track simplifiedTrack = this.simplifyingTrackService.simplify(track);
        return this.geometryCreatingService.createLineString(simplifiedTrack);
    }

    private static boolean isAnyGeometryContainCoordinate(final Coordinate coordinate,
                                                          final List<PreparedGeometry> cityGeometries,
                                                          final GeometryCreatingService geometryCreatingService) {
        final Point point = geometryCreatingService.createPoint(coordinate);
        return cityGeometries.stream()
                .anyMatch(geometry -> geometry.contains(point));
    }

    private double calculateDistance(final TrackSlice trackSlice) {
        final Coordinate first = trackSlice.getFirst();
        final Coordinate second = trackSlice.getSecond();
        return this.calculatingDistanceService.calculate(first, second);
    }
}
