package by.bsu.wialontransport.base.containerinitializer;

import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.utility.DockerImageName;

import java.util.Map;

import static org.testcontainers.utility.DockerImageName.parse;

public final class KafkaContainerInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {
    private static final String TEST_PROPERTY_KEY_BOOTSTRAP_SERVERS = "spring.kafka.bootstrap-servers";

    private static final String FULL_IMAGE_NAME = "confluentinc/cp-kafka:5.4.3";
    private static final DockerImageName DOCKER_IMAGE_NAME = parse(FULL_IMAGE_NAME);

    private static final KafkaContainer kafkaContainer = new KafkaContainer(DOCKER_IMAGE_NAME);

    static {
        kafkaContainer.start();
    }

    @Override
    public void initialize(final ConfigurableApplicationContext context) {
        TestPropertyValues.of(
                Map.of(
                        TEST_PROPERTY_KEY_BOOTSTRAP_SERVERS, kafkaContainer.getBootstrapServers()
                )
        ).applyTo(context.getEnvironment());
    }
}
