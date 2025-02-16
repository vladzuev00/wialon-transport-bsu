package by.vladzuev.locationreceiver.protocol.wialon.property;

import by.vladzuev.locationreceiver.protocol.core.property.ProtocolServerProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "protocol-server.wialon")
public final class WialonServerProperty extends ProtocolServerProperty {

    public WialonServerProperty(final String host,
                                final Integer port,
                                final Integer threadCountProcessingConnection,
                                final Integer threadCountProcessingData,
                                final Integer connectionLifeTimeoutSeconds) {
        super(host, port, threadCountProcessingConnection, threadCountProcessingData, connectionLifeTimeoutSeconds);
    }
}
