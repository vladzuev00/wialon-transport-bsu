package by.bsu.wialontransport.crud.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

@Value
@AllArgsConstructor
@Builder
public class DataCalculations implements AbstractDto<Long> {
    Long id;
    double gpsOdometer;
    boolean ignitionOn;
    long engineOnDurationSeconds;
    Data data;
}
