package by.bsu.wialontransport.kafka.transportable.data;

import by.bsu.wialontransport.kafka.transportable.Transportable;
import lombok.*;
import lombok.experimental.FieldNameConstants;

@AllArgsConstructor
@Getter
@EqualsAndHashCode
@ToString
@FieldNameConstants
public abstract class TransportableData implements Transportable<Long> {
    private final long epochSeconds;
    private final double latitude;
    private final double longitude;
    private final int course;
    private final int speed;
    private final int altitude;
    private final int amountOfSatellites;
    private final double reductionPrecision;
    private final int inputs;
    private final int outputs;
    private final String serializedAnalogInputs;
    private final String driverKeyCode;
    private final String serializedParameters;
    private final Long trackerId;

    @Override
    public final Long findTopicKey() {
        return trackerId;
    }
}
