package by.bsu.wialontransport.config.property;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@Getter
public class DataValidationProperty {
    private int minValidAmountOfSatellites;
    private int maxValidAmountSatellite;
    private LocalDateTime minValidDateTime;
    private int deltaSecondsFromNowMaxAllowableValidDateTime;
    private int minValidDOP;
    private int maxValidDOP;
}