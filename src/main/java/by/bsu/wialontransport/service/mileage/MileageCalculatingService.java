package by.bsu.wialontransport.service.mileage;

import by.bsu.wialontransport.crud.service.CityService;
import by.bsu.wialontransport.model.Coordinate;
import by.bsu.wialontransport.model.Mileage;
import by.bsu.wialontransport.model.Track;
import by.bsu.wialontransport.service.calculatingdistance.CalculatingDistanceService;
import by.bsu.wialontransport.service.geometrycreating.GeometryCreatingService;
import by.bsu.wialontransport.service.simplifyingtrack.SimplifyingTrackService;
import lombok.RequiredArgsConstructor;
import lombok.Value;
import org.locationtech.jts.geom.LineString;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.prep.PreparedGeometry;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

import static java.util.stream.Collectors.partitioningBy;
import static java.util.stream.Collectors.summingDouble;
import static java.util.stream.IntStream.rangeClosed;

//TODO: test
@Service
@RequiredArgsConstructor
public final class MileageCalculatingService {
    private final SimplifyingTrackService simplifyingTrackService;
    private final GeometryCreatingService geometryCreatingService;
    private final CalculatingDistanceService calculatingDistanceService;
    private final CityService cityService;

    public Mileage calculate(final Track track) {
        final Map<Boolean, Double> mileagesByLocatedInCity = this.findMileagesByLocatedInCity(track);
        final double urban = mileagesByLocatedInCity.get(true);
        final double country = mileagesByLocatedInCity.get(false);
        return new Mileage(urban, country);
    }

    private Map<Boolean, Double> findMileagesByLocatedInCity(final Track track) {
        final List<PreparedGeometry> intersectedCitiesGeometries = this.findCitiesGeometriesIntersectedBySimplifiedTrack(
                track
        );
        final List<Coordinate> trackCoordinates = track.getCoordinates();
        final int indexPenultimateCoordinate = trackCoordinates.size() - 2;
        return rangeClosed(0, indexPenultimateCoordinate)
                .mapToObj(i -> new TrackSlice(
                        trackCoordinates.get(i),
                        trackCoordinates.get(i + 1),
                        //slices, which is located in city, must have second coordinate, which is located in city
                        this.isAnyGeometryContainCoordinate(trackCoordinates.get(i + 1), intersectedCitiesGeometries)
                ))
                .collect(
                        partitioningBy(
                                TrackSlice::isLocatedInCity,
                                summingDouble(this::calculateDistance)
                        )
                );
    }

    private List<PreparedGeometry> findCitiesGeometriesIntersectedBySimplifiedTrack(final Track track) {
        final LineString lineString = this.createLineStringBySimplifiedTrack(track);
        return this.cityService.findPreparedGeometriesIntersectedByLineString(lineString);
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

    @Value
    private static class TrackSlice {
        Coordinate first;
        Coordinate second;
        boolean locatedInCity;
    }
}
