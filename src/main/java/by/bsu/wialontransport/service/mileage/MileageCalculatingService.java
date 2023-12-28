package by.bsu.wialontransport.service.mileage;

import by.bsu.wialontransport.crud.service.AddressService;
import by.bsu.wialontransport.model.RequestCoordinate;
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

import static by.bsu.wialontransport.model.Track.create;
import static java.util.stream.Collectors.partitioningBy;
import static java.util.stream.Collectors.summingDouble;

@RequiredArgsConstructor
public abstract class MileageCalculatingService {
    private final SimplifyingTrackService simplifyingTrackService;
    private final GeometryCreatingService geometryCreatingService;
    private final CalculatingDistanceService calculatingDistanceService;
    private final AddressService addressService;

    //TODO: test
    public final Mileage calculate(final RequestCoordinate firstCoordinate, final RequestCoordinate secondCoordinate) {
        final Track track = create(firstCoordinate, secondCoordinate);
        return this.calculate(track);
    }

    public final Mileage calculate(final Track track) {
        final Map<Boolean, Double> mileagesByLocatedInCity = this.findMileagesByLocatedInCity(track);
        final double urban = mileagesByLocatedInCity.get(true);
        final double country = mileagesByLocatedInCity.get(false);
        return new Mileage(urban, country);
    }

    protected abstract Stream<TrackSlice> createTrackSliceStream(final Track track,
                                                                 //TODO: replace by List<Geometry>
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
//        return this.addressService.findCitiesPreparedGeometriesIntersectedByLineString(lineString);
        return null;
    }

    private LineString createLineStringBySimplifiedTrack(final Track track) {
        final Track simplifiedTrack = this.simplifyingTrackService.simplify(track);
        return this.geometryCreatingService.createLineString(simplifiedTrack);
    }

    private static boolean isAnyGeometryContainCoordinate(final RequestCoordinate coordinate,
                                                          final List<PreparedGeometry> cityGeometries,
                                                          final GeometryCreatingService geometryCreatingService) {
        final Point point = geometryCreatingService.createPoint(coordinate);
        return cityGeometries.stream()
                .anyMatch(geometry -> geometry.contains(point));
    }

    private double calculateDistance(final TrackSlice trackSlice) {
        final RequestCoordinate first = trackSlice.getFirst();
        final RequestCoordinate second = trackSlice.getSecond();
        return this.calculatingDistanceService.calculate(first, second);
    }
}
