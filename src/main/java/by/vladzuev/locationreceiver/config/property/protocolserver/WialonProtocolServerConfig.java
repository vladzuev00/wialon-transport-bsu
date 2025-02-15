package by.vladzuev.locationreceiver.config.property.protocolserver;

import lombok.Builder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

@ConfigurationProperties(prefix = "protocol-server.wialon")
public final class WialonProtocolServerConfig extends ProtocolServerConfig {

    @Builder
    @ConstructorBinding
    public WialonProtocolServerConfig(final String host,
                                      final Integer port,
                                      final Integer threadCountProcessingConnection,
                                      final Integer threadCountProcessingData,
                                      final Integer connectionLifeTimeoutSeconds) {
        super(host, port, threadCountProcessingConnection, threadCountProcessingData, connectionLifeTimeoutSeconds);
    }
}
