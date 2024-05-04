package by.bsu.wialontransport.kafka.producer.serializer;

import by.bsu.wialontransport.base.AbstractSpringBootTest;
import by.bsu.wialontransport.kafka.model.transportable.Transportable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;
import lombok.experimental.FieldNameConstants;
import org.apache.avro.Schema;
import org.apache.avro.generic.GenericData;
import org.apache.avro.generic.GenericRecord;
import org.apache.avro.io.DatumWriter;
import org.apache.avro.reflect.ReflectData;
import org.junit.Before;
import org.junit.Test;

import java.util.Map;

import static by.bsu.wialontransport.kafka.property.KafkaProperty.SCHEMA;
import static by.bsu.wialontransport.util.ReflectionUtil.getProperty;
import static org.junit.Assert.*;

public final class AvroGenericRecordSerializerTest extends AbstractSpringBootTest {
    private static final String FIELD_NAME_DATUM_WRITER = "datumWriter";
    private static final String FIELD_NAME_SCHEMA = "root";

    private static final Schema GIVEN_SCHEMA = createTransportableSchema();
    private static final Map<String, ?> GIVEN_CONFIGURATION_PROPERTIES = Map.of(SCHEMA.getName(), GIVEN_SCHEMA);
    private static final boolean GIVEN_KEY = true;

    private final AvroGenericRecordSerializer serializer = new AvroGenericRecordSerializer();

    @Before
    public void configureSerializer() {
        serializer.configure(GIVEN_CONFIGURATION_PROPERTIES, GIVEN_KEY);
    }

    @Test
    public void serializerShouldBeConfigured() {
        final DatumWriter<GenericRecord> actualDatumWriter = findDatumWriter(serializer);
        assertNotNull(actualDatumWriter);

        final Schema actualSchema = findSchema(actualDatumWriter);
        assertSame(GIVEN_SCHEMA, actualSchema);
    }

    @Test
    public void genericRecordShouldBeSerialized() {
        final String givenTopic = "test-topic";
        final GenericRecord givenGenericRecord = createGenericRecord(255L, "447336934", "text");

        final byte[] actual = serializer.serialize(givenTopic, givenGenericRecord);
        final byte[] expected = new byte[]{-2, 3, 18, 52, 52, 55, 51, 51, 54, 57, 51, 52, 8, 116, 101, 120, 116};
        assertArrayEquals(expected, actual);
    }

    @Test
    @SuppressWarnings("ConstantConditions")
    public void nullGenericRecordShouldBeSerializedToNull() {
        final String givenTopic = "test-topic";

        final byte[] actual = serializer.serialize(givenTopic, null);
        assertNull(actual);
    }

    @SuppressWarnings("unchecked")
    private static DatumWriter<GenericRecord> findDatumWriter(final AvroGenericRecordSerializer serializer) {
        return (DatumWriter<GenericRecord>) getProperty(serializer, FIELD_NAME_DATUM_WRITER, DatumWriter.class);
    }

    private static Schema findSchema(final DatumWriter<GenericRecord> datumWriter) {
        return getProperty(datumWriter, FIELD_NAME_SCHEMA, Schema.class);
    }

    private static Schema createTransportableSchema() {
        return ReflectData.get().getSchema(TestTransportable.class);
    }

    @SuppressWarnings("SameParameterValue")
    private static GenericRecord createGenericRecord(final Long id, final String phoneNumber, final String text) {
        final GenericRecord genericRecord = new GenericData.Record(GIVEN_SCHEMA);
        genericRecord.put(TestTransportable.Fields.id, id);
        genericRecord.put(TestTransportable.Fields.phoneNumber, phoneNumber);
        genericRecord.put(TestTransportable.Fields.text, text);
        return genericRecord;
    }

    @Value
    @AllArgsConstructor
    @Builder
    @FieldNameConstants
    private static class TestTransportable implements Transportable<String> {
        Long id;
        String phoneNumber;
        String text;

        @Override
        public String findTopicKey() {
            return phoneNumber;
        }
    }
}
