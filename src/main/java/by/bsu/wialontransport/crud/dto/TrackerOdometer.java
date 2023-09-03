package by.bsu.wialontransport.crud.dto;

import lombok.Value;

@Value
public class TrackerOdometer implements AbstractDto<Long> {
    Long id;
    double urban;
    double country;
}
