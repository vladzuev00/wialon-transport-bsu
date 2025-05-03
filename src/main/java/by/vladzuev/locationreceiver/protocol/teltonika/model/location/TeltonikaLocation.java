package by.vladzuev.locationreceiver.protocol.teltonika.model.location;

import lombok.Value;

import java.time.LocalDateTime;

@Value
public class TeltonikaLocation {
    LocalDateTime dateTime;
    float latitude;
    float longitude;
    short altitude;
    short angle;
    byte satelliteCount;
    short speed;
}
