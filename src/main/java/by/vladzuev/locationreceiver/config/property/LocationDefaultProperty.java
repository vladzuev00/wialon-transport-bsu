package by.vladzuev.locationreceiver.config.property;

import by.vladzuev.locationreceiver.validation.annotation.*;
import lombok.*;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

import java.time.LocalDate;
import java.time.LocalTime;

@RequiredArgsConstructor
@Getter
@EqualsAndHashCode
@ToString
@Builder
@ConstructorBinding
@ConfigurationProperties("tracker-server.data-default-property")
public final class LocationDefaultProperty {

    @Course
    private final Integer course;

    @Altitude
    private final Integer altitude;

    @Speed
    private final Double speed;

    @AmountOfSatellites
    private final Integer satelliteCount;

    @Hdop
    private final Double hdop;

    @Inputs
    private final Integer inputs;

    @Outputs
    private final Integer outputs;

    @DriverKeyCode
    private final String driverKeyCode;

    public LocalDate getDate() {
        return LocalDate.now();
    }

    public LocalTime getTime() {
        return LocalTime.now();
    }

    public double getLatitude() {
        return Double.MIN_VALUE;
    }

    public double getLongitude() {
        return Double.MIN_VALUE;
    }
}
