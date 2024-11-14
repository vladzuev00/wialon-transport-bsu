package by.bsu.wialontransport.protocol.wialon.handler.location;

import by.bsu.wialontransport.protocol.wialon.model.packages.location.response.WialonResponseSingleLocationPackage;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static by.bsu.wialontransport.protocol.wialon.model.packages.location.response.WialonResponseSingleLocationPackage.Status.PACKAGE_FIX_SUCCESS;

public final class WialonRequestSingleLocationPackageHandlerTest {
    private final WialonRequestSingleLocationPackageHandler handler = new WialonRequestSingleLocationPackageHandler(
            null,
            null,
            null,
            null
    );

    @Test
    public void responseShouldBeCreated() {
        final int givenLocationCount = 1;

        final WialonResponseSingleLocationPackage actual = handler.createResponse(givenLocationCount);
        final var expected = new WialonResponseSingleLocationPackage(PACKAGE_FIX_SUCCESS);
        Assertions.assertEquals(expected, actual);
    }
}
