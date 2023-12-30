package by.bsu.wialontransport.crud.dto;

import by.bsu.wialontransport.crud.entity.ParameterEntity.Type;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

import static by.bsu.wialontransport.crud.entity.ParameterEntity.Type.DOUBLE;

@Value
@AllArgsConstructor
@Builder
public class Parameter implements Dto<Long> {
    Long id;
    String name;
    Type type;
    String value;
    Data data;

    public static Parameter createDoubleParameter(final String name, final double value) {
        return Parameter.builder()
                .name(name)
                .type(DOUBLE)
                .value(Double.toString(value))
                .build();
    }
}
