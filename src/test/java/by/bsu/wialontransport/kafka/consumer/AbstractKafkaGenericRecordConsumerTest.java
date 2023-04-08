package by.bsu.wialontransport.kafka.consumer;

import lombok.RequiredArgsConstructor;
import org.apache.avro.Schema;
import org.apache.avro.generic.GenericRecord;
import org.junit.Test;

import java.time.LocalDateTime;

import static by.bsu.wialontransport.kafka.consumer.AbstractKafkaGenericRecordConsumer.*;
import static java.time.ZoneOffset.UTC;
import static org.junit.Assert.assertEquals;

public final class AbstractKafkaGenericRecordConsumerTest {

    @Test
    public void valueShouldBeExtractedFromGenericRecord() {
        final int givenValue = 5;
        final GenericRecord givenGenericRecord = new TestGenericRecord(givenValue);

        final String givenGenericRecordKey = "key";

        final int actual = extractValue(givenGenericRecord, givenGenericRecordKey);
        assertEquals(givenValue, actual);
    }

    @Test
    public void dateTimeShouldBeExtractedFromGenericRecord() {
        final LocalDateTime givenDateTime = LocalDateTime.of(
                2023, 4, 8, 18, 25, 2);
        final long givenEpochSeconds = givenDateTime.toEpochSecond(UTC);
        final GenericRecord givenGenericRecord = new TestGenericRecord(givenEpochSeconds);

        final String givenGenericRecordKey = "key";

        final LocalDateTime actual = extractDateTime(givenGenericRecord, givenGenericRecordKey);
        assertEquals(givenDateTime, actual);
    }

    @Test
    public void stringShouldBeExtractedFromGenericRecord() {
        final String givenValue = "value";
        final GenericRecord givenGenericRecord = new TestGenericRecord(givenValue);

        final String givenGenericRecordKey = "key";

        final String actual = extractString(givenGenericRecord, givenGenericRecordKey);
        assertEquals(givenValue, actual);
    }

    @Test
    public void charShouldBeExtractedFromGenericRecord() {
        final char givenChar = 'c';
        final GenericRecord givenGenericRecord = new TestGenericRecord((int) givenChar);

        final String givenGenericRecordKey = "key";

        final char actual = extractChar(givenGenericRecord, givenGenericRecordKey);
        assertEquals(givenChar, actual);
    }

    @RequiredArgsConstructor
    private static final class TestGenericRecord implements GenericRecord {
        private final Object value;

        @Override
        public void put(final String key, final Object value) {
            throw new UnsupportedOperationException();
        }

        @Override
        public Object get(final String key) {
            return this.value;
        }

        @Override
        public void put(final int index, final Object value) {
            throw new UnsupportedOperationException();
        }

        @Override
        public Object get(final int index) {
            throw new UnsupportedOperationException();
        }

        @Override
        public Schema getSchema() {
            throw new UnsupportedOperationException();
        }
    }
}
