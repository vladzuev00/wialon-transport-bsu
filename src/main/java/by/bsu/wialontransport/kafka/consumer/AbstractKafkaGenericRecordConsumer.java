package by.bsu.wialontransport.kafka.consumer;

import org.apache.avro.generic.GenericRecord;

import java.time.LocalDateTime;

import static java.time.LocalDateTime.ofEpochSecond;
import static java.time.ZoneOffset.UTC;

public abstract class AbstractKafkaGenericRecordConsumer<K, DATA>
        extends AbstractKafkaConsumer<K, GenericRecord, DATA> {

    @SuppressWarnings("unchecked")
    protected static <T> T extractValue(final GenericRecord genericRecord, final String genericRecordKey) {
        return (T) genericRecord.get(genericRecordKey);
    }

    protected static LocalDateTime extractDateTime(final GenericRecord genericRecord, final String genericRecordKey) {
        final long epochSeconds = extractValue(genericRecord, genericRecordKey);
        return ofEpochSecond(epochSeconds, 0, UTC);
    }

    protected static String extractString(final GenericRecord genericRecord, final String genericRecordKey) {
        return extractValue(genericRecord, genericRecordKey).toString();
    }

    protected static char extractChar(final GenericRecord genericRecord, final String genericRecordKey) {
        final int charAsInt = extractValue(genericRecord, genericRecordKey);
        return (char) charAsInt;
    }
}
