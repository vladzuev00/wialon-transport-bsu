package by.bsu.wialontransport.kafka.transportable;

import lombok.*;
import lombok.experimental.FieldNameConstants;

@Getter
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@FieldNameConstants
public final class TransportableSavedData extends TransportableData {
    private final Long addressId;

    @Builder(builderMethodName = "savedDataBuilder")
    public TransportableSavedData(final Long id,
                                  final long epochSeconds,
                                  final int latitudeDegrees,
                                  final int latitudeMinutes,
                                  final int latitudeMinuteShare,
                                  final char latitudeTypeValue,
                                  final int longitudeDegrees,
                                  final int longitudeMinutes,
                                  final int longitudeMinuteShare,
                                  final char longitudeTypeValue,
                                  final int speed,
                                  final int course,
                                  final int altitude,
                                  final int amountOfSatellites,
                                  final double reductionPrecision,
                                  final int inputs,
                                  final int outputs,
                                  final String serializedAnalogInputs,
                                  final String driverKeyCode,
                                  final String serializedParameters,
                                  final Long trackerId,
                                  final Long addressId) {
        super(
                id, epochSeconds,
                latitudeDegrees, latitudeMinutes, latitudeMinuteShare, latitudeTypeValue,
                longitudeDegrees, longitudeMinutes, longitudeMinuteShare, longitudeTypeValue,
                speed, course, altitude, amountOfSatellites, reductionPrecision, inputs, outputs,
                serializedAnalogInputs, driverKeyCode, serializedParameters, trackerId
        );
        this.addressId = addressId;
    }
}
