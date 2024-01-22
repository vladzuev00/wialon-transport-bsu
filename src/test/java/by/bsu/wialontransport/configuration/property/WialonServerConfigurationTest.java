package by.bsu.wialontransport.configuration.property;

import by.bsu.wialontransport.base.AbstractContextTest;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.Assert.assertEquals;

public final class WialonServerConfigurationTest extends AbstractContextTest {

    @Autowired
    private ProtocolServerConfiguration configuration;

    @Test
    public void configurationShouldBeInitialized() {
//        final ProtocolServerConfiguration expected = ProtocolServerConfiguration.builder()
//                .host("localhost")
//                .port(6001)
//                .threadCountProcessingConnection(1)
//                .threadCountProcessingData(10)
//                .connectionLifeTimeoutSeconds(300)
//                .build();
//        assertEquals(expected, this.configuration);
    }
}
