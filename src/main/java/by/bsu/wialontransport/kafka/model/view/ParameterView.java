package by.bsu.wialontransport.kafka.model.view;

import by.bsu.wialontransport.model.ParameterType;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@RequiredArgsConstructor
@Getter
@EqualsAndHashCode
@ToString
public abstract class ParameterView {
    private final String name;
    private final ParameterType type;
    private final String value;
}
