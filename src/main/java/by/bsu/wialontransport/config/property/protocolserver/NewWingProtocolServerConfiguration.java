package by.bsu.wialontransport.config.property.protocolserver;

import by.bsu.wialontransport.validation.annotation.Host;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;
import org.springframework.validation.annotation.Validated;

//TODO: add validation
@ConfigurationProperties(prefix = "protocol-server.new-wing")
public class NewWingProtocolServerConfiguration extends ProtocolServerConfiguration {

    //TODO: delete @ConstructorBinding
    @ConstructorBinding
    public NewWingProtocolServerConfiguration(final String host,
                                              final int port,
                                              final int threadCountProcessingConnection,
                                              final int threadCountProcessingData,
                                              final int connectionLifeTimeoutSeconds) {
        super(host, port, threadCountProcessingConnection, threadCountProcessingData, connectionLifeTimeoutSeconds);
    }

}
