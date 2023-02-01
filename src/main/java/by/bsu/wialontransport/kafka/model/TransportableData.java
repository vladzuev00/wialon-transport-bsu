package by.bsu.wialontransport.kafka.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;
import org.apache.avro.reflect.Nullable;

@Value
@AllArgsConstructor
@Builder
public class TransportableData {
    //fields from Data:
    @Nullable
    Long id;

    long dateTimeEpochSecond;
    double latitude;
    double longitude;
    int speed;
    int course;
    int altitude;
    int amountOfSatellites;
    double reductionPrecision;
    int inputs;
    int outputs;
    double[] analogInputs;
    String driverKeyCode;

    //parameters
    double gsmLevel;
    double voltage;
    double cornerAcceleration;
    double accelerationUp;
    double accelerationDown;
    double gpsOdometer;

    //additional calculations
    int ignition;
    long engineTime;
    double shock;
    Long deviceId;
}
