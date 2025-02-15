package by.vladzuev.locationreceiver.kafka.producer.serializer;

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

import static by.vladzuev.locationreceiver.kafka.property.KafkaProperty.SCHEMA;

public final class AvroGenericRecordSerializer implements Serializer<GenericRecord> {
    private final DatumWriter<GenericRecord> datumWriter = new GenericDatumWriter<>();

    @Override
    public void configure(final Map<String, ?> configurationProperties, final boolean key) {
        final Schema schema = (Schema) configurationProperties.get(SCHEMA.getName());
        datumWriter.setSchema(schema);
    }

    @Override
    public byte[] serialize(final String topic, final GenericRecord record) {
        if (record == null) {
            return null;
        }
        try (final ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            final BinaryEncoder binaryEncoder = createBinaryEncoder(outputStream);
            datumWriter.write(record, binaryEncoder);
            binaryEncoder.flush();
            return outputStream.toByteArray();
        } catch (final IOException cause) {
            throw new RecordSerializationException(cause);
        }
    }

    private static BinaryEncoder createBinaryEncoder(final ByteArrayOutputStream outputStream) {
        return EncoderFactory.get().binaryEncoder(outputStream, null);
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
