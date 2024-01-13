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
    int course;
    double speed;
    int altitude;
    int amountOfSatellites;
    double hdop;
    int inputs;
    int outputs;
    double[] analogInputs;
    String driverKeyCode;
    Set<Parameter> parameters;
    Tracker tracker;
}
