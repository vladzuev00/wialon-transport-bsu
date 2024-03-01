package by.bsu.wialontransport.config.property.protocolserver;

import by.bsu.wialontransport.base.AbstractSpringBootTest;
import lombok.Builder;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.Validator;

public final class ProtocolServerConfigTest extends AbstractSpringBootTest {

    @Autowired
    private Validator validator;

    @Test
    public void configShouldBeValid() {
        final NewWingProtocolServerConfig givenConfig = NewWingProtocolServerConfig.builder()
                .host("  ")
                .build();

        System.out.println(validator.validate(givenConfig).size());
        throw new RuntimeException();
    }

    private static final class TestProtocolServerConfig extends ProtocolServerConfig {

        @Builder
        public TestProtocolServerConfig(final String host,
                                        final int port,
                                        final int threadCountProcessingConnection,
                                        final int threadCountProcessingData,
                                        final int connectionLifeTimeoutSeconds) {
            super(host, port, threadCountProcessingConnection, threadCountProcessingData, connectionLifeTimeoutSeconds);
        }
    }
}
