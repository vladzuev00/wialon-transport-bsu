package by.vladzuev.locationreceiver.service.mileage.model;

import by.vladzuev.locationreceiver.model.GpsCoordinate;
import lombok.Value;

@Value
public class TrackSlice {
    GpsCoordinate first;
    GpsCoordinate second;
    boolean locatedInCity;
}
