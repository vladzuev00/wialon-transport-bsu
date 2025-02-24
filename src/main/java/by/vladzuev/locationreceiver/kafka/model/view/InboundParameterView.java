package by.vladzuev.locationreceiver.kafka.model.view;

import by.vladzuev.locationreceiver.crud.enumeration.ParameterType;
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
