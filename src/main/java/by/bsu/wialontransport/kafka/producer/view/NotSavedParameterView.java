package by.bsu.wialontransport.kafka.producer.view;

import by.bsu.wialontransport.crud.entity.ParameterEntity.Type;

public final class NotSavedParameterView extends ParameterView {

    public NotSavedParameterView(final String name, final Type type, final String value) {
        super(name, type, value);
    }
}
