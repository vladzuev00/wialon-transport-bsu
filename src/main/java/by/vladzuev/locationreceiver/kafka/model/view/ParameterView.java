package by.vladzuev.locationreceiver.kafka.model.view;

import by.vladzuev.locationreceiver.crud.enumeration.ParameterType;
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
