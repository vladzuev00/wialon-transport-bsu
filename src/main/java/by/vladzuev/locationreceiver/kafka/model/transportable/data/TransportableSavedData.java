package by.vladzuev.locationreceiver.kafka.model.transportable.data;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import lombok.experimental.FieldNameConstants;

@Getter
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@FieldNameConstants
public final class TransportableSavedData extends TransportableData {
    private final Long id;
    private final Long addressId;

    @Builder
    public TransportableSavedData(final Long id,
                                  final long epochSeconds,
                                  final double latitude,
                                  final double longitude,
                                  final int course,
                                  final double speed,
                                  final int altitude,
                                  final int amountOfSatellites,
                                  final double hdop,
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
                hdop,
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
