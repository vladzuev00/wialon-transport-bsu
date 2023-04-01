package by.bsu.wialontransport.kafka.consumer;

import org.apache.kafka.clients.consumer.ConsumerRecord;

/**
 * @param <K> - topic's key
 * @param <V> - topic's value
 */
public abstract class AbstractKafkaConsumer<K, V, DATA> {

    public void consume(final ConsumerRecord<K, V> consumerRecord) {
        final DATA data = this.mapToData(consumerRecord.value());
        this.processData(data);
    }

    protected abstract DATA mapToData(final V value);

    protected abstract void processData(final DATA data);
}
