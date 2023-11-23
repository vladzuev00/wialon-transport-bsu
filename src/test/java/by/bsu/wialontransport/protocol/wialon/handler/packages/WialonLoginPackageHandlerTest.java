package by.bsu.wialontransport.protocol.wialon.handler.packages;

import by.bsu.wialontransport.protocol.core.model.packages.Package;
import by.bsu.wialontransport.protocol.wialon.wialonpackage.login.WialonResponseLoginPackage;
import org.junit.Test;

import static by.bsu.wialontransport.protocol.wialon.wialonpackage.login.WialonResponseLoginPackage.Status.*;
import static org.junit.Assert.assertEquals;

public final class WialonLoginPackageHandlerTest {
    private final WialonLoginPackageHandler loginPackageHandler = new WialonLoginPackageHandler(
            null,
            null,
            null,
            null,
            null
    );

    @Test
    public void noSuchImeiResponseShouldBeCreated() {
        final Package actual = this.loginPackageHandler.createNoSuchImeiResponse();
        final Package expected = new WialonResponseLoginPackage(CONNECTION_FAILURE);
        assertEquals(expected, actual);
    }

    @Test
    public void successResponseShouldBeCreated() {
        final Package actual = this.loginPackageHandler.createSuccessResponse();
        final Package expected = new WialonResponseLoginPackage(SUCCESS_AUTHORIZATION);
        assertEquals(expected, actual);
    }

    @Test
    public void wrongPasswordResponseShouldBeCreated() {
        final Package actual = this.loginPackageHandler.createWrongPasswordResponse();
        final Package expected = new WialonResponseLoginPackage(ERROR_CHECK_PASSWORD);
        assertEquals(expected, actual);
    }
}
