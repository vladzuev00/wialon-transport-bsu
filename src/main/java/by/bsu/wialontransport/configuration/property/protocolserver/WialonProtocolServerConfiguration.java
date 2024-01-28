package by.bsu.wialontransport.configuration.property.protocolserver;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

@ConstructorBinding
@ConfigurationProperties(prefix = "protocol-server.new-wing")
public class WialonProtocolServerConfiguration extends ProtocolServerConfiguration {

    public WialonProtocolServerConfiguration(final String host,
                                             final int port,
                                             final int threadCountProcessingConnection,
                                             final int threadCountProcessingData,
                                             final int connectionLifeTimeoutSeconds) {
        super(host, port, threadCountProcessingConnection, threadCountProcessingData, connectionLifeTimeoutSeconds);
    }

}
