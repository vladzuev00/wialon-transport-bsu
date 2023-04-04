package by.bsu.wialontransport.kafka.consumer;

import org.apache.kafka.clients.consumer.ConsumerRecord;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @param <K> - topic's key
 * @param <V> - topic's value
 */
public abstract class AbstractKafkaConsumer<K, V, DATA> {

    public void consume(final List<ConsumerRecord<K, V>> consumerRecords) {
        final List<DATA> data = this.mapToData(consumerRecords);
        this.processData(data);
    }

    protected abstract DATA mapToData(final V value);

    protected abstract void processData(final List<DATA> data);

    private List<DATA> mapToData(final List<ConsumerRecord<K, V>> consumerRecords) {
        return consumerRecords.stream()
                .map(ConsumerRecord::value)
                .map(this::mapToData)
                .collect(Collectors.toList());
    }
}
