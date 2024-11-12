package by.bsu.wialontransport.kafka.producer;

import by.bsu.wialontransport.kafka.model.transportable.Transportable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.kafka.core.KafkaTemplate;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public final class KafkaProducerTest {
    private static final String GIVEN_TOPIC_NAME = "topic-name";

    @Mock
    private KafkaTemplate<String, TestValue> mockedKafkaTemplate;

    private TestKafkaProducer producer;

    @Before
    public void initializeProducer() {
        producer = new TestKafkaProducer(mockedKafkaTemplate, GIVEN_TOPIC_NAME);
    }

    @Test
    public void recordShouldBeSent() {
        final Long givenId = 255L;
        final String givenPhoneNumber = "447336934";
        final String givenText = "text";
        final TestSource givenSource = new TestSource(givenId, givenPhoneNumber, givenText);

        producer.produce(givenSource);

        final TestValue expectedValue = new TestValue(givenId, givenPhoneNumber, givenText);
        final ProducerRecord<String, TestValue> expectedProducerRecord = new ProducerRecord<>(
                GIVEN_TOPIC_NAME,
                givenPhoneNumber,
                expectedValue
        );
        verify(mockedKafkaTemplate, times(1)).send(eq(expectedProducerRecord));
    }

    @Value
    @AllArgsConstructor
    @Builder
    private static class TestSource {
        Long id;
        String phoneNumber;
        String text;
    }

    @Value
    @AllArgsConstructor
    @Builder
    private static class TestValue {
        Long id;
        String phoneNumber;
        String text;
    }

    @Value
    @AllArgsConstructor
    @Builder
    private static class TestTransportable implements Transportable<String> {
        Long id;
        String phoneNumber;
        String text;

        @Override
        public String findTopicKey() {
            return phoneNumber;
        }
    }

    private static final class TestKafkaProducer extends KafkaProducer<String, TestValue, TestTransportable, TestSource> {

        public TestKafkaProducer(final KafkaTemplate<String, TestValue> kafkaTemplate, final String topicName) {
            super(kafkaTemplate, topicName);
        }

        @Override
        protected TestTransportable mapToTransportable(final TestSource source) {
            return TestTransportable.builder()
                    .id(source.getId())
                    .phoneNumber(source.getPhoneNumber())
                    .text(source.getText())
                    .build();
        }

        @Override
        protected TestValue mapToValue(final TestTransportable source) {
            return TestValue.builder()
                    .id(source.getId())
                    .phoneNumber(source.getPhoneNumber())
                    .text(source.getText())
                    .build();
        }
    }
}
