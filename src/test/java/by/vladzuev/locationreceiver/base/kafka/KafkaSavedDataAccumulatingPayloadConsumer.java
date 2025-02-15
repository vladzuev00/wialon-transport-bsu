package by.vladzuev.locationreceiver.base.kafka;

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
public final class KafkaSavedDataAccumulatingPayloadConsumer {
    private static final String PAYLOAD_RECORD_SEPARATOR = " || ";
    private static final int DEFAULT_EXPECTED_RECORD_COUNT = 1;
    private static final int WAIT_CONSUMING_SECONDS = 5;

    private final Phaser phaser;
    private final StringBuffer payloadBuffer;

    public KafkaSavedDataAccumulatingPayloadConsumer() {
        phaser = createNotTerminatedPhaser(DEFAULT_EXPECTED_RECORD_COUNT + 1);
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

    public void expect(final int recordCount) {
        rangeClosed(1, phaser.getUnarrivedParties()).forEach(i -> phaser.arriveAndDeregister());
        phaser.bulkRegister(recordCount + 1);
        payloadBuffer.delete(0, payloadBuffer.length());
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

    public String getPayload() {
        return payloadBuffer.toString();
    }

    @SuppressWarnings("SameParameterValue")
    private Phaser createNotTerminatedPhaser(final int parties) {
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
            payloadBuffer.append(PAYLOAD_RECORD_SEPARATOR);
        }
        phaser.arriveAndDeregister();
    }
}
