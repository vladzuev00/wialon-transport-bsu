package by.bsu.wialontransport.kafka.transportable;

import lombok.*;
import lombok.experimental.FieldNameConstants;
import org.apache.avro.reflect.Nullable;

@AllArgsConstructor
@Getter
@EqualsAndHashCode
@ToString
@Builder
@FieldNameConstants
public class TransportableData implements Transportable<Long> {

    @Nullable
    private final Long id;

    private final long epochSeconds;
    private final int latitudeDegrees;
    private final int latitudeMinutes;
    private final int latitudeMinuteShare;
    private final char latitudeTypeValue;
    private final int longitudeDegrees;
    private final int longitudeMinutes;
    private final int longitudeMinuteShare;
    private final char longitudeTypeValue;
    private final int speed;
    private final int course;
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
    public final Long findKey() {
        return this.trackerId;
    }
}
