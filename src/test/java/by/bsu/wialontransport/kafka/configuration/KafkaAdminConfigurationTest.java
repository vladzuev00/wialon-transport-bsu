package by.bsu.wialontransport.kafka.configuration;

import org.junit.Test;
import org.springframework.kafka.core.KafkaAdmin;

import java.util.Map;

import static org.apache.kafka.clients.admin.AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG;
import static org.junit.Assert.assertEquals;

public final class KafkaAdminConfigurationTest {
    private final KafkaAdminConfiguration configuration = new KafkaAdminConfiguration();

    @Test
    public void adminShouldBeCreated() {
        final String givenBootstrapAddress = "value";

        final KafkaAdmin actual = configuration.kafkaAdmin(givenBootstrapAddress);
        final Map<String, Object> actualConfigurationByNames = actual.getConfigurationProperties();
        final Map<String, Object> expectedConfigurationByNames = Map.of(BOOTSTRAP_SERVERS_CONFIG, givenBootstrapAddress);
        assertEquals(expectedConfigurationByNames, actualConfigurationByNames);
    }
}
