package by.bsu.wialontransport.service.mileage.model;

import by.bsu.wialontransport.model.Coordinate;
import lombok.Value;

@Value
public class TrackSlice {
    Coordinate first;
    Coordinate second;
    boolean locatedInCity;
}
