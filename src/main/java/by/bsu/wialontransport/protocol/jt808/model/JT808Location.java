package by.bsu.wialontransport.protocol.jt808.model;

import lombok.Value;

import java.time.Instant;

@Value
public class JT808Location {
    Instant dateTime;
    double latitude;
    double longitude;
    short altitude;
    short speed;
    short course;
}
