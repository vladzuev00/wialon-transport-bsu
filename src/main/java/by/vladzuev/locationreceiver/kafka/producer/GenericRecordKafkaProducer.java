package by.vladzuev.locationreceiver.kafka.producer;

import by.vladzuev.locationreceiver.kafka.model.transportable.Transportable;
import org.apache.avro.Schema;
import org.apache.avro.generic.GenericDatumReader;
import org.apache.avro.generic.GenericRecord;
import org.apache.avro.io.*;
import org.apache.avro.reflect.ReflectDatumWriter;
import org.springframework.kafka.core.KafkaTemplate;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public abstract class GenericRecordKafkaProducer<K, T extends Transportable<K>, S>
        extends KafkaProducer<K, GenericRecord, T, S> {
    private final Schema schema;

    public GenericRecordKafkaProducer(final KafkaTemplate<K, GenericRecord> kafkaTemplate,
                                      final String topicName,
                                      final Schema schema) {
        super(kafkaTemplate, topicName);
        this.schema = schema;
    }

    @Override
    protected final GenericRecord mapToValue(final T source) {
        try (final ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            final ReflectDatumWriter<T> datumWriter = new ReflectDatumWriter<>(schema);
            final BinaryEncoder encoder = createEncoder(outputStream);
            datumWriter.write(source, encoder);
            encoder.flush();
            final DatumReader<GenericRecord> datumReader = new GenericDatumReader<>(schema);
            final BinaryDecoder decoder = createDecoder(outputStream);
            return datumReader.read(null, decoder);
        } catch (final IOException cause) {
            throw new MappingTransportableToGenericRecordException(cause);
        }
    }

    private static BinaryEncoder createEncoder(final OutputStream outputStream) {
        return EncoderFactory.get().binaryEncoder(outputStream, null);
    }

    private static BinaryDecoder createDecoder(final ByteArrayOutputStream outputStream) {
        return DecoderFactory.get().binaryDecoder(outputStream.toByteArray(), null);
    }

    static final class MappingTransportableToGenericRecordException extends RuntimeException {

        @SuppressWarnings("unused")
        public MappingTransportableToGenericRecordException() {

        }

        @SuppressWarnings("unused")
        public MappingTransportableToGenericRecordException(final String description) {
            super(description);
        }

        public MappingTransportableToGenericRecordException(final Exception cause) {
            super(cause);
        }

        @SuppressWarnings("unused")
        public MappingTransportableToGenericRecordException(final String description, final Exception cause) {
            super(description, cause);
        }
    }
}
