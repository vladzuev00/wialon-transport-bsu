package by.vladzuev.locationreceiver.protocol.jt808.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

import java.time.LocalDateTime;

@Value
@Builder
@AllArgsConstructor
public class JT808Location {
    LocalDateTime dateTime;
    double latitude;
    double longitude;
    short altitude;
    short speed;
    short course;
}
