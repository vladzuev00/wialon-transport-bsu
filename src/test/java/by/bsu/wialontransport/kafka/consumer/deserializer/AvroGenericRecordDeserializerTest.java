package by.bsu.wialontransport.kafka.consumer.deserializer;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;
import lombok.experimental.FieldNameConstants;
import org.apache.avro.Schema;
import org.apache.avro.generic.GenericData;
import org.apache.avro.generic.GenericDatumReader;
import org.apache.avro.generic.GenericRecord;
import org.apache.avro.io.DatumReader;
import org.apache.avro.reflect.ReflectData;
import org.junit.Before;
import org.junit.Test;

import java.util.Map;

import static by.bsu.wialontransport.kafka.property.KafkaProperty.SCHEMA;
import static by.bsu.wialontransport.util.ReflectionUtil.getProperty;
import static org.junit.Assert.*;

public final class AvroGenericRecordDeserializerTest {
    private static final String FIELD_NAME_DATUM_READER = "datumReader";

    private static final Schema GIVEN_SCHEMA = createSourceSchema();
    private static final Map<String, ?> GIVEN_CONFIGURATION_PROPERTIES = Map.of(SCHEMA.getName(), GIVEN_SCHEMA);
    private static final boolean GIVEN_KEY = true;

    private final AvroGenericRecordDeserializer deserializer = new AvroGenericRecordDeserializer();

    @Before
    public void configureDeserializer() {
        deserializer.configure(GIVEN_CONFIGURATION_PROPERTIES, GIVEN_KEY);
    }

    @Test
    public void deserializerShouldBeConfigured() {
        final GenericDatumReader<GenericRecord> actualDatumReader = findDatumReader(deserializer);
        assertNotNull(actualDatumReader);

        final Schema actualSchema = actualDatumReader.getSchema();
        assertSame(GIVEN_SCHEMA, actualSchema);
    }

    @Test
    public void bytesShouldBeDeserialized() {
        final String givenTopic = "test-topic";
        final byte[] givenBytes = {-2, 3, 18, 52, 52, 55, 51, 51, 54, 57, 51, 52, 8, 116, 101, 120, 116};

        final GenericRecord actual = deserializer.deserialize(givenTopic, givenBytes);
        final GenericRecord expected = createRecord(255L, "447336934", "text");
        assertEquals(expected, actual);
    }

    @Test
    public void nullBytesShouldBeDeserializedToNull() {
        final String givenTopic = "test-topic";

        final GenericRecord actual = deserializer.deserialize(givenTopic, null);
        assertNull(actual);
    }

    private static Schema createSourceSchema() {
        return ReflectData.get().getSchema(TestSource.class);
    }

    @SuppressWarnings("unchecked")
    private static GenericDatumReader<GenericRecord> findDatumReader(final AvroGenericRecordDeserializer deserializer) {
        return (GenericDatumReader<GenericRecord>) getProperty(deserializer, FIELD_NAME_DATUM_READER, DatumReader.class);
    }

    @SuppressWarnings("SameParameterValue")
    private static GenericRecord createRecord(final Long id, final String phoneNumber, final String text) {
        final GenericRecord record = new GenericData.Record(GIVEN_SCHEMA);
        record.put(TestSource.Fields.id, id);
        record.put(TestSource.Fields.phoneNumber, phoneNumber);
        record.put(TestSource.Fields.text, text);
        return record;
    }

    @Value
    @AllArgsConstructor
    @Builder
    @FieldNameConstants
    private static class TestSource {
        Long id;
        String phoneNumber;
        String text;
    }
}
