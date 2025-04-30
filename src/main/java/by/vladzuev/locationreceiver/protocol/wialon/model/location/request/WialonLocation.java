package by.vladzuev.locationreceiver.protocol.wialon.model.location.request;

import by.vladzuev.locationreceiver.crud.dto.Parameter;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Set;

@Value
@Builder
@AllArgsConstructor
public class WialonLocation {
    LocalDate date;
    LocalTime time;
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
