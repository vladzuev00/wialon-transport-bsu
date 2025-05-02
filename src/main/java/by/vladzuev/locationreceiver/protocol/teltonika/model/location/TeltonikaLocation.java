package by.vladzuev.locationreceiver.protocol.teltonika.model.location;

import lombok.Value;

@Value
public class TeltonikaLocation {
    float latitude;
    float longitude;
    short altitude;
    short angle;
    byte satelliteCount;
    short speed;
}
