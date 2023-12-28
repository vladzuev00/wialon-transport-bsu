package by.bsu.wialontransport.kafka.producer;

import by.bsu.wialontransport.kafka.transportable.Transportable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;
import lombok.experimental.FieldNameConstants;
import org.apache.avro.Schema;
import org.apache.avro.generic.GenericData;
import org.apache.avro.generic.GenericRecord;
import org.apache.avro.reflect.ReflectData;
import org.junit.Test;
import org.springframework.kafka.core.KafkaTemplate;

import static org.junit.Assert.assertEquals;

public final class GenericRecordKafkaProducerTest {
    private static final Schema GIVEN_SCHEMA = createTransportableSchema();

    private final TestKafkaProducer producer = new TestKafkaProducer(null, null, GIVEN_SCHEMA);

    @Test
    public void sourceShouldBeMappedToValue() {
        final Long givenId = 255L;
        final String givenPhoneNumber = "447336934";
        final String givenText = "text";
        final TestTransportable givenSource = new TestTransportable(givenId, givenPhoneNumber, givenText);

        final GenericRecord actual = producer.mapToValue(givenSource);
        final GenericRecord expected = createGenericRecord(givenId, givenPhoneNumber, givenText);
        assertEquals(expected, actual);
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

    private static final class TestKafkaProducer extends GenericRecordKafkaProducer<String, TestTransportable, Object> {


        public TestKafkaProducer(final KafkaTemplate<String, GenericRecord> kafkaTemplate,
                                 final String topicName,
                                 final Schema schema) {
            super(kafkaTemplate, topicName, schema);
        }

        @Override
        protected TestTransportable mapToTransportable(final Object source) {
            throw new UnsupportedOperationException();
        }
    }
}
