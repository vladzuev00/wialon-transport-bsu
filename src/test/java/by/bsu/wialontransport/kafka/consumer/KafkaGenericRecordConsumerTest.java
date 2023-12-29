package by.bsu.wialontransport.kafka.consumer;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Value;
import org.apache.avro.generic.GenericRecord;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.junit.Test;

import java.time.LocalDateTime;
import java.util.List;

import static java.lang.Integer.MAX_VALUE;
import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.same;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public final class KafkaGenericRecordConsumerTest {
    private static final String GENERIC_RECORD_KEY_ID = "id";
    private static final String GENERIC_RECORD_KEY_PHONE_NUMBER = "phoneNumber";
    private static final String GENERIC_RECORD_KEY_TEXT = "text";
    private static final String GENERIC_RECORD_KEY_EPOCH_SECONDS = "epochSeconds";

    private static final String GIVEN_TOPIC = "test-topic";
    private static final int GIVEN_PARTITION = MAX_VALUE;
    private static final int GIVEN_OFFSET = MAX_VALUE;


    private final TestKafkaGenericRecordConsumer consumer = new TestKafkaGenericRecordConsumer();

    @Test
    public void recordsShouldBeConsumed() {
        final List<ConsumerRecord<String, GenericRecord>> givenRecords = List.of(
                createConsumerRecord(255L, "447336934", "text-1", 1703844174),
                createConsumerRecord(256L, "447336935", "text-2", 1703845174),
                createConsumerRecord(257L, "447336936", "text-3", 1703846174)
        );

        consumer.consume(givenRecords);

        final List<TestSource> actual = consumer.getProcessedSources();
        final List<TestSource> expected = List.of(
                new TestSource(255L, "447336934", "text-1", LocalDateTime.of(2023, 12, 29, 10, 2, 54)),
                new TestSource(256L, "447336935", "text-2", LocalDateTime.of(2023, 12, 29, 10, 19, 34)),
                new TestSource(257L, "447336936", "text-3", LocalDateTime.of(2023, 12, 29, 10, 36, 14))
        );
        assertEquals(expected, actual);
    }

    private static ConsumerRecord<String, GenericRecord> createConsumerRecord(final Long id,
                                                                              final String phoneNumber,
                                                                              final String text,
                                                                              final long epochSeconds) {
        final GenericRecord genericRecord = createGenericRecord(id, phoneNumber, text, epochSeconds);
        return new ConsumerRecord<>(GIVEN_TOPIC, GIVEN_PARTITION, GIVEN_OFFSET, phoneNumber, genericRecord);
    }

    private static GenericRecord createGenericRecord(final Long id,
                                                     final String phoneNumber,
                                                     final String text,
                                                     final long epochSeconds) {
        final GenericRecord record = mock(GenericRecord.class);
        when(record.get(same(GENERIC_RECORD_KEY_ID))).thenReturn(id);
        when(record.get(same(GENERIC_RECORD_KEY_PHONE_NUMBER))).thenReturn(phoneNumber);
        when(record.get(same(GENERIC_RECORD_KEY_TEXT))).thenReturn(text);
        when(record.get(same(GENERIC_RECORD_KEY_EPOCH_SECONDS))).thenReturn(epochSeconds);
        return record;
    }

    @Value
    @AllArgsConstructor
    @Builder
    private static class TestSource {
        Long id;
        String phoneNumber;
        String text;
        LocalDateTime dateTime;
    }

    @Getter
    private static final class TestKafkaGenericRecordConsumer extends KafkaGenericRecordConsumer<String, TestSource> {
        private List<TestSource> processedSources;

        @Override
        protected TestSource mapToSource(final GenericRecord record) {
            return TestSource.builder()
                    .id(extractId(record))
                    .phoneNumber(extractPhoneNumber(record))
                    .text(extractText(record))
                    .dateTime(extractDateTime(record))
                    .build();
        }

        @Override
        protected void process(final List<TestSource> sources) {
            processedSources = sources;
        }

        private static Long extractId(final GenericRecord record) {
            return extractValue(record, GENERIC_RECORD_KEY_ID, Long.class);
        }

        private static String extractPhoneNumber(final GenericRecord record) {
            return extractString(record, GENERIC_RECORD_KEY_PHONE_NUMBER);
        }

        private static String extractText(final GenericRecord record) {
            return extractString(record, GENERIC_RECORD_KEY_TEXT);
        }

        private static LocalDateTime extractDateTime(final GenericRecord record) {
            return extractDateTime(record, GENERIC_RECORD_KEY_EPOCH_SECONDS);
        }
    }
}
