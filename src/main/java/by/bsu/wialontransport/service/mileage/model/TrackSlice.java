package by.bsu.wialontransport.service.mileage.model;

import by.bsu.wialontransport.model.RequestCoordinate;
import lombok.Value;

@Value
public class TrackSlice {
    RequestCoordinate first;
    RequestCoordinate second;
    boolean locatedInCity;
}
