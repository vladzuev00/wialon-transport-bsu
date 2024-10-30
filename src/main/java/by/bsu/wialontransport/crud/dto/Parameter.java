package by.bsu.wialontransport.crud.dto;

import by.bsu.wialontransport.model.ParameterType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

@Value
@AllArgsConstructor
@Builder
public class Parameter implements Dto<Long> {
    Long id;
    String name;
    ParameterType type;
    String value;
}
