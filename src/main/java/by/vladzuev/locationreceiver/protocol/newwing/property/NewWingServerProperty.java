package by.vladzuev.locationreceiver.protocol.newwing.property;

import by.vladzuev.locationreceiver.protocol.core.property.ProtocolServerProperty;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

@ConfigurationProperties(prefix = "protocol-server.new-wing")
public final class NewWingServerProperty extends ProtocolServerProperty {

    @ConstructorBinding
    public NewWingServerProperty(final String host,
                                 final Integer port,
                                 final Integer threadCountProcessingConnection,
                                 final Integer threadCountProcessingData,
                                 final Integer connectionLifeTimeoutSeconds) {
        super(host, port, threadCountProcessingConnection, threadCountProcessingData, connectionLifeTimeoutSeconds);
    }
}
