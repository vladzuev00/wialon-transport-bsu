package by.bsu.wialontransport.base;

import org.junit.ClassRule;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.utility.DockerImageName;

import static org.springframework.test.context.support.TestPropertySourceUtils.addInlinedPropertiesToEnvironment;
import static org.testcontainers.utility.DockerImageName.parse;

@ContextConfiguration(initializers = { AbstractKafkaContainerTest.KafkaBootstrapAddressOverrider.class })
public abstract class AbstractKafkaContainerTest extends AbstractContextTest {
    private static final String FULL_IMAGE_NAME = "confluentinc/cp-kafka:5.4.3";
    private static final DockerImageName DOCKER_IMAGE_NAME = parse(FULL_IMAGE_NAME);

    @ClassRule
    public static KafkaContainer kafkaContainer = new KafkaContainer(DOCKER_IMAGE_NAME);

    public static final class KafkaBootstrapAddressOverrider
            implements ApplicationContextInitializer<ConfigurableApplicationContext> {
        private static final String PROPERTY_KEY_BOOTSTRAP_SERVERS = "spring.kafka.bootstrap-servers";
        private static final String DELIMITER_PROPERTY_KEY_AND_VALUE = "=";

        @Override
        @SuppressWarnings("NullableProblems")
        public void initialize(final ConfigurableApplicationContext applicationContext) {
            addInlinedPropertiesToEnvironment(
                    applicationContext, findStringBootstrapPropertyKeyAndValue()
            );
        }

        private static String findStringBootstrapPropertyKeyAndValue() {
            return PROPERTY_KEY_BOOTSTRAP_SERVERS
                    + DELIMITER_PROPERTY_KEY_AND_VALUE
                    + kafkaContainer.getBootstrapServers();
        }
    }
}
