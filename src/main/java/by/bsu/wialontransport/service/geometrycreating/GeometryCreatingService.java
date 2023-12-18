package by.bsu.wialontransport.service.geometrycreating;

import by.bsu.wialontransport.model.RequestCoordinate;
import by.bsu.wialontransport.model.Track;
import by.bsu.wialontransport.util.CoordinateUtil;
import lombok.RequiredArgsConstructor;
import org.locationtech.jts.geom.CoordinateSequence;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.LineString;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.impl.CoordinateArraySequence;
import org.springframework.stereotype.Service;

import java.util.function.Supplier;
import java.util.stream.Stream;

import static by.bsu.wialontransport.util.CoordinateUtil.mapToJtsCoordinate;

//TODO: refactor tests
@Service
@RequiredArgsConstructor
public final class GeometryCreatingService {
    private final GeometryFactory geometryFactory;

    public LineString createLineString(final Track track) {
        return this.createLineString(
                () -> mapToCoordinateSequence(track)
        );
    }

    public LineString createLineString(final RequestCoordinate first, final RequestCoordinate second) {
        return this.createLineString(
                () -> mapToCoordinateSequence(first, second)
        );
    }

    public Point createPoint(final RequestCoordinate coordinate) {
        final org.locationtech.jts.geom.Coordinate jtsCoordinate = mapToJtsCoordinate(coordinate);
        return this.geometryFactory.createPoint(jtsCoordinate);
    }

    private LineString createLineString(final Supplier<CoordinateSequence> coordinateSequenceSupplier) {
        final CoordinateSequence coordinateSequence = coordinateSequenceSupplier.get();
        return new LineString(coordinateSequence, this.geometryFactory);
    }

    private static CoordinateSequence mapToCoordinateSequence(final Track track) {
        return createCoordinateSequence(
                () -> mapToJtsCoordinates(track)
        );
    }

    private static CoordinateSequence mapToCoordinateSequence(final RequestCoordinate firstCoordinate,
                                                              final RequestCoordinate secondCoordinate) {
        return createCoordinateSequence(
                () -> mapToJtsCoordinates(firstCoordinate, secondCoordinate)
        );
    }

    private static CoordinateSequence createCoordinateSequence(final Supplier<org.locationtech.jts.geom.Coordinate[]> coordinateSupplier) {
        final org.locationtech.jts.geom.Coordinate[] jtsCoordinates = coordinateSupplier.get();
        return new CoordinateArraySequence(jtsCoordinates);
    }

    private static org.locationtech.jts.geom.Coordinate[] mapToJtsCoordinates(final Track track) {
        return mapToJtsCoordinates(
                track::findCoordinateStream
        );
    }

    private static org.locationtech.jts.geom.Coordinate[] mapToJtsCoordinates(final RequestCoordinate firstCoordinate,
                                                                              final RequestCoordinate secondCoordinate) {
        return mapToJtsCoordinates(
                () -> Stream.of(firstCoordinate, secondCoordinate)
        );
    }

    private static org.locationtech.jts.geom.Coordinate[] mapToJtsCoordinates(final Supplier<Stream<RequestCoordinate>> coordinateStreamSupplier) {
        final Stream<RequestCoordinate> coordinateStream = coordinateStreamSupplier.get();
        return mapToJtsCoordinates(coordinateStream);
    }

    private static org.locationtech.jts.geom.Coordinate[] mapToJtsCoordinates(final Stream<RequestCoordinate> coordinateStream) {
        return coordinateStream
                .map(CoordinateUtil::mapToJtsCoordinate)
                .toArray(org.locationtech.jts.geom.Coordinate[]::new);
    }
}
