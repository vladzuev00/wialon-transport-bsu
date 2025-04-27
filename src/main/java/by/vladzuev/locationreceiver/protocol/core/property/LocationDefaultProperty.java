package by.vladzuev.locationreceiver.protocol.core.property;

import lombok.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.time.LocalDate;
import java.time.LocalTime;

@Value
@ConfigurationProperties("location.default")
public class LocationDefaultProperty {
    Double latitude;
    Double longitude;
    Integer course;
    Integer altitude;
    Double speed;
    Integer satelliteCount;
    Double hdop;
    Integer inputs;
    Integer outputs;
    String driverKeyCode;

    public LocalDate getDate() {
        return LocalDate.now();
    }

    public LocalTime getTime() {
        return LocalTime.now();
    }
}
