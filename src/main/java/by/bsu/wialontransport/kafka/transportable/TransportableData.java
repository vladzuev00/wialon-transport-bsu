package by.bsu.wialontransport.kafka.transportable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;
import lombok.experimental.FieldNameConstants;
import org.apache.avro.reflect.Nullable;

@Value
@Builder
@AllArgsConstructor
@FieldNameConstants
public class TransportableData implements Transportable<Long> {

    @Nullable
    Long id;

    long epochSeconds;
    int latitudeDegrees;
    int latitudeMinutes;
    int latitudeMinuteShare;
    char latitudeTypeValue;
    int longitudeDegrees;
    int longitudeMinutes;
    int longitudeMinuteShare;
    char longitudeTypeValue;
    int speed;
    int course;
    int altitude;
    int amountOfSatellites;
    double reductionPrecision;
    int inputs;
    int outputs;
    String serializedAnalogInputs;
    String driverKeyCode;
    String serializedParameters;
    Long trackerId;

    //TODO: test
    @Override
    public Long findKey() {
        return this.trackerId;
    }
}
