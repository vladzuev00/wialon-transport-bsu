package by.bsu.wialontransport.kafka.transportable;

import lombok.*;
import lombok.experimental.FieldNameConstants;

@Getter
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@FieldNameConstants
public final class TransportableSavedData extends TransportableData {
    private final Long id;
    private final Long addressId;

    @Builder(builderMethodName = "savedDataBuilder")
    public TransportableSavedData(final Long id,
                                  final long epochSeconds,
                                  final double latitude,
                                  final double longitude,
                                  final int course,
                                  final int speed,
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
                epochSeconds,
                latitude,
                longitude,
                course,
                speed,
                altitude,
                amountOfSatellites,
                reductionPrecision,
                inputs,
                outputs,
                serializedAnalogInputs,
                driverKeyCode,
                serializedParameters,
                trackerId
        );
        this.id = id;
        this.addressId = addressId;
    }
}
