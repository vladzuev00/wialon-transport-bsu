package by.vladzuev.locationreceiver.model;

import lombok.Value;

import java.util.List;
import java.util.stream.Stream;

import static java.util.Arrays.asList;

@Value
public class TempTrack {
    List<CoordinateRequest> coordinates;

    //TODO: test
    public Stream<CoordinateRequest> findCoordinateStream() {
        return this.coordinates.stream();
    }

    public static TempTrack create(final CoordinateRequest... coordinates) {
        return new TempTrack(asList(coordinates));
    }
}
