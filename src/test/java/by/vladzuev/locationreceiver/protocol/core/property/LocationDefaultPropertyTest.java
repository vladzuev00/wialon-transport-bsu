package by.vladzuev.locationreceiver.protocol.core.property;

import by.vladzuev.locationreceiver.base.AbstractSpringBootTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public final class LocationDefaultPropertyTest extends AbstractSpringBootTest {

    @Autowired
    private LocationDefaultProperty property;

    @Test
    public void propertyShouldBeCreated() {
        final LocationDefaultProperty expected = new LocationDefaultProperty(
                0.,
                0.,
                0,
                0,
                0.,
                0,
                0.,
                0,
                0,
                "default-driver"
        );
        assertEquals(expected, property);
    }

    @Test
    public void dateShouldBeGot() {
        final LocalDate actual = property.getDate();
        assertNotNull(actual);
    }

    @Test
    public void timeShouldBeGot() {
        final LocalTime actual = property.getTime();
        assertNotNull(actual);
    }
}
