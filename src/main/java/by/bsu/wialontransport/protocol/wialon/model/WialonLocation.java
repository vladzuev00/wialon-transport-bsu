package by.bsu.wialontransport.protocol.wialon.model;

import by.bsu.wialontransport.crud.dto.Parameter;
import lombok.Value;

import java.time.LocalDateTime;
import java.util.Set;

@Value
public class WialonLocation {
    LocalDateTime dateTime;
    double latitude;
    double longitude;
    int course;
    double speed;
    int altitude;
    int satelliteCount;
    double hdop;
    int inputs;
    int outputs;
    double[] analogInputs;
    String driverKeyCode;
    Set<Parameter> parameters;
}
