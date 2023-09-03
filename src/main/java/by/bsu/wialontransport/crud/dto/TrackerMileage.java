package by.bsu.wialontransport.crud.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

@Value
@AllArgsConstructor
@Builder
public class TrackerMileage implements AbstractDto<Long> {
    Long id;
    double urban;
    double country;
}
