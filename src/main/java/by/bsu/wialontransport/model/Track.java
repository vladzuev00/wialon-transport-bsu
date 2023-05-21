package by.bsu.wialontransport.model;

import lombok.Value;

import java.util.List;

import static java.util.Arrays.asList;

@Value
public class Track {
    List<Coordinate> coordinates;

    public static Track create(final Coordinate... coordinates) {
        return new Track(asList(coordinates));
    }
}
