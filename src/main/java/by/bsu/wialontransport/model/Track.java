package by.bsu.wialontransport.model;

import lombok.Value;

import java.util.List;
import java.util.stream.Stream;

import static java.util.Arrays.asList;

@Value
public class Track {
    List<RequestCoordinate> coordinates;

    //TODO: test
    public Stream<RequestCoordinate> findCoordinateStream() {
        return this.coordinates.stream();
    }

    public static Track create(final RequestCoordinate... coordinates) {
        return new Track(asList(coordinates));
    }
}
