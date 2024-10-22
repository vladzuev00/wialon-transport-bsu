package by.bsu.wialontransport.protocol.jt808.model;

import lombok.Value;

import java.time.Instant;

@Value
public class Location {
    Instant dateTime;
    float latitude;
    float longitude;
    short altitude;
    short speed;
    short course;
}
