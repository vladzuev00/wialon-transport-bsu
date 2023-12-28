package by.bsu.wialontransport.kafka.property;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum KafkaProperty {
    SCHEMA("SCHEMA");

    private final String name;
}
