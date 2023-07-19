package by.bsu.wialontransport.service.geometrycreating;

import by.bsu.wialontransport.model.Coordinate;
import by.bsu.wialontransport.model.Track;
import lombok.RequiredArgsConstructor;
import org.locationtech.jts.geom.CoordinateSequence;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.LineString;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.impl.CoordinateArraySequence;
import org.springframework.stereotype.Service;

//TODO: test
@Service
@RequiredArgsConstructor
public final class GeometryCreatingService {
    private final GeometryFactory geometryFactory;

    public LineString createLineString(final Track track) {
        final CoordinateSequence coordinateSequence = mapToCoordinateSequence(track);
        return new LineString(coordinateSequence, this.geometryFactory);
    }

    public Point createPoint(final Coordinate coordinate) {
        final org.locationtech.jts.geom.Coordinate jtsCoordinate = mapToJtsCoordinate(coordinate);
        return this.geometryFactory.createPoint(jtsCoordinate);
    }

    private static CoordinateSequence mapToCoordinateSequence(final Track track) {
        final org.locationtech.jts.geom.Coordinate[] jtsCoordinates = mapToJtsCoordinates(track);
        return new CoordinateArraySequence(jtsCoordinates);
    }

    private static org.locationtech.jts.geom.Coordinate[] mapToJtsCoordinates(final Track track) {
        return track.getCoordinates()
                .stream()
                .map(GeometryCreatingService::mapToJtsCoordinate)
                .toArray(org.locationtech.jts.geom.Coordinate[]::new);
    }

    private static org.locationtech.jts.geom.Coordinate mapToJtsCoordinate(Coordinate mapped) {
        return new org.locationtech.jts.geom.Coordinate(mapped.getLongitude(), mapped.getLatitude());
    }
}
