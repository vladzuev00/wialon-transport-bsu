package by.vladzuev.locationreceiver.protocol.newwing.property;

import by.vladzuev.locationreceiver.base.AbstractSpringBootTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

public final class NewWingServerPropertyTest extends AbstractSpringBootTest {

    @Autowired
    private NewWingServerProperty property;

    @Test
    public void propertyShouldBeCreated() {
        final NewWingServerProperty expected = new NewWingServerProperty(
                "localhost",
                6001,
                1,
                10,
                300
        );
        Assertions.assertEquals(expected, property);
    }
}
