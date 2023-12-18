package by.bsu.wialontransport.model;

import lombok.Value;

@Value
public class Line {
    RequestCoordinate firstCoordinate;
    RequestCoordinate secondCoordinate;
}
