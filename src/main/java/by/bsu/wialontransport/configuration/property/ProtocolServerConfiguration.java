package by.bsu.wialontransport.configuration.property;

import lombok.*;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties
@ConfigurationProperties(prefix = "wialon.server")
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@EqualsAndHashCode
@ToString
@Builder
public abstract class ProtocolServerConfiguration {
    private String host;
    private int port;
    private int threadCountProcessingConnection;
    private int threadCountProcessingData;
    private int connectionLifeTimeoutSeconds;
}
