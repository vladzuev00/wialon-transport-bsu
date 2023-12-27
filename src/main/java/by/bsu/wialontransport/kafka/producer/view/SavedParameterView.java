package by.bsu.wialontransport.kafka.producer.view;

import by.bsu.wialontransport.crud.entity.ParameterEntity.Type;
import lombok.Getter;

@Getter
public final class SavedParameterView extends ParameterView {
    private final Long id;

    public SavedParameterView(final String name, final Type type, final String value, final Long id) {
        super(name, type, value);
        this.id = id;
    }
}
