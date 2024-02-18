package by.bsu.wialontransport.base.kafka;

import org.apache.avro.generic.GenericRecord;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.Phaser;
import java.util.concurrent.TimeoutException;

import static java.lang.Thread.currentThread;
import static java.util.concurrent.TimeUnit.SECONDS;
import static java.util.stream.IntStream.rangeClosed;

@Component
public final class TestKafkaSavedDataConsumer {
    private static final String PAYLOAD_RECORDS_SEPARATOR = " || ";
    private static final int DEFAULT_CONSUMED_RECORDS_COUNT = 1;
    private static final int WAIT_CONSUMING_SECONDS = 5;

    private final Phaser phaser;
    private final StringBuffer payloadBuffer;

    public TestKafkaSavedDataConsumer() {
        phaser = createNotTerminatedPhaser(DEFAULT_CONSUMED_RECORDS_COUNT + 1);
        payloadBuffer = new StringBuffer();
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

    public void reset(final int consumedRecordsCount) {
        rangeClosed(1, phaser.getUnarrivedParties()).forEach(i -> phaser.arriveAndDeregister());
        phaser.bulkRegister(consumedRecordsCount + 1);
        payloadBuffer.delete(0, payloadBuffer.length());
    }

    public String getPayload() {
        return payloadBuffer.toString();
    }

    public boolean isSuccessConsuming() {
        try {
            final int phaseNumber = phaser.arriveAndDeregister();
            phaser.awaitAdvanceInterruptibly(phaseNumber, WAIT_CONSUMING_SECONDS, SECONDS);
            return true;
        } catch (final InterruptedException exception) {
            currentThread().interrupt();
            return false;
        } catch (final TimeoutException exception) {
            return false;
        }
    }

    @SuppressWarnings("SameParameterValue")
    private static Phaser createNotTerminatedPhaser(final int parties) {
        return new Phaser(parties) {
            @Override
            protected boolean onAdvance(final int phase, final int registeredParties) {
                return false;
            }
        };
    }

    private void accumulate(final ConsumerRecord<Long, GenericRecord> record) {
        payloadBuffer.append(record.toString());
        if (phaser.getUnarrivedParties() > 1) {
            payloadBuffer.append(PAYLOAD_RECORDS_SEPARATOR);
        }
        phaser.arriveAndDeregister();
    }
}
