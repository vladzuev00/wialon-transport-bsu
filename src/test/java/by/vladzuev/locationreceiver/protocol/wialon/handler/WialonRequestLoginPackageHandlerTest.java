package by.vladzuev.locationreceiver.protocol.wialon.handler;

import by.vladzuev.locationreceiver.protocol.wialon.model.login.WialonResponseLoginPackage;
import org.junit.jupiter.api.Test;

import static by.vladzuev.locationreceiver.protocol.wialon.model.login.WialonResponseLoginPackage.Status.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

public final class WialonRequestLoginPackageHandlerTest {
    private final WialonRequestLoginPackageHandler handler = new WialonRequestLoginPackageHandler(
            null,
            null,
            null,
            null,
            null,
            null
    );

    @Test
    public void noSuchImeiResponseShouldBeCreated() {
        final WialonResponseLoginPackage actual = handler.createNoSuchImeiResponse();
        final WialonResponseLoginPackage expected = new WialonResponseLoginPackage(CONNECTION_FAILURE);
        assertEquals(expected, actual);
    }

    @Test
    public void successResponseShouldBeCreated() {
        final WialonResponseLoginPackage actual = handler.createSuccessResponse();
        final WialonResponseLoginPackage expected = new WialonResponseLoginPackage(SUCCESS_AUTHORIZATION);
        assertEquals(expected, actual);
    }

    @Test
    public void wrongPasswordResponseShouldBeCreated() {
        final WialonResponseLoginPackage actual = handler.createWrongPasswordResponse();
        final WialonResponseLoginPackage expected = new WialonResponseLoginPackage(PASSWORD_ERROR);
        assertEquals(expected, actual);
    }
}
