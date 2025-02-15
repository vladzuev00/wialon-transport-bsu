package by.vladzuev.locationreceiver.model;

import by.vladzuev.locationreceiver.crud.dto.Tracker;
import lombok.Value;

import java.util.List;

@Value
public class Track {
    Tracker tracker;
    List<GpsCoordinate> coordinates;
}
