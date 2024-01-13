package by.bsu.wialontransport.protocol.wialon.model;

import by.bsu.wialontransport.crud.dto.Parameter;
import by.bsu.wialontransport.crud.dto.Tracker;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Value;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Map;

@Value
public class WialonData {
    LocalDate date;
    LocalTime time;
    double latitude;
    LatitudeType latitudeType;
    double longitude;
    LongitudeType longitudeType;
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

    @RequiredArgsConstructor
    @Getter
    public enum LatitudeType {
        NORTH('N'), SOUTH('S');

        private final char value;
    }

    @RequiredArgsConstructor
    @Getter
    public enum LongitudeType {
        EAST('E'), WESTERN('W');

        private final char value;
    }
}
