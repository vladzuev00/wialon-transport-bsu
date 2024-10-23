package by.bsu.wialontransport.protocol.jt808.model;

import lombok.Value;

import java.time.LocalDateTime;

@Value
public class JT808Location {
    LocalDateTime dateTime;
    double latitude;
    double longitude;
    short altitude;
    short speed;
    short course;
}
