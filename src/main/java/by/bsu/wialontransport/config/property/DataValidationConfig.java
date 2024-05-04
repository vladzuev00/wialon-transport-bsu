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
import java.time.LocalDateTime;

@Getter
@EqualsAndHashCode
@ToString
@ConfigurationProperties("data.validation")
public final class DataValidationConfig {

    @AmountOfSatellites
    private final Integer minAmountOfSatellites;

    @AmountOfSatellites
    private final Integer maxAmountOfSatellites;

    @NotNull
    @Past
    private final LocalDateTime minDateTime;

    @NotNull
    @Positive
    private final Integer maxDateTimeDeltaSecondsFromNow;

    @Builder
    @ConstructorBinding
    public DataValidationConfig(final Integer minAmountOfSatellites,
                                final Integer maxAmountOfSatellites,
                                @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") final LocalDateTime minDateTime,
                                final Integer maxDateTimeDeltaSecondsFromNow) {
        validateAmountOfSatellitesRange(minAmountOfSatellites, maxAmountOfSatellites);
        this.minAmountOfSatellites = minAmountOfSatellites;
        this.maxAmountOfSatellites = maxAmountOfSatellites;
        this.minDateTime = minDateTime;
        this.maxDateTimeDeltaSecondsFromNow = maxDateTimeDeltaSecondsFromNow;
    }

    private void validateAmountOfSatellitesRange(final Integer min, final Integer max) {
        if (min == null || max == null || min > max) {
            throw new IllegalArgumentException("Amount of satellites values aren't valid");
        }
    }
}
