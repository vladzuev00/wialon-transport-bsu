package by.bsu.wialontransport.model;

import lombok.Value;

import java.util.List;

@Value
public class Track {
    List<Coordinate> coordinates;
}
