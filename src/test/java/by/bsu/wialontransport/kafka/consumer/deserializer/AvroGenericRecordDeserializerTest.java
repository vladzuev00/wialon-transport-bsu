package by.bsu.wialontransport.kafka.consumer.deserializer;

import org.apache.avro.Schema;
import org.junit.Test;

import java.lang.reflect.Field;
import java.util.Map;

import static by.bsu.wialontransport.kafka.property.KafkaProperty.SCHEMA;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;

public final class AvroGenericRecordDeserializerTest {
    private static final String FIELD_NAME_SCHEMA = "schema";

    private final AvroGenericRecordDeserializer deserializer = new AvroGenericRecordDeserializer();

    @Test
    public void deserializerShouldBeConfigured()
            throws Exception {
        final Schema givenSchema = mock(Schema.class);
        final Map<String, ?> givenConfigurationProperties = Map.of(SCHEMA.getName(), givenSchema);
        final boolean givenIsKey = false;

        this.deserializer.configure(givenConfigurationProperties, givenIsKey);

        final Schema actual = this.findDeserializerSchema();
        assertSame(givenSchema, actual);
    }

    @Test
    public void genericRecordShouldBeDeserialized() {
        throw new RuntimeException();
    }

    private Schema findDeserializerSchema()
            throws Exception {
        final Field field = AvroGenericRecordDeserializer.class.getDeclaredField(FIELD_NAME_SCHEMA);
        field.setAccessible(true);
        try {
            return (Schema) field.get(this.deserializer);
        } finally {
            field.setAccessible(false);
        }
    }
}
