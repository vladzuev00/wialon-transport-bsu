package by.bsu.wialontransport.kafka.model.transportable;

@FunctionalInterface
public interface Transportable<K> {
    K findTopicKey();
}
