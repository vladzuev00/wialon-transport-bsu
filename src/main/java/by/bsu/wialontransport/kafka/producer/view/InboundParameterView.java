package by.bsu.wialontransport.kafka.producer.view;

import by.bsu.wialontransport.crud.entity.ParameterEntity.Type;

public final class InboundarameterView extends ParameterView {

    public InboundParameterView(final String name, final Type type, final String value) {
        super(name, type, value);
    }
}
