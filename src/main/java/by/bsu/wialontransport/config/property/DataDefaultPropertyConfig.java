package by.bsu.wialontransport.config.property;

import by.bsu.wialontransport.validation.annotation.*;
import lombok.*;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

@RequiredArgsConstructor
@Getter
@EqualsAndHashCode
@ToString
@Builder
@ConstructorBinding
@ConfigurationProperties("tracker-server.data-default-property")
public final class DataDefaultPropertyConfig {

    @Course
    private final Integer course;

    @Altitude
    private final Integer altitude;

    @Speed
    private final Double speed;

    @AmountOfSatellites
    private final Integer amountOfSatellites;

    @Hdop
    private final Double hdop;

    @Inputs
    private final Integer inputs;

    @Outputs
    private final Integer outputs;

    @DriverKeyCode
    private final String driverKeyCode;
}
