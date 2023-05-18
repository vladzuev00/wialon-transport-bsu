package by.bsu.wialontransport.kafka.transportable;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public final class TransportableDataTest {

    @Test
    public void topicKeyShouldBeFound() {
        final Long givenTrackerId = 255L;
        final TransportableData givenData = TransportableData.builder()
                .trackerId(givenTrackerId)
                .build();

        final Long actual = givenData.findTopicKey();
        assertEquals(givenTrackerId, actual);
    }

}
