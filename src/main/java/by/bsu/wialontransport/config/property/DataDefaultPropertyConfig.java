package by.bsu.wialontransport.config.property;

import by.bsu.wialontransport.validation.annotation.Hdop;
import lombok.*;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;

@RequiredArgsConstructor
@Getter
@EqualsAndHashCode
@ToString
@Builder
@ConstructorBinding
@ConfigurationProperties("tracker-server.data-default-property")
public final class DataDefaultPropertyConfig {

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

    @Hdop
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
