package by.bsu.wialontransport.config.property.protocolserver;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

//TODO: add validation
@ConfigurationProperties(prefix = "protocol-server.wialon")
public class WialonProtocolServerConfig extends ProtocolServerConfig {

    //TODO: delete @ConstructorBinding
    @ConstructorBinding
    public WialonProtocolServerConfig(final String host,
                                      final int port,
                                      final int threadCountProcessingConnection,
                                      final int threadCountProcessingData,
                                      final int connectionLifeTimeoutSeconds) {
        super(host, port, threadCountProcessingConnection, threadCountProcessingData, connectionLifeTimeoutSeconds);
    }

}
