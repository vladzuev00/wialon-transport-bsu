package by.bsu.wialontransport.model;

import lombok.Value;

@Value
public class AreaCoordinate {
    GpsCoordinate leftBottom;
    GpsCoordinate rightUpper;
}
