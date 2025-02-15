package by.vladzuev.locationreceiver.model.online;

import by.vladzuev.locationreceiver.model.GpsCoordinate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

import java.time.LocalDateTime;

//TODO: delete tests
@Value
@AllArgsConstructor
@Builder
public class TrackerOnline {
    Long trackerId;
    LastData lastData;
    OnlineStatus status;

    @Value
    public static class LastData {
        LocalDateTime dateTime;
        GpsCoordinate coordinate;
    }
}
