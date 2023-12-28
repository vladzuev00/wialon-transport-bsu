package by.bsu.wialontransport.kafka.producer;

import by.bsu.wialontransport.kafka.transportable.Transportable;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.kafka.core.KafkaTemplate;

@RequiredArgsConstructor
public abstract class KafkaProducer<K, V, T extends Transportable<K>, S> {
    private final KafkaTemplate<K, V> kafkaTemplate;
    private final String topicName;

    public final void send(final S source) {
        final T transportable = mapToTransportable(source);
        final K key = transportable.findTopicKey();
        final V value = mapToValue(transportable);
        sendValue(key, value);
    }

    protected abstract T mapToTransportable(final S source);

    protected abstract V mapToValue(final T source);

    private void sendValue(final K key, final V value) {
        final ProducerRecord<K, V> producerRecord = new ProducerRecord<>(topicName, key, value);
        kafkaTemplate.send(producerRecord);
    }
}
