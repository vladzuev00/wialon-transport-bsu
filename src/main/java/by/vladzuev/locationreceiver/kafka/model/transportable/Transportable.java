package by.vladzuev.locationreceiver.kafka.model.transportable;

@FunctionalInterface
public interface Transportable<K> {
    K findTopicKey();
}
