package by.vladzuev.locationreceiver.crud.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
@AllArgsConstructor
public class City implements Dto<Long> {
    Long id;
    Address address;
}
