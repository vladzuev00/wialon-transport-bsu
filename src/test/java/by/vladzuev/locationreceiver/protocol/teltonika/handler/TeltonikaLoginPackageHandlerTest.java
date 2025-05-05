package by.vladzuev.locationreceiver.protocol.teltonika.handler;

import by.vladzuev.locationreceiver.protocol.teltonika.model.login.TeltonikaResponseFailedLoginPackage;
import by.vladzuev.locationreceiver.protocol.teltonika.model.login.TeltonikaResponseSuccessLoginPackage;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public final class TeltonikaLoginPackageHandlerTest {
    private final TeltonikaLoginPackageHandler handler = new TeltonikaLoginPackageHandler(
            null,
            null,
            null,
            null,
            null
    );

    @Test
    public void noSuchImeiResponseShouldBeCreated() {
        final TeltonikaResponseFailedLoginPackage actual = handler.createNoSuchImeiResponse();
        final TeltonikaResponseFailedLoginPackage expected = new TeltonikaResponseFailedLoginPackage();
        assertEquals(expected, actual);
    }

    @Test
    public void successShouldBeHandled() {
        handler.onSuccess();
    }

    @Test
    public void successResponseShouldBeCreated() {
        final TeltonikaResponseSuccessLoginPackage actual = handler.createSuccessResponse();
        final TeltonikaResponseSuccessLoginPackage expected = new TeltonikaResponseSuccessLoginPackage();
        assertEquals(expected, actual);
    }
}
