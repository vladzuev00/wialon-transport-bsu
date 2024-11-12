package by.bsu.wialontransport.protocol.newwing.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

import java.time.LocalDate;
import java.time.LocalTime;

@Value
@Builder
@AllArgsConstructor
public class NewWingLocation {
    LocalDate date;
    LocalTime time;
    double latitude;
    double longitude;
    short course;
    double speed;
    double hdop;
    double[] analogInputs;
}
