package by.bsu.wialontransport.kafka.model.transportable.data;

import lombok.Builder;
import org.junit.Test;

import static org.junit.Assert.assertSame;

public final class TransportableDataTest {

    @Test
    public void topicKeyShouldBeFound() {
        final Long givenTrackerId = 255L;
        final TransportableData givenData = TestTransportableData.builder()
                .trackerId(givenTrackerId)
                .build();

        final Long actual = givenData.findTopicKey();
        assertSame(givenTrackerId, actual);
    }

    private static final class TestTransportableData extends TransportableData {

        @Builder
        public TestTransportableData(final long epochSeconds,
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
                                     final Long trackerId) {
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
        }
    }
}
