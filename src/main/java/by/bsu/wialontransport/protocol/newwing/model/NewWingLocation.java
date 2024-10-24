package by.bsu.wialontransport.protocol.newwing.model;

import lombok.Value;

import java.time.LocalDateTime;

@Value
public class NewWingLocation {
    LocalDateTime dateTime;
    double latitude;
    double longitude;
    int speed;
    double hdop;
    double[] analogInputs;
}
