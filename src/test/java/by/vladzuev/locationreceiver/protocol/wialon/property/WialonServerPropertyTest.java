package by.vladzuev.locationreceiver.protocol.wialon.property;

import by.vladzuev.locationreceiver.base.AbstractSpringBootTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.assertEquals;

public final class WialonServerPropertyTest extends AbstractSpringBootTest {

    @Autowired
    private WialonServerProperty property;

    @Test
    public void propertyShouldBeCreated() {
        final WialonServerProperty expected = new WialonServerProperty(
                "localhost",
                6002,
                1,
                10,
                300
        );
        assertEquals(expected, property);
    }
}
