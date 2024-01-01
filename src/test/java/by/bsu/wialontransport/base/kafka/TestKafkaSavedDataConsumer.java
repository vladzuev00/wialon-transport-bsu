package by.bsu.wialontransport.base.kafka;

import lombok.Getter;
import org.apache.avro.generic.GenericRecord;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.CountDownLatch;

import static java.util.stream.Collectors.joining;

@Component
@Getter
public final class TestKafkaSavedDataConsumer {
    private static final int RESET_LATCH_COUNT = 1;
    private static final String CONSUMER_RECORDS_SEPARATOR_IN_PAYLOADS = " || ";

    private CountDownLatch countDownLatch;
    private String payload;

    public TestKafkaSavedDataConsumer() {
        this.reset();
    }

    @KafkaListener(
            topics = "${kafka.topic.saved-data.name}",
            groupId = "${kafka.topic.saved-data.consumer.group-id}",
            containerFactory = "listenerContainerFactorySavedData"
    )
    public void consume(final List<ConsumerRecord<Long, GenericRecord>> consumerRecords) {
        this.payload = convertToPayload(consumerRecords);
        this.countDownLatch.countDown();
    }

    public void reset() {
        this.countDownLatch = new CountDownLatch(RESET_LATCH_COUNT);
    }

    private static String convertToPayload(final List<ConsumerRecord<Long, GenericRecord>> consumerRecords) {
        return consumerRecords.stream()
                .map(ConsumerRecord::toString)
                .collect(joining(CONSUMER_RECORDS_SEPARATOR_IN_PAYLOADS));
    }
}
