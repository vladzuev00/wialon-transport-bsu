package by.bsu.wialontransport.base.kafka;

import lombok.Getter;
import org.apache.avro.generic.GenericRecord;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.CountDownLatch;

import static java.lang.Thread.currentThread;
import static java.util.concurrent.TimeUnit.SECONDS;

@Component
public final class TestKafkaSavedDataConsumer {
    private static final String PAYLOAD_RECORDS_SEPARATOR = " || ";
    private static final int DEFAULT_CONSUMED_RECORDS_COUNT = 1;
    private static final int WAIT_CONSUMING_IN_SECONDS = 5;

    @Getter
    private volatile CountDownLatch countDownLatch;

    private volatile StringBuffer payloadBuffer;

    public TestKafkaSavedDataConsumer() {
        reset();
    }

    @KafkaListener(
            topics = "${kafka.topic.saved-data.name}",
            groupId = "${kafka.topic.saved-data.consumer.group-id}",
            containerFactory = "listenerContainerFactorySavedData"
    )
    public void consume(final List<ConsumerRecord<Long, GenericRecord>> records) {
        records.forEach(this::accumulate);
    }

    public void reset() {
        reset(DEFAULT_CONSUMED_RECORDS_COUNT);
    }

    public void reset(final int consumedRecordCount) {
        countDownLatch = new CountDownLatch(consumedRecordCount);
        payloadBuffer = new StringBuffer();
    }

    public String getPayload() {
        return payloadBuffer.toString();
    }

    public boolean isSuccessConsuming() {
        try {
            return countDownLatch.await(WAIT_CONSUMING_IN_SECONDS, SECONDS);
        } catch (final InterruptedException exception) {
            currentThread().interrupt();
            return false;
        }
    }

    private void accumulate(final ConsumerRecord<Long, GenericRecord> record) {
        payloadBuffer.append(record.toString());
        if (countDownLatch.getCount() > 1) {
            payloadBuffer.append(PAYLOAD_RECORDS_SEPARATOR);
        }
        countDownLatch.countDown();
    }
}
