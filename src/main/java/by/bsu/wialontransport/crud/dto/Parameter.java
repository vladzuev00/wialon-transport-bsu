package by.bsu.wialontransport.crud.dto;

import by.bsu.wialontransport.crud.entity.ParameterEntity.Type;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

@Value
@AllArgsConstructor
@Builder
public class Parameter implements Dto<Long> {
    Long id;
    String name;
    Type type;
    String value;
    Data data;
}
