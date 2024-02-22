package by.bsu.wialontransport.config.property;

import lombok.*;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

//TODO: add validation
@Configuration
@EnableConfigurationProperties
@ConfigurationProperties(prefix = "tracker-server.data-default-property")
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@EqualsAndHashCode
@ToString
@Builder
public class DataDefaultPropertyConfiguration {
    private int course;
    private int altitude;
    private double speed;
    private int amountOfSatellites;
    private double hdop;
    private int inputs;
    private int outputs;
    private String driverKeyCode;
}
