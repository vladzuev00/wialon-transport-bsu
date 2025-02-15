package by.vladzuev.locationreceiver.kafka.configuration;

import by.vladzuev.locationreceiver.kafka.model.transportable.data.TransportableData;
import by.vladzuev.locationreceiver.kafka.model.transportable.data.TransportableSavedData;
import org.apache.avro.Schema;
import org.apache.avro.reflect.ReflectData;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SchemaConfiguration {

    @Bean
    public ReflectData reflectData() {
        return ReflectData.get();
    }

    @Bean
    public Schema transportableDataSchema(final ReflectData reflectData) {
        return reflectData.getSchema(TransportableData.class);
    }

    @Bean
    public Schema transportableSavedDataSchema(final ReflectData reflectData) {
        return reflectData.getSchema(TransportableSavedData.class);
    }
}
