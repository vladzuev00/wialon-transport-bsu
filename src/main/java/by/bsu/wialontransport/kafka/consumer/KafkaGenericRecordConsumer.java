package by.bsu.wialontransport.kafka.consumer;

import org.apache.avro.generic.GenericRecord;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

import static java.time.LocalDateTime.ofEpochSecond;
import static java.time.ZoneOffset.UTC;

public abstract class KafkaGenericRecordConsumer<K, SOURCE> extends KafkaConsumer<K, GenericRecord, SOURCE> {
    private static final ZoneOffset ZONE_OFFSET = UTC;

    protected static <T> T extractValue(final GenericRecord record, final String key, final Class<T> type) {
        return type.cast(record.get(key));
    }

    protected static LocalDateTime extractDateTime(final GenericRecord record, final String key) {
        final long epochSeconds = extractValue(record, key, Long.class);
        return ofEpochSecond(epochSeconds, 0, ZONE_OFFSET);
    }

    protected static String extractString(final GenericRecord record, final String key) {
        return extractValue(record, key, CharSequence.class).toString();
    }
}
