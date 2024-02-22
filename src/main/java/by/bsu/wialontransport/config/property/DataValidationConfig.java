package by.bsu.wialontransport.config.property;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.*;
import java.time.LocalDateTime;

@Getter
@EqualsAndHashCode
@ToString
@ConfigurationProperties("data.validation")
public class DataValidationConfig {

    @Min(1)
    @Max(9999)
    private final Integer minValidAmountOfSatellites;

    @Min(1)
    @Max(9999)
    private final Integer maxValidAmountOfSatellites;

    @NotNull
    @Past
    private final LocalDateTime minValidDateTime;

    @NotNull
    @Positive
    private final Integer maxValidDateTimeDeltaSecondsFromNow;

    @Builder
    @ConstructorBinding
    public DataValidationConfig(final Integer minValidAmountOfSatellites,
                                final Integer maxValidAmountOfSatellites,
                                @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") final LocalDateTime minValidDateTime,
                                final Integer maxValidDateTimeDeltaSecondsFromNow) {
        checkAmountOfSatellitesValues(minValidAmountOfSatellites, maxValidAmountOfSatellites);
        this.minValidAmountOfSatellites = minValidAmountOfSatellites;
        this.maxValidAmountOfSatellites = maxValidAmountOfSatellites;
        this.minValidDateTime = minValidDateTime;
        this.maxValidDateTimeDeltaSecondsFromNow = maxValidDateTimeDeltaSecondsFromNow;
    }

    private static void checkAmountOfSatellitesValues(final Integer minValue, final Integer maxValue) {
        if (minValue == null || maxValue == null || minValue > maxValue) {
            throw new IllegalArgumentException("Amount of satellites values aren't valid");
        }
    }
}
