package by.vladzuev.locationreceiver.config.property;

import by.vladzuev.locationreceiver.validation.annotation.AmountOfSatellites;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import java.time.Duration;
import java.time.LocalDateTime;

@Getter
@EqualsAndHashCode
@ToString
@ConfigurationProperties("data.validation")
public final class LocationValidationProperty {

    @AmountOfSatellites
    private final Integer minSatelliteCount;

    @AmountOfSatellites
    private final Integer maxSatelliteCount;

    @NotNull
    @Past
    private final LocalDateTime minDateTime;

    @NotNull
    private final Duration maxDateTimeDeltaFromNow;

    @Builder
    @ConstructorBinding
    public LocationValidationProperty(final Integer minSatelliteCount,
                                      final Integer maxSatelliteCount,
                                      @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") final LocalDateTime minDateTime,
                                      final Duration maxDateTimeDeltaFromNow) {
        validateAmountOfSatellitesRange(minSatelliteCount, maxSatelliteCount);
        this.minSatelliteCount = minSatelliteCount;
        this.maxSatelliteCount = maxSatelliteCount;
        this.minDateTime = minDateTime;
        this.maxDateTimeDeltaFromNow = maxDateTimeDeltaFromNow;
    }

    private void validateAmountOfSatellitesRange(final Integer min, final Integer max) {
        if (min == null || max == null || min > max) {
            throw new IllegalArgumentException("Amount of satellites values aren't valid");
        }
    }
}
