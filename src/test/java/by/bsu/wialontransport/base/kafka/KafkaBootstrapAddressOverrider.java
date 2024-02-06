package by.bsu.wialontransport.base.kafka;

import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;

import static by.bsu.wialontransport.base.kafka.AbstractKafkaContainerTest.kafkaContainer;
import static org.springframework.test.context.support.TestPropertySourceUtils.addInlinedPropertiesToEnvironment;

public final class KafkaBootstrapAddressOverrider implements ApplicationContextInitializer<ConfigurableApplicationContext> {
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
