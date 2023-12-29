package by.bsu.wialontransport.kafka.model.view;

import by.bsu.wialontransport.crud.entity.ParameterEntity.Type;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

public final class InboundParameterView extends ParameterView {

    @Builder
    @JsonCreator
    public InboundParameterView(@JsonProperty("name") final String name,
                                @JsonProperty("type") final Type type,
                                @JsonProperty("value") final String value) {
        super(name, type, value);
    }
}
