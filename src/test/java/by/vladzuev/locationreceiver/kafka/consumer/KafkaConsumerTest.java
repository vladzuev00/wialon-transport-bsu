package by.vladzuev.locationreceiver.kafka.consumer;

import lombok.Getter;
import lombok.Value;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.junit.Test;

import java.util.List;

import static java.lang.Integer.MAX_VALUE;
import static org.junit.Assert.assertEquals;

public final class KafkaConsumerTest {
    private static final String GIVEN_TOPIC = "topic-name";
    private static final int GIVEN_PARTITION = MAX_VALUE;
    private static final int GIVEN_OFFSET = MAX_VALUE;

    private final TestKafkaConsumer consumer = new TestKafkaConsumer();

    @Test
    public void recordsShouldBeConsumed() {
        final List<ConsumerRecord<String, TestValue>> givenRecords = List.of(
                createRecord(255L, "447336934", "text-1"),
                createRecord(256L, "447336935", "text-2"),
                createRecord(257L, "447336936", "text-3")
        );

        consumer.consume(givenRecords);

        final List<TestSource> actual = consumer.getProcessedSources();
        final List<TestSource> expected = List.of(
                new TestSource(255L, "447336934", "text-1"),
                new TestSource(256L, "447336935", "text-2"),
                new TestSource(257L, "447336936", "text-3")
        );
        assertEquals(expected, actual);
    }

    private static ConsumerRecord<String, TestValue> createRecord(final Long id,
                                                                  final String phoneNumber,
                                                                  final String text) {
        final TestValue value = new TestValue(id, phoneNumber, text);
        return new ConsumerRecord<>(GIVEN_TOPIC, GIVEN_PARTITION, GIVEN_OFFSET, phoneNumber, value);
    }

    @Value
    private static class TestValue {
        Long id;
        String phoneNumber;
        String text;
    }

    @Value
    private static class TestSource {
        Long id;
        String phoneNumber;
        String text;
    }

    @Getter
    private static final class TestKafkaConsumer extends KafkaConsumer<String, TestValue, TestSource> {
        private List<TestSource> processedSources;

        @Override
        protected TestSource mapToSource(final TestValue value) {
            return new TestSource(
                    value.getId(),
                    value.getPhoneNumber(),
                    value.getText()
            );
        }

        @Override
        protected void process(final List<TestSource> sources) {
            processedSources = sources;
        }
    }
}
