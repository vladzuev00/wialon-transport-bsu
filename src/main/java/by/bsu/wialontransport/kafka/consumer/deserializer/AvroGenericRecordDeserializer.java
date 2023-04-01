package by.bsu.wialontransport.kafka.consumer.deserializer;

import by.bsu.wialontransport.kafka.consumer.deserializer.exception.AvroGenericRecordDeserializationException;
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
    private Schema schema;

    @Override
    public void configure(final Map<String, ?> configurationProperties, final boolean isKey) {
        this.schema = (Schema) configurationProperties.get(SCHEMA.getName());
    }

    @Override
    public GenericRecord deserialize(final String topic, final byte[] bytes) {
        try {
            if (bytes == null) {
                return null;
            }
            final DatumReader<GenericRecord> datumReader = new GenericDatumReader<>(this.schema);
            final Decoder decoder = DecoderFactory.get()
                    .binaryDecoder(bytes, null);
            return datumReader.read(null, decoder);
        } catch (final IOException cause) {
            throw new AvroGenericRecordDeserializationException(cause);
        }
    }
}
