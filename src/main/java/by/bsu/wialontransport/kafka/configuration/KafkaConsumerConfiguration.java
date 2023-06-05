package by.bsu.wialontransport.kafka.configuration;

import by.bsu.wialontransport.kafka.consumer.deserializer.AvroGenericRecordDeserializer;
import org.apache.avro.Schema;
import org.apache.avro.generic.GenericRecord;
import org.apache.kafka.common.serialization.LongDeserializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;

import java.util.Map;

import static org.apache.kafka.clients.consumer.ConsumerConfig.*;
import static org.springframework.kafka.listener.ContainerProperties.AckMode.MANUAL_IMMEDIATE;
import static by.bsu.wialontransport.kafka.property.KafkaProperty.SCHEMA;

@EnableKafka
@Configuration
public class KafkaConsumerConfiguration {

    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapAddress;

    /**
     * Kafka factory of consumer, which consumes data from kafka, which haven't been saved in database yet.
     */
    @Bean
    @Autowired
    public ConsumerFactory<Long, GenericRecord> consumerFactoryInboundData(
            @Value("${kafka.topic.inbound-data.consumer.group-id}") final String groupId,
            @Value("${kafka.topic.inbound-data.consumer.max-poll-records}") final int maxPollRecords,
            @Value("${kafka.topic.inbound-data.consumer.fetch-max-wait-ms}") final int fetchMaxWaitMs,
            @Value("${kafka.topic.inbound-data.consumer.fetch-min-bytes}") final int fetchMinBytes,
            @Qualifier("transportableDataSchema") final Schema schema) {
        return this.createConsumerFactory(groupId, maxPollRecords, fetchMaxWaitMs, fetchMinBytes, schema);
    }

    @Bean
    @Autowired
    public ConcurrentKafkaListenerContainerFactory<Long, GenericRecord> kafkaListenerContainerFactoryInboundData(
            @Qualifier("consumerFactoryInboundData")
            final ConsumerFactory<Long, GenericRecord> consumerFactoryInboundData) {
        return createKafkaListenerContainerFactory(consumerFactoryInboundData);
    }

    /**
     * Kafka factory of consumer, which consumes data from kafka, which have been saved in database.
     */
    @Bean
    @Autowired
    public ConsumerFactory<Long, GenericRecord> consumerFactorySavedData(
            @Value("${kafka.topic.saved-data.consumer.group-id}") final String groupId,
            @Value("${kafka.topic.saved-data.consumer.max-poll-records}") final int maxPollRecords,
            @Value("${kafka.topic.saved-data.consumer.fetch-max-wait-ms}") final int fetchMaxWaitMs,
            @Value("${kafka.topic.saved-data.consumer.fetch-min-bytes}") final int fetchMinBytes,
            @Qualifier("transportableSavedDataSchema") final Schema schema) {
        return this.createConsumerFactory(groupId, maxPollRecords, fetchMaxWaitMs, fetchMinBytes, schema);
    }

    @Bean
    @Autowired
    public ConcurrentKafkaListenerContainerFactory<Long, GenericRecord> kafkaListenerContainerFactorySavedData(
            @Qualifier("consumerFactorySavedData")
            final ConsumerFactory<Long, GenericRecord> consumerFactorySavedData) {
        return createKafkaListenerContainerFactory(consumerFactorySavedData);
    }

    private <KeyType, ValueType> ConsumerFactory<KeyType, ValueType> createConsumerFactory(final String groupId,
                                                                                           final int maxPollRecords,
                                                                                           final int fetchMaxWaitMs,
                                                                                           final int fetchMinBytes,
                                                                                           final Schema schema) {
        final Map<String, Object> configurationProperties = Map.of(
                BOOTSTRAP_SERVERS_CONFIG, this.bootstrapAddress,
                GROUP_ID_CONFIG, groupId,
                KEY_DESERIALIZER_CLASS_CONFIG, LongDeserializer.class,
                VALUE_DESERIALIZER_CLASS_CONFIG, AvroGenericRecordDeserializer.class,
                MAX_POLL_RECORDS_CONFIG, maxPollRecords,
                FETCH_MAX_WAIT_MS_CONFIG, fetchMaxWaitMs,
                FETCH_MIN_BYTES_CONFIG, fetchMinBytes,
                ENABLE_AUTO_COMMIT_CONFIG, false,
                SCHEMA.getName(), schema);
        return new DefaultKafkaConsumerFactory<>(configurationProperties);
    }

    private static <KeyType, ValueType> ConcurrentKafkaListenerContainerFactory<KeyType, ValueType> createKafkaListenerContainerFactory(
            final ConsumerFactory<KeyType, ValueType> consumerFactory) {
        final ConcurrentKafkaListenerContainerFactory<KeyType, ValueType> listenerContainerFactory
                = new ConcurrentKafkaListenerContainerFactory<>();
        listenerContainerFactory.setBatchListener(true);
        listenerContainerFactory.setConsumerFactory(consumerFactory);
        listenerContainerFactory.getContainerProperties()
                .setAckMode(MANUAL_IMMEDIATE);
        return listenerContainerFactory;
    }
}
