package by.vladzuev.locationreceiver.protocol.core.property;

import by.vladzuev.locationreceiver.base.AbstractSpringBootTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.assertEquals;

//TODO: test LocationDefaultProperty::getDate LocationDefaultProperty::getTime
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
                "test-driver"
        );
        assertEquals(expected, property);
    }
}
