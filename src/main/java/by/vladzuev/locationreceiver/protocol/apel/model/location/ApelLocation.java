package by.vladzuev.locationreceiver.protocol.apel.model.location;

import lombok.Value;

@Value
public class ApelLocation {
    int epochSeconds;
    int latitude;
    int longitude;
    short speed;
    short course;
    short altitude;
}
