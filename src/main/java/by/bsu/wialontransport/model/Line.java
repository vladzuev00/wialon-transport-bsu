package by.bsu.wialontransport.model;

import lombok.Value;

@Value
public class Line {
    CoordinateRequest firstCoordinate;
    CoordinateRequest secondCoordinate;
}
