package by.bsu.wialontransport.kafka.configuration;

import by.bsu.wialontransport.kafka.serializer.AvroGenericRecordSerializer;
import org.apache.avro.Schema;
import org.apache.avro.generic.GenericRecord;
import org.apache.kafka.common.serialization.LongSerializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

import java.util.Map;

import static by.bsu.wialontransport.kafka.property.KafkaProperty.SCHEMA;
import static org.apache.kafka.clients.producer.ProducerConfig.*;

@Configuration
@Import(SchemaConfiguration.class)
public class KafkaProducerConfiguration {

    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapAddress;

    /**
     * Kafka template for producer, which sends data, which haven't been saved in database yet, to kafka.
     */
    @Bean
    @Autowired
    public KafkaTemplate<Long, GenericRecord> kafkaTemplateInboundData(
            @Value("${kafka.topic.inbound-data.producer.batch-size}") final int batchSize,
            @Value("${kafka.topic.inbound-data.producer.linger-ms}") final int lingerMs,
            @Value("${kafka.topic.inbound-data.producer.delivery-timeout-ms}") final int deliveryTimeoutMs,
            @Qualifier("dataSchema") final Schema schema) {
        return new KafkaTemplate<>(this.createProducerFactory(batchSize, lingerMs, deliveryTimeoutMs, schema));
    }

    /**
     * Kafka template for producer, which sends data, which have been saved in database, to kafka.
     */
    @Bean
    @Autowired
    public KafkaTemplate<Long, GenericRecord> kafkaTemplateSavedData(
            @Value("${kafka.topic.saved-data.producer.batch-size}") final int batchSize,
            @Value("${kafka.topic.saved-data.producer.linger-ms}") final int lingerMs,
            @Value("${kafka.topic.saved-data.producer.delivery-timeout-ms}") final int deliveryTimeoutMs,
            @Qualifier("dataSchema") final Schema schema) {
        return new KafkaTemplate<>(this.createProducerFactory(batchSize, lingerMs, deliveryTimeoutMs, schema));
    }

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
                SCHEMA.getName(), schema);
        return new DefaultKafkaProducerFactory<>(configurationProperties);
    }
}
