package by.bsu.wialontransport.kafka.model.transportable.data;

import lombok.Builder;

public final class TransportableInboundData extends TransportableData {

    @Builder
    public TransportableInboundData(final long epochSeconds,
                                    final double latitude,
                                    final double longitude,
                                    final int course,
                                    final double speed,
                                    final int altitude,
                                    final int amountOfSatellites,
                                    final double reductionPrecision,
                                    final int inputs,
                                    final int outputs,
                                    final String serializedAnalogInputs,
                                    final String driverKeyCode,
                                    final String serializedParameters,
                                    final Long trackerId) {
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
    }
}