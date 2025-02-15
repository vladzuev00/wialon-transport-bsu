package by.vladzuev.locationreceiver.crud.dto;

import by.vladzuev.locationreceiver.model.GpsCoordinate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

import java.time.LocalDateTime;
import java.util.Map;

@Value
@AllArgsConstructor
@Builder
public class Location implements Dto<Long> {
    Long id;
    LocalDateTime dateTime;
    GpsCoordinate coordinate;
    int course;
    double speed;
    int altitude;
    int satelliteCount;
    double hdop;
    int inputs;
    int outputs;
    double[] analogInputs;
    String driverKeyCode;

    /**
     * parameter's name to parameter
     */
    Map<String, Parameter> parametersByNames;
    Tracker tracker;
    Address address;

    public String findCityName() {
        return address.getCityName();
    }

    public String findCountryName() {
        return address.getCountryName();
    }
}
