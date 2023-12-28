package by.bsu.wialontransport.kafka.producer.data.view;

import by.bsu.wialontransport.crud.entity.ParameterEntity.Type;
import lombok.Builder;

public final class InboundParameterView extends ParameterView {

    @Builder
    public InboundParameterView(final String name, final Type type, final String value) {
        super(name, type, value);
    }
}
