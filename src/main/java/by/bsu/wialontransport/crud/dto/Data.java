package by.bsu.wialontransport.crud.dto;

import by.bsu.wialontransport.model.Coordinate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

import java.time.LocalDateTime;
import java.util.Map;

@Value
@AllArgsConstructor
@Builder
public class Data implements Dto<Long> {
    Long id;
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
