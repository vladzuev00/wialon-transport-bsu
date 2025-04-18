package by.vladzuev.locationreceiver.crud.dto;

import by.vladzuev.locationreceiver.crud.enumeration.ParameterType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
@AllArgsConstructor
public class Parameter implements Dto<Long> {
    Long id;
    String name;
    ParameterType type;
    String value;
    Location location;
}
