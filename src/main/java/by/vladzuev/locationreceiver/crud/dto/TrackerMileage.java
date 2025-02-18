package by.vladzuev.locationreceiver.crud.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
@AllArgsConstructor
public class TrackerMileage implements Dto<Long> {
    Long id;
    double urban;
    double country;
}
