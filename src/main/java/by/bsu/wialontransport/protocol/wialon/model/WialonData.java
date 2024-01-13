package by.bsu.wialontransport.protocol.wialon.model;

import by.bsu.wialontransport.crud.dto.Parameter;
import by.bsu.wialontransport.crud.dto.Tracker;
import by.bsu.wialontransport.protocol.wialon.model.coordinate.Latitude;
import by.bsu.wialontransport.protocol.wialon.model.coordinate.Longitude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Set;

@Value
@AllArgsConstructor
@Builder
public class WialonData {
    LocalDate date;
    LocalTime time;
    Latitude latitude;
    Longitude longitude;
    Integer course;
    Double speed;
    Integer altitude;
    Integer amountOfSatellites;
    Double hdop;
    Integer inputs;
    Integer outputs;
    double[] analogInputs;
    String driverKeyCode;
    Set<Parameter> parameters;
    Tracker tracker;
}
