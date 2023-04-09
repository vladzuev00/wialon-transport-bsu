package by.bsu.wialontransport.configuration.property;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties
@ConfigurationProperties(prefix = "wialon.server")
@NoArgsConstructor
@Setter
@Getter
public class WialonServerConfiguration {
    private String host;
    private int port;
    private int amountThreadsToProcessConnection;
    private int amountThreadsToProcessData;
    private int aliveConnectionTimeoutSeconds;
}
