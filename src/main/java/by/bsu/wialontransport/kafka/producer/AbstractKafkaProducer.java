package by.bsu.wialontransport.kafka.producer;

import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.kafka.core.KafkaTemplate;

/**
 * @param <K>             - topic's key
 * @param <V>             - topic's value
 * @param <TRANSPORTABLE> - value which was mapped to value before it will be sent to kafka's topic
 * @param <SOURCE>        - value which was mapped to TRANSPORTABLE before it will be sent to kafka's topic
 */
public abstract class AbstractKafkaProducer<K, V, TRANSPORTABLE, SOURCE> {
    private final KafkaTemplate<K, V> kafkaTemplate;
    private final String topicName;

    public AbstractKafkaProducer(final KafkaTemplate<K, V> kafkaTemplate, final String topicName) {
        this.kafkaTemplate = kafkaTemplate;
        this.topicName = topicName;
    }

    public final void send(final SOURCE source) {
        final TRANSPORTABLE transportable = this.mapToTransportable(source);
        final K key = this.findKey(transportable);
        final V value = this.mapToValue(transportable);
        this.send(key, value);
    }

    protected abstract K findKey(final TRANSPORTABLE transportable);

    protected abstract TRANSPORTABLE mapToTransportable(final SOURCE source);

    protected abstract V mapToValue(final TRANSPORTABLE mapped);

    private void send(final K key, final V value) {
        final ProducerRecord<K, V> producerRecord = new ProducerRecord<>(this.topicName, key, value);
        this.kafkaTemplate.send(producerRecord);
    }
}
