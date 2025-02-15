package by.vladzuev.locationreceiver.model;

import lombok.Value;

@Value
public class AreaCoordinate {
    GpsCoordinate leftBottom;
    GpsCoordinate rightUpper;
}
