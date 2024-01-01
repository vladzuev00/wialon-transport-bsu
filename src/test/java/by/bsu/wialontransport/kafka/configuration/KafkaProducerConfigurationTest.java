package by.bsu.wialontransport.kafka.configuration;

import by.bsu.wialontransport.kafka.producer.serializer.AvroGenericRecordSerializer;
import org.apache.avro.Schema;
import org.apache.avro.generic.GenericRecord;
import org.apache.kafka.common.serialization.LongSerializer;
import org.junit.Test;
import org.springframework.kafka.core.KafkaTemplate;

import java.util.Map;

import static by.bsu.wialontransport.kafka.property.KafkaProperty.SCHEMA;
import static by.bsu.wialontransport.util.ReflectionUtil.setProperty;
import static org.apache.kafka.clients.producer.ProducerConfig.*;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;

public final class KafkaProducerConfigurationTest {
    private static final String FIELD_NAME_BOOTSTRAP_ADDRESS = "bootstrapAddress";
    private static final String GIVEN_BOOTSTRAP_ADDRESS = "bootstrap-address";

    private final KafkaProducerConfiguration configuration = createConfiguration();

    @Test
    public void kafkaTemplateInboundDataShouldBeCreated() {
        final int givenBatchSize = 5;
        final int givenLingerMs = 6;
        final int givenDeliveryTimeoutMs = 7;
        final Schema givenSchema = mock(Schema.class);

        final KafkaTemplate<Long, GenericRecord> actual = configuration.kafkaTemplateInboundData(
                givenBatchSize,
                givenLingerMs,
                givenDeliveryTimeoutMs,
                givenSchema
        );
        final Map<String, Object> actualPropertiesByNames = actual.getProducerFactory().getConfigurationProperties();
        final Map<String, Object> expectedPropertiesByNames = Map.of(
                BOOTSTRAP_SERVERS_CONFIG, GIVEN_BOOTSTRAP_ADDRESS,
                KEY_SERIALIZER_CLASS_CONFIG, LongSerializer.class,
                VALUE_SERIALIZER_CLASS_CONFIG, AvroGenericRecordSerializer.class,
                BATCH_SIZE_CONFIG, givenBatchSize,
                LINGER_MS_CONFIG, givenLingerMs,
                DELIVERY_TIMEOUT_MS_CONFIG, givenDeliveryTimeoutMs,
                SCHEMA.getName(), givenSchema
        );
        assertEquals(expectedPropertiesByNames, actualPropertiesByNames);
    }

    @Test
    public void kafkaTemplateSavedDataShouldBeCreated() {
        final int givenBatchSize = 5;
        final int givenLingerMs = 6;
        final int givenDeliveryTimeoutMs = 7;
        final Schema givenSchema = mock(Schema.class);

        final KafkaTemplate<Long, GenericRecord> actual = configuration.kafkaTemplateSavedData(
                givenBatchSize,
                givenLingerMs,
                givenDeliveryTimeoutMs,
                givenSchema
        );
        final Map<String, Object> actualPropertiesByNames = actual.getProducerFactory().getConfigurationProperties();
        final Map<String, Object> expectedPropertiesByNames = Map.of(
                BOOTSTRAP_SERVERS_CONFIG, GIVEN_BOOTSTRAP_ADDRESS,
                KEY_SERIALIZER_CLASS_CONFIG, LongSerializer.class,
                VALUE_SERIALIZER_CLASS_CONFIG, AvroGenericRecordSerializer.class,
                BATCH_SIZE_CONFIG, givenBatchSize,
                LINGER_MS_CONFIG, givenLingerMs,
                DELIVERY_TIMEOUT_MS_CONFIG, givenDeliveryTimeoutMs,
                SCHEMA.getName(), givenSchema
        );
        assertEquals(expectedPropertiesByNames, actualPropertiesByNames);
    }

    private static KafkaProducerConfiguration createConfiguration() {
        final KafkaProducerConfiguration configuration = new KafkaProducerConfiguration();
        setBootstrapAddress(configuration);
        return configuration;
    }

    private static void setBootstrapAddress(final KafkaProducerConfiguration configuration) {
        setProperty(configuration, FIELD_NAME_BOOTSTRAP_ADDRESS, GIVEN_BOOTSTRAP_ADDRESS);
    }
}
