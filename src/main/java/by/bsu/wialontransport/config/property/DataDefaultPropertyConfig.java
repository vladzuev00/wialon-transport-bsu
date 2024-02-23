package by.bsu.wialontransport.config.property;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Setter
@Getter
@EqualsAndHashCode
@ToString
@ConfigurationProperties("tracker-server.data-default-property")
public class DataDefaultPropertyConfig {
    private Integer course;
    private Integer altitude;
    private Double speed;
    private Integer amountOfSatellites;
    private Double hdop;
    private Integer inputs;
    private Integer outputs;
    private String driverKeyCode;
}
