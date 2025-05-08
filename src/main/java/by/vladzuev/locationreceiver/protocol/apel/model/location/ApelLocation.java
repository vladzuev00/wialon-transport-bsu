package by.vladzuev.locationreceiver.protocol.apel.model.location;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
@AllArgsConstructor
public class ApelLocation {
    int epochSeconds;
    int latitude;
    int longitude;
    short speed;
    Double hdop;
    short course;
    short altitude;
    Byte satelliteCount;
    double[] analogInputs;
}
