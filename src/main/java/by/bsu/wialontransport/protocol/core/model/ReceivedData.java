package by.bsu.wialontransport.protocol.core.model;

import by.bsu.wialontransport.crud.dto.Parameter;
import by.bsu.wialontransport.crud.dto.Tracker;
import by.bsu.wialontransport.model.Coordinate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

import java.time.LocalDateTime;
import java.util.Map;

@Value
@AllArgsConstructor
@Builder
public class ReceivedData {
    LocalDateTime dateTime;
    Coordinate coordinate;
    int course;
    double speed;
    int altitude;
    int amountOfSatellites;
    double hdop;
    int inputs;
    int outputs;
    double[] analogInputs;
    String driverKeyCode;
    Map<String, Parameter> parametersByNames;
    Tracker tracker;
}
