package by.bsu.wialontransport.kafka.configuration;

import org.apache.kafka.common.protocol.types.Schema;
import org.apache.kafka.common.serialization.LongSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.ProducerFactory;

import java.util.Map;

import static org.apache.kafka.clients.producer.ProducerConfig.*;

@Configuration
public final class KafkaProducerConfiguration {

    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapAddress;

    private <KeyType, ValueType> ProducerFactory<KeyType, ValueType> createProducerFactory(final int batchSize,
                                                                                           final int lingerMs,
                                                                                           final int deliveryTimeoutMs,
                                                                                           final Schema schema) {
        final Map<String, Object> configurationProperties = Map.of(
                BOOTSTRAP_SERVERS_CONFIG, this.bootstrapAddress,
                KEY_SERIALIZER_CLASS_CONFIG, LongSerializer.class,
                VALUE_SERIALIZER_CLASS_CONFIG, AvroGenericRecordSerializer.class,
                BATCH_SIZE_CONFIG, batchSize,
                LINGER_MS_CONFIG, lingerMs,
                DELIVERY_TIMEOUT_MS_CONFIG, deliveryTimeoutMs,
                SCHEMA_PROP_NAME, schema);
        return new DefaultKafkaProducerFactory<>(configurationProperties);
    }
}
