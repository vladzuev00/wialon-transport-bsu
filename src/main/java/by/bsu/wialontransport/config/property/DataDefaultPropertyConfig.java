package by.bsu.wialontransport.config.property;

import lombok.*;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

import javax.validation.constraints.*;

@RequiredArgsConstructor
@Getter
@EqualsAndHashCode
@ToString
@Builder
@ConfigurationProperties("tracker-server.data-default-property")
@ConstructorBinding
public class DataDefaultPropertyConfig {

    @NotNull
    private final Integer course;

    @NotNull
    private final Integer altitude;

    @NotNull
    @PositiveOrZero
    private final Double speed;

    @NotNull
    @Positive
    private final Integer amountOfSatellites;

    @NotNull
    @Min(1)
    @Max(10)
    private final Double hdop;

    @NotNull
    @PositiveOrZero
    private final Integer inputs;

    @NotNull
    @PositiveOrZero
    private final Integer outputs;

    @NotBlank
    private final String driverKeyCode;
}
