package by.bsu.wialontransport.protocol.newwing.handler.packages;

import by.bsu.wialontransport.protocol.core.model.packages.Package;
import by.bsu.wialontransport.protocol.newwing.model.packages.response.NewWingFailureResponsePackage;
import by.bsu.wialontransport.protocol.newwing.model.packages.response.NewWingSuccessResponsePackage;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public final class NewWingLoginPackageHandlerTest {
    private final NewWingLoginPackageHandler loginPackageHandler = new NewWingLoginPackageHandler(
            null,
            null,
            null,
            null
    );

    @Test
    public void noSuchImeiResponseShouldBeCreated() {
        final Package actual = loginPackageHandler.createNoSuchImeiResponse();
        final Package expected = new NewWingFailureResponsePackage();
        assertEquals(expected, actual);
    }

    @Test
    public void successResponseShouldBeCreated() {
        final Package actual = loginPackageHandler.createSuccessResponse();
        final Package expected = new NewWingSuccessResponsePackage();
        assertEquals(expected, actual);
    }
}
