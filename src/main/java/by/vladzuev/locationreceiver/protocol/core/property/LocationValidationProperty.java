package by.vladzuev.locationreceiver.protocol.core.property;

import lombok.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.time.Duration;
import java.time.LocalDateTime;

@Value
@ConfigurationProperties("location.validation")
public class LocationValidationProperty {
    Integer minSatelliteCount;
    Integer maxSatelliteCount;
    LocalDateTime minDateTime;
    Duration maxDateTimeDeltaFromNow;
}
