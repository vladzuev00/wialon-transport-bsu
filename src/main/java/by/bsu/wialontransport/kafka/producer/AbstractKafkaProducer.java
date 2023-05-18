package by.bsu.wialontransport.kafka.producer;

import by.bsu.wialontransport.kafka.transportable.Transportable;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.kafka.core.KafkaTemplate;

/**
 * @param <K>      - topic's key
 * @param <V>      - topic's value
 * @param <T>      - value which was mapped to value before it will be sent to kafka's topic
 * @param <S> - value which was mapped to TRANSPORTABLE before it will be sent to kafka's topic
 */
public abstract class AbstractKafkaProducer<K, V, T extends Transportable<K>, S> {
    private final KafkaTemplate<K, V> kafkaTemplate;
    private final String topicName;

    public AbstractKafkaProducer(final KafkaTemplate<K, V> kafkaTemplate, final String topicName) {
        this.kafkaTemplate = kafkaTemplate;
        this.topicName = topicName;
    }

    public final void send(final S source) {
        final T transportable = this.mapToTransportable(source);
        final K key = transportable.findTopicKey();
        final V value = this.mapToValue(transportable);
        this.sendValue(key, value);
    }

    protected abstract T mapToTransportable(final S source);

    protected abstract V mapToValue(final T mapped);

    private void sendValue(final K key, final V value) {
        final ProducerRecord<K, V> producerRecord = new ProducerRecord<>(this.topicName, key, value);
        this.kafkaTemplate.send(producerRecord);
    }
}
