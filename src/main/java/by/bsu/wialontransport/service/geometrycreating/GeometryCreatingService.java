package by.bsu.wialontransport.service.geometrycreating;

import by.bsu.wialontransport.model.GpsCoordinate;
import lombok.RequiredArgsConstructor;
import org.locationtech.jts.geom.CoordinateXY;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.LineString;
import org.locationtech.jts.geom.Point;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public final class GeometryCreatingService {
    private final GeometryFactory geometryFactory;

    public LineString createLineString(final List<GpsCoordinate> coordinates) {
        return geometryFactory.createLineString(mapToJtsCoordinates(coordinates));
    }

    public Point createPoint(final GpsCoordinate coordinate) {
        return geometryFactory.createPoint(mapToJtsCoordinate(coordinate));
    }

    private static CoordinateXY[] mapToJtsCoordinates(final List<GpsCoordinate> coordinates) {
        return coordinates.stream()
                .map(GeometryCreatingService::mapToJtsCoordinate)
                .toArray(CoordinateXY[]::new);
    }

    private static CoordinateXY mapToJtsCoordinate(final GpsCoordinate coordinate) {
        return new CoordinateXY(coordinate.getLongitude(), coordinate.getLatitude());
    }
}
