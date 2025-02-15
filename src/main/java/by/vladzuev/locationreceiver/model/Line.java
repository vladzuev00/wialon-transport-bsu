package by.vladzuev.locationreceiver.model;

import lombok.Value;

@Value
public class Line {
    CoordinateRequest firstCoordinate;
    CoordinateRequest secondCoordinate;
}
