package by.bsu.wialontransport.kafka.producer.serializer;

import org.apache.avro.Schema;
import org.apache.avro.generic.GenericDatumWriter;
import org.apache.avro.generic.GenericRecord;
import org.apache.avro.io.BinaryEncoder;
import org.apache.avro.io.DatumWriter;
import org.apache.avro.io.EncoderFactory;
import org.apache.kafka.common.serialization.Serializer;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Map;

import static by.bsu.wialontransport.kafka.property.KafkaProperty.SCHEMA;

public final class AvroGenericRecordSerializer implements Serializer<GenericRecord> {
    private Schema schema;

    @Override
    public void configure(final Map<String, ?> configurationProperties, final boolean key) {
        schema = (Schema) configurationProperties.get(SCHEMA.getName());
    }

    @Override
    public byte[] serialize(final String topic, final GenericRecord record) {
        if (record == null) {
            return null;
        }
        try (final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()) {
            final BinaryEncoder binaryEncoder = EncoderFactory.get().binaryEncoder(byteArrayOutputStream, null);
            final DatumWriter<GenericRecord> datumWriter = new GenericDatumWriter<>(schema);
            datumWriter.write(record, binaryEncoder);
            binaryEncoder.flush();
            return byteArrayOutputStream.toByteArray();
        } catch (final IOException cause) {
            throw new RecordSerializationException(cause);
        }
    }

    static final class RecordSerializationException extends RuntimeException {

        @SuppressWarnings("unused")
        public RecordSerializationException() {

        }

        @SuppressWarnings("unused")
        public RecordSerializationException(final String description) {
            super(description);
        }

        public RecordSerializationException(final Exception cause) {
            super(cause);
        }

        @SuppressWarnings("unused")
        public RecordSerializationException(final String description, final Exception cause) {
            super(description, cause);
        }
    }
}
