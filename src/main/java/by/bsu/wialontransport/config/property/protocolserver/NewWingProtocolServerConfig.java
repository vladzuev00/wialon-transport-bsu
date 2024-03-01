package by.bsu.wialontransport.config.property.protocolserver;

import by.bsu.wialontransport.validation.annotation.Host;
import lombok.Builder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

//TODO: add validation
@ConfigurationProperties(prefix = "protocol-server.new-wing")
public class NewWingProtocolServerConfig extends ProtocolServerConfig {

    //TODO: delete @ConstructorBinding
    @Builder
    @ConstructorBinding
    public NewWingProtocolServerConfig(@Host final String host,
                                       final int port,
                                       final int threadCountProcessingConnection,
                                       final int threadCountProcessingData,
                                       final int connectionLifeTimeoutSeconds) {
        super(host, port, threadCountProcessingConnection, threadCountProcessingData, connectionLifeTimeoutSeconds);
    }

}
