package by.bsu.wialontransport.kafka.transportable;

import lombok.Value;
import lombok.experimental.FieldNameConstants;
import org.apache.avro.reflect.Nullable;

@Value
@FieldNameConstants
public class TransportableData {

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
}
