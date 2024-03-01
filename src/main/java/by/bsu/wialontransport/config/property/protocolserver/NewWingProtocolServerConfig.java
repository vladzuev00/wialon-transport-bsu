package by.bsu.wialontransport.config.property.protocolserver;

import lombok.Builder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;

//TODO: add validation
@Validated
@ConfigurationProperties(prefix = "protocol-server.new-wing")
public class NewWingProtocolServerConfig extends ProtocolServerConfig {

    //TODO: delete @ConstructorBinding
    @Builder
    @ConstructorBinding
    public NewWingProtocolServerConfig(@NotNull final String host,
                                       final int port,
                                       final int threadCountProcessingConnection,
                                       final int threadCountProcessingData,
                                       final int connectionLifeTimeoutSeconds) {
        super(host, port, threadCountProcessingConnection, threadCountProcessingData, connectionLifeTimeoutSeconds);
    }

}
