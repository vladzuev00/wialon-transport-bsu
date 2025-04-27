package by.vladzuev.locationreceiver.protocol.core.property;

import by.vladzuev.locationreceiver.base.AbstractSpringBootTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.Duration;
import java.time.LocalDateTime;

import static java.time.temporal.ChronoUnit.SECONDS;

public final class LocationValidationPropertyTest extends AbstractSpringBootTest {

    @Autowired
    private LocationValidationProperty property;

    @Test
    public void propertyShouldBeCreated() {
        final LocationValidationProperty actual = new LocationValidationProperty(
                3,
                999,
                LocalDateTime.of(2010, 1, 1, 0, 0, 0),
                Duration.of(15, SECONDS)
        );
        Assertions.assertEquals(property, actual);
    }
}
