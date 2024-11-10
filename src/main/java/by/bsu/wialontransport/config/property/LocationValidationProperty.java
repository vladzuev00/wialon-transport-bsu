package by.bsu.wialontransport.config.property;

import by.bsu.wialontransport.validation.annotation.AmountOfSatellites;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Past;
import javax.validation.constraints.Positive;
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
    private final Integer getMaxSatelliteCount;

    @NotNull
    @Past
    private final LocalDateTime minDateTime;

    @NotNull
    private final Duration maxDateTimeDeltaFromNow;

    @Builder
    @ConstructorBinding
    public LocationValidationProperty(final Integer minSatelliteCount,
                                      final Integer getMaxSatelliteCount,
                                      @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") final LocalDateTime minDateTime,
                                      final Duration maxDateTimeDeltaFromNow) {
        validateAmountOfSatellitesRange(minSatelliteCount, getMaxSatelliteCount);
        this.minSatelliteCount = minSatelliteCount;
        this.getMaxSatelliteCount = getMaxSatelliteCount;
        this.minDateTime = minDateTime;
        this.maxDateTimeDeltaFromNow = maxDateTimeDeltaFromNow;
    }

    private void validateAmountOfSatellitesRange(final Integer min, final Integer max) {
        if (min == null || max == null || min > max) {
            throw new IllegalArgumentException("Amount of satellites values aren't valid");
        }
    }
}
