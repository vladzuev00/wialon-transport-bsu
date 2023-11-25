package by.bsu.wialontransport.model;

import by.bsu.wialontransport.crud.dto.Parameter;
import by.bsu.wialontransport.crud.dto.Tracker;
import lombok.Value;

import java.time.LocalDateTime;
import java.util.Map;

@Value
public class ReceivedData {
    LocalDateTime dateTime;
    Coordinate coordinate;
    int course;
    double speed;
    int altitude;
    int amountOfSatellites;
    double reductionPrecision;
    int inputs;
    int outputs;
    double[] analogInputs;
    String driverKeyCode;
    Map<String, Parameter> parametersByNames;
    Tracker tracker;
}
