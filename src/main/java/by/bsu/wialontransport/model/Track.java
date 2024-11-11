package by.bsu.wialontransport.model;

import by.bsu.wialontransport.crud.dto.Tracker;
import lombok.Value;

import java.util.List;

@Value
public class Track {
    Tracker tracker;
    List<GpsCoordinate> coordinates;
}
