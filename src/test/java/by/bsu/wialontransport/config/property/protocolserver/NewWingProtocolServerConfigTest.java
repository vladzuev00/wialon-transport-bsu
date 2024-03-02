package by.bsu.wialontransport.config.property.protocolserver;

import by.bsu.wialontransport.base.AbstractSpringBootTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.Assert.assertEquals;

public final class NewWingProtocolServerConfigTest extends AbstractSpringBootTest {

    @Autowired
    private NewWingProtocolServerConfig config;

    @Test
    public void configShouldBeCreated() {
        final NewWingProtocolServerConfig expected = NewWingProtocolServerConfig.builder()
                .host("localhost")
                .port(6001)
                .threadCountProcessingConnection(1)
                .threadCountProcessingData(10)
                .connectionLifeTimeoutSeconds(300)
                .build();
        assertEquals(expected, config);
    }
}
