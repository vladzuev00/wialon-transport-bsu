package by.bsu.wialontransport.configuration.property.protocolserver;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@ConfigurationProperties(prefix = "protocol-server.new-wing")
public class NewWingProtocolServerConfiguration extends ProtocolServerConfiguration {

    @ConstructorBinding
    public NewWingProtocolServerConfiguration(final String host,
                                              final int port,
                                              final int threadCountProcessingConnection,
                                              final int threadCountProcessingData,
                                              final int connectionLifeTimeoutSeconds) {
        super(host, port, threadCountProcessingConnection, threadCountProcessingData, connectionLifeTimeoutSeconds);
    }

}
