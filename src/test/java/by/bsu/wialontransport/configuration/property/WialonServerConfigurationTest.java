package by.bsu.wialontransport.configuration.property;

import by.bsu.wialontransport.base.AbstractContextTest;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.Assert.assertEquals;

public final class WialonServerConfigurationTest extends AbstractContextTest {

    @Autowired
    private WialonServerConfiguration configuration;

    @Test
    public void configurationShouldBeInitialized() {
        final WialonServerConfiguration expected = WialonServerConfiguration.builder()
                .host("localhost")
                .port(6001)
                .amountThreadsToProcessConnection(1)
                .amountThreadsToProcessData(10)
                .aliveConnectionTimeoutSeconds(300)
                .build();
        assertEquals(expected, this.configuration);
    }
}
