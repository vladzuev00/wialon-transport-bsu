package by.bsu.wialontransport.protocol.wialon.handler.packages;

import by.bsu.wialontransport.protocol.core.model.packages.Package;
import by.bsu.wialontransport.protocol.wialon.model.packages.login.WialonResponseLoginPackage;
import org.junit.Test;

import static by.bsu.wialontransport.protocol.wialon.model.packages.login.WialonResponseLoginPackage.Status.*;
import static org.junit.Assert.assertEquals;

public final class WialonRequestLoginPackageHandlerTest {
    private final WialonRequestLoginPackageHandler handler = new WialonRequestLoginPackageHandler(
            null,
            null,
            null,
            null,
            null
    );

    @Test
    public void noSuchImeiResponseShouldBeCreated() {
        final Package actual = handler.createNoSuchImeiResponse();
        final Package expected = new WialonResponseLoginPackage(CONNECTION_FAILURE);
        assertEquals(expected, actual);
    }

    @Test
    public void successResponseShouldBeCreated() {
        final Package actual = handler.createSuccessResponse();
        final Package expected = new WialonResponseLoginPackage(SUCCESS_AUTHORIZATION);
        assertEquals(expected, actual);
    }

    @Test
    public void wrongPasswordResponseShouldBeCreated() {
        final Package actual = handler.createWrongPasswordResponse();
        final Package expected = new WialonResponseLoginPackage(ERROR_CHECK_PASSWORD);
        assertEquals(expected, actual);
    }
}
