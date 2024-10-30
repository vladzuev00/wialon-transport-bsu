package by.bsu.wialontransport.protocol.wialon.model;

import by.bsu.wialontransport.crud.dto.Parameter;
import lombok.Value;

import java.time.LocalDateTime;
import java.util.Set;

@Value
public class WialonLocation {
    LocalDateTime dateTime;
    Double latitude;
    Double longitude;
    Integer course;
    Double speed;
    Integer altitude;
    Integer satelliteCount;
    Double hdop;
    Integer inputs;
    Integer outputs;
    double[] analogInputs;
    String driverKeyCode;
    Set<Parameter> parameters;
}
