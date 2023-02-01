package by.bsu.wialontransport.kafka.property;

public enum KafkaProperty {
    SCHEMA("SCHEMA");

    private final String name;

    KafkaProperty(final String name) {
        this.name = name;
    }

    public final String getName() {
        return this.name;
    }
}
