package by.bsu.wialontransport.kafka.model.view;

import by.bsu.wialontransport.crud.entity.ParameterEntity.Type;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public final class SavedParameterView extends ParameterView {
    private final Long id;

    @Builder
    @JsonCreator
    public SavedParameterView(@JsonProperty("name") final String name,
                              @JsonProperty("type") final Type type,
                              @JsonProperty("value") final String value,
                              @JsonProperty("id") final Long id) {
        super(name, type, value);
        this.id = id;
    }
}