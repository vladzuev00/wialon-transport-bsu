package by.bsu.wialontransport.config.property.protocolserver;

import lombok.Builder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

@ConfigurationProperties(prefix = "protocol-server.new-wing")
public final class NewWingProtocolServerConfig extends ProtocolServerConfig {

    @Builder
    @ConstructorBinding
    public NewWingProtocolServerConfig(final String host,
                                       final Integer port,
                                       final Integer threadCountProcessingConnection,
                                       final Integer threadCountProcessingData,
                                       final Integer connectionLifeTimeoutSeconds) {
        super(host, port, threadCountProcessingConnection, threadCountProcessingData, connectionLifeTimeoutSeconds);
    }

}
