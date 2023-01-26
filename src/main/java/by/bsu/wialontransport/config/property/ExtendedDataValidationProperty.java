package by.bsu.wialontransport.config.property;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties
@ConfigurationProperties(prefix = "extended_data.validation")
@NoArgsConstructor
@Setter
@Getter
public class ExtendedDataValidationProperty {
    private int minValidDOP;
    private int maxValidDOP;
}
