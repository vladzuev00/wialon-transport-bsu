package by.vladzuev.locationreceiver.protocol.newwing.handler;

import by.vladzuev.locationreceiver.protocol.newwing.model.response.NewWingFailureResponsePackage;
import by.vladzuev.locationreceiver.protocol.newwing.model.response.NewWingSuccessResponsePackage;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public final class NewWingLoginPackageHandlerTest {
    private final NewWingLoginPackageHandler handler = new NewWingLoginPackageHandler(
            null,
            null,
            null,
            null,
            null
    );

    @Test
    public void noSuchImeiResponseShouldBeCreated() {
        final NewWingFailureResponsePackage actual = handler.createNoSuchImeiResponse();
        final NewWingFailureResponsePackage expected = new NewWingFailureResponsePackage();
        assertEquals(expected, actual);
    }

    @Test
    public void successResponseShouldBeCreated() {
        final NewWingSuccessResponsePackage actual = handler.createSuccessResponse();
        final NewWingSuccessResponsePackage expected = new NewWingSuccessResponsePackage();
        assertEquals(expected, actual);
    }
}
