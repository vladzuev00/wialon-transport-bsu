package by.bsu.wialontransport.kafka.configuration;

import by.bsu.wialontransport.kafka.consumer.deserializer.AvroGenericRecordDeserializer;
import org.apache.avro.Schema;
import org.apache.avro.generic.GenericRecord;
import org.apache.kafka.common.serialization.LongDeserializer;
import org.junit.Test;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.listener.ContainerProperties.AckMode;

import java.util.Map;

import static by.bsu.wialontransport.kafka.property.KafkaProperty.SCHEMA;
import static by.bsu.wialontransport.util.ReflectionUtil.setProperty;
import static org.apache.kafka.clients.consumer.ConsumerConfig.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.springframework.kafka.listener.ContainerProperties.AckMode.MANUAL_IMMEDIATE;

public final class KafkaConsumerConfigurationTest {
    private static final String FIELD_NAME_BOOTSTRAP_ADDRESS = "bootstrapAddress";
    private static final String GIVEN_BOOTSTRAP_ADDRESS = "bootstrap-address";

    private final KafkaConsumerConfiguration configuration = createConfiguration();

    @Test
    public void consumerFactoryInboundDataShouldBeCreated() {
        final String givenGroupId = "group-id";
        final int givenMaxPollRecords = 5;
        final int givenFetchMaxWaitMs = 6;
        final int givenFetchMinBytes = 7;
        final Schema givenSchema = mock(Schema.class);

        final ConsumerFactory<Long, GenericRecord> actual = configuration.consumerFactoryInboundData(
                givenGroupId,
                givenMaxPollRecords,
                givenFetchMaxWaitMs,
                givenFetchMinBytes,
                givenSchema
        );
        final Map<String, Object> actualPropertiesByNames = actual.getConfigurationProperties();
        final Map<String, Object> expectedPropertiesByNames = Map.of(
                BOOTSTRAP_SERVERS_CONFIG, GIVEN_BOOTSTRAP_ADDRESS,
                GROUP_ID_CONFIG, givenGroupId,
                KEY_DESERIALIZER_CLASS_CONFIG, LongDeserializer.class,
                VALUE_DESERIALIZER_CLASS_CONFIG, AvroGenericRecordDeserializer.class,
                MAX_POLL_RECORDS_CONFIG, givenMaxPollRecords,
                FETCH_MAX_WAIT_MS_CONFIG, givenFetchMaxWaitMs,
                FETCH_MIN_BYTES_CONFIG, givenFetchMinBytes,
                ENABLE_AUTO_COMMIT_CONFIG, false,
                SCHEMA.getName(), givenSchema
        );
        assertEquals(expectedPropertiesByNames, actualPropertiesByNames);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void listenerContainerFactoryInboundDataShouldBeCreated() {
        final ConsumerFactory<Long, GenericRecord> givenConsumerFactory = mock(ConsumerFactory.class);

        final ConcurrentKafkaListenerContainerFactory<Long, GenericRecord> actual = configuration
                .listenerContainerFactoryInboundData(givenConsumerFactory);

        final ConsumerFactory<?, ?> actualConsumerFactory = actual.getConsumerFactory();
        assertSame(givenConsumerFactory, actualConsumerFactory);

        final Boolean actualBatchListener = actual.isBatchListener();
        final Boolean expectedBatchListener = true;
        assertEquals(expectedBatchListener, actualBatchListener);

        final AckMode actualAckMode = actual.getContainerProperties().getAckMode();
        assertSame(MANUAL_IMMEDIATE, actualAckMode);
    }

    @Test
    public void consumerFactorySavedDataShouldBeCreated() {
        final String givenGroupId = "group-id";
        final int givenMaxPollRecords = 5;
        final int givenFetchMaxWaitMs = 6;
        final int givenFetchMinBytes = 7;
        final Schema givenSchema = mock(Schema.class);

        final ConsumerFactory<Long, GenericRecord> actual = configuration.consumerFactorySavedData(
                givenGroupId,
                givenMaxPollRecords,
                givenFetchMaxWaitMs,
                givenFetchMinBytes,
                givenSchema
        );
        final Map<String, Object> actualPropertiesByNames = actual.getConfigurationProperties();
        final Map<String, Object> expectedPropertiesByNames = Map.of(
                BOOTSTRAP_SERVERS_CONFIG, GIVEN_BOOTSTRAP_ADDRESS,
                GROUP_ID_CONFIG, givenGroupId,
                KEY_DESERIALIZER_CLASS_CONFIG, LongDeserializer.class,
                VALUE_DESERIALIZER_CLASS_CONFIG, AvroGenericRecordDeserializer.class,
                MAX_POLL_RECORDS_CONFIG, givenMaxPollRecords,
                FETCH_MAX_WAIT_MS_CONFIG, givenFetchMaxWaitMs,
                FETCH_MIN_BYTES_CONFIG, givenFetchMinBytes,
                ENABLE_AUTO_COMMIT_CONFIG, false,
                SCHEMA.getName(), givenSchema
        );
        assertEquals(expectedPropertiesByNames, actualPropertiesByNames);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void listenerContainerFactorySavedDataShouldBeCreated() {
        final ConsumerFactory<Long, GenericRecord> givenConsumerFactory = mock(ConsumerFactory.class);

        final ConcurrentKafkaListenerContainerFactory<Long, GenericRecord> actual = configuration
                .listenerContainerFactorySavedData(givenConsumerFactory);

        final ConsumerFactory<?, ?> actualConsumerFactory = actual.getConsumerFactory();
        assertSame(givenConsumerFactory, actualConsumerFactory);

        final Boolean actualBatchListener = actual.isBatchListener();
        final Boolean expectedBatchListener = true;
        assertEquals(expectedBatchListener, actualBatchListener);

        final AckMode actualAckMode = actual.getContainerProperties().getAckMode();
        assertSame(MANUAL_IMMEDIATE, actualAckMode);
    }

    private static KafkaConsumerConfiguration createConfiguration() {
        final KafkaConsumerConfiguration configuration = new KafkaConsumerConfiguration();
        setBootstrapAddress(configuration);
        return configuration;
    }

    private static void setBootstrapAddress(final KafkaConsumerConfiguration configuration) {
        setProperty(configuration, FIELD_NAME_BOOTSTRAP_ADDRESS, GIVEN_BOOTSTRAP_ADDRESS);
    }
}
