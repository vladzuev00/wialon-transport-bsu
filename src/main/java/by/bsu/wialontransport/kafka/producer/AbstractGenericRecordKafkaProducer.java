package by.bsu.wialontransport.kafka.producer;

import by.bsu.wialontransport.kafka.producer.exception.KafkaProducerMappingToGenericRecordException;
import org.apache.avro.Schema;
import org.apache.avro.generic.GenericDatumReader;
import org.apache.avro.generic.GenericRecord;
import org.apache.avro.io.*;
import org.apache.avro.reflect.ReflectDatumWriter;
import org.springframework.kafka.core.KafkaTemplate;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public abstract class AbstractGenericRecordKafkaProducer<K, TRANSPORTABLE, SOURCE>
        extends AbstractKafkaProducer<K, GenericRecord, TRANSPORTABLE, SOURCE> {
    private final Schema schema;

    public AbstractGenericRecordKafkaProducer(final KafkaTemplate<K, GenericRecord> kafkaTemplate,
                                              final String topicName,
                                              final Schema schema) {
        super(kafkaTemplate, topicName);
        this.schema = schema;
    }

    @Override
    protected final GenericRecord mapToValue(final TRANSPORTABLE mapped) {
        try (final ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            final ReflectDatumWriter<TRANSPORTABLE> datumWriter = new ReflectDatumWriter<>(this.schema);
            final BinaryEncoder encoder = EncoderFactory.get()
                    .binaryEncoder(outputStream, null);
            datumWriter.write(mapped, encoder);
            encoder.flush();
            final DatumReader<GenericRecord> datumReader = new GenericDatumReader<>(this.schema);
            final BinaryDecoder decoder = DecoderFactory.get().binaryDecoder(outputStream.toByteArray(), null);
            return datumReader.read(null, decoder);
        } catch (final IOException cause) {
            throw new KafkaProducerMappingToGenericRecordException(cause);
        }
    }
}
