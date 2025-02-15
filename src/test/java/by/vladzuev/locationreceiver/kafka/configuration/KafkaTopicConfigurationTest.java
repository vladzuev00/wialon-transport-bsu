package by.vladzuev.locationreceiver.kafka.configuration;

import org.apache.kafka.clients.admin.NewTopic;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public final class KafkaTopicConfigurationTest {
    private final KafkaTopicConfiguration configuration = new KafkaTopicConfiguration();

    @Test
    public void inboundDataTopicShouldBeCreated() {
        final String givenTopicName = "topic";
        final int givenPartitionsAmount = 5;
        final short givenReplicationFactor = 6;

        final NewTopic actual = configuration.inboundDataTopic(
                givenTopicName,
                givenPartitionsAmount,
                givenReplicationFactor
        );
        final NewTopic expected = new NewTopic(givenTopicName, givenPartitionsAmount, givenReplicationFactor);
        assertEquals(expected, actual);
    }

    @Test
    public void savedDataTopicShouldBeCreated() {
        final String givenTopicName = "topic";
        final int givenPartitionsAmount = 5;
        final short givenReplicationFactor = 6;

        final NewTopic actual = configuration.savedDataTopic(
                givenTopicName,
                givenPartitionsAmount,
                givenReplicationFactor
        );
        final NewTopic expected = new NewTopic(givenTopicName, givenPartitionsAmount, givenReplicationFactor);
        assertEquals(expected, actual);
    }
}
