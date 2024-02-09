package by.bsu.wialontransport.kafka.consumer.deserializer;

import org.apache.avro.Schema;
import org.apache.avro.generic.GenericDatumReader;
import org.apache.avro.generic.GenericRecord;
import org.apache.avro.io.DatumReader;
import org.apache.avro.io.Decoder;
import org.apache.avro.io.DecoderFactory;
import org.apache.kafka.common.serialization.Deserializer;

import java.io.IOException;
import java.util.Map;

import static by.bsu.wialontransport.kafka.property.KafkaProperty.SCHEMA;

public final class AvroGenericRecordDeserializer implements Deserializer<GenericRecord> {
    private final DatumReader<GenericRecord> datumReader = new GenericDatumReader<>();

    @Override
    public void configure(final Map<String, ?> configurationProperties, final boolean isKey) {
        final Schema schema = (Schema) configurationProperties.get(SCHEMA.getName());
        datumReader.setSchema(schema);
    }

    @Override
    public GenericRecord deserialize(final String topic, final byte[] bytes) {
        try {
            if (bytes == null) {
                return null;
            }
            final Decoder decoder = createDecoder(bytes);
            return datumReader.read(null, decoder);
        } catch (final IOException cause) {
            throw new RecordDeserializationException(cause);
        }
    }

    private static Decoder createDecoder(final byte[] bytes) {
        return DecoderFactory.get().binaryDecoder(bytes, null);
    }

    static final class RecordDeserializationException extends RuntimeException {

        @SuppressWarnings("unused")
        public RecordDeserializationException() {

        }

        @SuppressWarnings("unused")
        public RecordDeserializationException(final String description) {
            super(description);
        }

        public RecordDeserializationException(final Exception cause) {
            super(cause);
        }

        @SuppressWarnings("unused")
        public RecordDeserializationException(final String description, final Exception cause) {
            super(description, cause);
        }
    }
}
