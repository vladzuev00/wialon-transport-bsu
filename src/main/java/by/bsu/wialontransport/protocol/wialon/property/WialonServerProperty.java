package by.bsu.wialontransport.protocol.wialon.property;

import by.bsu.wialontransport.protocol.core.property.ProtocolServerProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

@ConfigurationProperties(prefix = "protocol-server.wialon")
public final class WialonServerProperty extends ProtocolServerProperty {

    @ConstructorBinding
    public WialonServerProperty(final String host,
                                final Integer port,
                                final Integer threadCountProcessingConnection,
                                final Integer threadCountProcessingData,
                                final Integer connectionLifeTimeoutSeconds) {
        super(host, port, threadCountProcessingConnection, threadCountProcessingData, connectionLifeTimeoutSeconds);
    }
}
