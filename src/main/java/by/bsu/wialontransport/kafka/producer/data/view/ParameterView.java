package by.bsu.wialontransport.kafka.producer.data.view;

import by.bsu.wialontransport.crud.entity.ParameterEntity.Type;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public abstract class ParameterView {
    private final String name;
    private final Type type;
    private final String value;
}
