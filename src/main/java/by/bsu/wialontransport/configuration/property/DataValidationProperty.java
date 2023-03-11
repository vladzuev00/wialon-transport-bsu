package by.bsu.wialontransport.configuration.property;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Configuration
@EnableConfigurationProperties
@ConfigurationProperties(prefix = "data.validation")
@NoArgsConstructor
@Setter
@Getter
public class DataValidationProperty {
    private int minValidAmountOfSatellites;
    private int maxValidAmountSatellites;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime minValidDateTime;

    private int deltaSecondsFromNowMaxAllowableValidDateTime;
    private int minValidDOP;
    private int maxValidDOP;
}