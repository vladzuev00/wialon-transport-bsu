package by.bsu.wialontransport.kafka.consumer;

import org.apache.kafka.clients.consumer.ConsumerRecord;

import java.util.List;

import static by.bsu.wialontransport.util.CollectionUtil.mapToList;

public abstract class KafkaConsumer<K, V, SOURCE> {

    public void consume(final List<ConsumerRecord<K, V>> records) {
        final List<SOURCE> sources = mapToSources(records);
        process(sources);
    }

    protected abstract SOURCE mapToSource(final V value);

    protected abstract void process(final List<SOURCE> sources);

    private List<SOURCE> mapToSources(final List<ConsumerRecord<K, V>> records) {
        return mapToList(records, this::mapToSource);
    }

    private SOURCE mapToSource(final ConsumerRecord<K, V> record) {
        final V value = record.value();
        return mapToSource(value);
    }
}
