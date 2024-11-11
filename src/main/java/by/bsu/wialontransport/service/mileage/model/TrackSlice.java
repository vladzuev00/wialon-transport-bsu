package by.bsu.wialontransport.service.mileage.model;

import by.bsu.wialontransport.model.GpsCoordinate;
import lombok.Value;

@Value
public class TrackSlice {
    GpsCoordinate first;
    GpsCoordinate second;
    boolean locatedInCity;
}
