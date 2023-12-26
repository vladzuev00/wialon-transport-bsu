package by.bsu.wialontransport.kafka.producer;

import by.bsu.wialontransport.kafka.transportable.Transportable;
import org.apache.avro.Schema;
import org.apache.avro.generic.GenericDatumReader;
import org.apache.avro.generic.GenericRecord;
import org.apache.avro.io.*;
import org.apache.avro.reflect.ReflectDatumWriter;
import org.springframework.kafka.core.KafkaTemplate;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public abstract class AbstractGenericRecordKafkaProducer<K, T extends Transportable<K>, S>
        extends AbstractKafkaProducer<K, GenericRecord, T, S> {
    private final Schema schema;

    public AbstractGenericRecordKafkaProducer(final KafkaTemplate<K, GenericRecord> kafkaTemplate,
                                              final String topicName,
                                              final Schema schema) {
        super(kafkaTemplate, topicName);
        this.schema = schema;
    }

    @Override
    protected final GenericRecord mapToValue(final T mapped) {
        try (final ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            final ReflectDatumWriter<T> datumWriter = new ReflectDatumWriter<>(schema);
            final BinaryEncoder encoder = createEncoder(outputStream);
            datumWriter.write(mapped, encoder);
            encoder.flush();
            final DatumReader<GenericRecord> datumReader = new GenericDatumReader<>(schema);
            final BinaryDecoder decoder = createDecoder(outputStream);
            return datumReader.read(null, decoder);
        } catch (final IOException cause) {
            throw new KafkaProducerMappingToGenericRecordException(cause);
        }
    }

    private static BinaryEncoder createEncoder(final OutputStream outputStream) {
        return EncoderFactory.get().binaryEncoder(outputStream, null);
    }

    private static BinaryDecoder createDecoder(final ByteArrayOutputStream outputStream) {
        return DecoderFactory.get().binaryDecoder(outputStream.toByteArray(), null);
    }

    static final class KafkaProducerMappingToGenericRecordException extends RuntimeException {

        @SuppressWarnings("unused")
        public KafkaProducerMappingToGenericRecordException() {

        }

        @SuppressWarnings("unused")
        public KafkaProducerMappingToGenericRecordException(final String description) {
            super(description);
        }

        public KafkaProducerMappingToGenericRecordException(final Exception cause) {
            super(cause);
        }

        @SuppressWarnings("unused")
        public KafkaProducerMappingToGenericRecordException(final String description, final Exception cause) {
            super(description, cause);
        }
    }
}
