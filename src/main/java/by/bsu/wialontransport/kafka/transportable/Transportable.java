package by.bsu.wialontransport.kafka.transportable;

@FunctionalInterface
public interface Transportable<K> {
    K findKey();
}
