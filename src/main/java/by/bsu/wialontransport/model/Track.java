package by.bsu.wialontransport.model;

import lombok.Value;

import java.util.List;
import java.util.stream.Stream;

import static java.util.Arrays.asList;

@Value
public class Track {
    List<Coordinate> coordinates;

    //TODO: test
    public Stream<Coordinate> findCoordinateStream() {
        return this.coordinates.stream();
    }

    public static Track create(final Coordinate... coordinates) {
        return new Track(asList(coordinates));
    }
}
