package by.vladzuev.locationreceiver.kafka.consumer;

import org.apache.avro.generic.GenericRecord;
import org.junit.Test;

import java.time.LocalDateTime;

import static by.vladzuev.locationreceiver.kafka.consumer.KafkaGenericRecordConsumer.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.mockito.ArgumentMatchers.same;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public final class KafkaGenericRecordConsumerTest {

    @Test
    public void valueShouldBeExtracted() {
        final String givenKey = "key-1";
        final Integer givenValue = 5;
        final GenericRecord givenRecord = createRecordWithOneProperty(givenKey, givenValue);

        final Integer actual = extractValue(givenRecord, givenKey, Integer.class);
        assertSame(givenValue, actual);
    }

    @Test(expected = ClassCastException.class)
    public void valueShouldNotBeExtractedBecauseOfNotSuitableType() {
        final String givenKey = "key-1";
        final Integer givenValue = 5;
        final GenericRecord givenRecord = createRecordWithOneProperty(givenKey, givenValue);

        extractValue(givenRecord, givenKey, Boolean.class);
    }

    @Test
    public void dateTimeShouldBeExtracted() {
        final String givenKey = "key-2";
        final long givenEpochSeconds = 1703844174;
        final GenericRecord givenRecord = createRecordWithOneProperty(givenKey, givenEpochSeconds);

        final LocalDateTime actual = extractDateTime(givenRecord, givenKey);
        final LocalDateTime expected = LocalDateTime.of(2023, 12, 29, 10, 2, 54);
        assertEquals(expected, actual);
    }

    @Test(expected = ClassCastException.class)
    public void dateTimeShouldNotBeExtractedBecausePropertyWithGivenKeyIsNotDateTime() {
        final String givenKey = "key-2";
        final String givenValue = "text";
        final GenericRecord givenRecord = createRecordWithOneProperty(givenKey, givenValue);

        extractDateTime(givenRecord, givenKey);
    }

    @Test
    public void stringShouldBeExtracted() {
        final String givenKey = "key-2";
        final String givenValue = "text";
        final GenericRecord givenRecord = createRecordWithOneProperty(givenKey, givenValue);

        final String actual = extractString(givenRecord, givenKey);
        assertSame(givenValue, actual);
    }

    @Test(expected = ClassCastException.class)
    public void stringShouldNotBeExtractedBecausePropertyWithGivenKeyIsNotString() {
        final String givenKey = "key-2";
        final long givenValue = 1703844174;
        final GenericRecord givenRecord = createRecordWithOneProperty(givenKey, givenValue);

        extractString(givenRecord, givenKey);
    }

    private static GenericRecord createRecordWithOneProperty(final String key, final Object value) {
        final GenericRecord record = mock(GenericRecord.class);
        when(record.get(same(key))).thenReturn(value);
        return record;
    }
}
