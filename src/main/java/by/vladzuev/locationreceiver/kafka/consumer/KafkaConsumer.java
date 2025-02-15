package by.vladzuev.locationreceiver.kafka.consumer;

import by.vladzuev.locationreceiver.util.CollectionUtil;
import org.apache.kafka.clients.consumer.ConsumerRecord;

import java.util.List;

public abstract class KafkaConsumer<K, V, SOURCE> {

    public void consume(final List<ConsumerRecord<K, V>> records) {
        final List<SOURCE> sources = mapToSources(records);
        process(sources);
    }

    protected abstract SOURCE mapToSource(final V value);

    protected abstract void process(final List<SOURCE> sources);

    private List<SOURCE> mapToSources(final List<ConsumerRecord<K, V>> records) {
        return CollectionUtil.mapToList(records, this::mapToSource);
    }

    private SOURCE mapToSource(final ConsumerRecord<K, V> record) {
        final V value = record.value();
        return mapToSource(value);
    }
}
