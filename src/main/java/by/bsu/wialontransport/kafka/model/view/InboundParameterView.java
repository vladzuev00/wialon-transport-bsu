package by.bsu.wialontransport.kafka.model.view;

import by.bsu.wialontransport.model.ParameterType;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

public final class InboundParameterView extends ParameterView {

    @Builder
    @JsonCreator
    public InboundParameterView(@JsonProperty("name") final String name,
                                @JsonProperty("type") final ParameterType type,
                                @JsonProperty("value") final String value) {
        super(name, type, value);
    }
}
