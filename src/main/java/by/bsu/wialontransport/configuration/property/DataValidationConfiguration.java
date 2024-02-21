package by.bsu.wialontransport.configuration.property;

import lombok.*;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

//TODO: add validation
@Configuration
@EnableConfigurationProperties
@ConfigurationProperties(prefix = "data.validation")
@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@EqualsAndHashCode
@ToString
@Builder
public class DataValidationConfiguration {
    private int minValidAmountOfSatellites;
    private int maxValidAmountSatellites;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime minValidDateTime;

    private int deltaSecondsFromNowMaxAllowableValidDateTime;
}
