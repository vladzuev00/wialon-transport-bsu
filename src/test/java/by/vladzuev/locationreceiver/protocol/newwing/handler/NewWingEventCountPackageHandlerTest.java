package by.vladzuev.locationreceiver.protocol.newwing.handler;

import by.vladzuev.locationreceiver.protocol.newwing.model.request.NewWingEventCountPackage;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public final class NewWingEventCountPackageHandlerTest {
    private final NewWingEventCountPackageHandler handler = new NewWingEventCountPackageHandler();

    @Test
    public void responseShouldBeCreated() {
        final NewWingEventCountPackage actual = handler.createResponse();
        final NewWingEventCountPackage expected = new NewWingEventCountPackage();
        assertEquals(expected, actual);
    }
}
