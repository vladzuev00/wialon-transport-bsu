package by.bsu.wialontransport.protocol.wialon.handler.packages.data;

import by.bsu.wialontransport.protocol.wialon.wialonpackage.data.response.WialonResponseDataPackage;
import org.junit.Test;

import static by.bsu.wialontransport.protocol.wialon.wialonpackage.data.response.WialonResponseDataPackage.Status.ERROR_PACKAGE_STRUCTURE;
import static by.bsu.wialontransport.protocol.wialon.wialonpackage.data.response.WialonResponseDataPackage.Status.PACKAGE_FIX_SUCCESS;
import static org.junit.Assert.assertEquals;

public final class WialonRequestDataPackageHandlerTest {
    private final WialonRequestDataPackageHandler handler = new WialonRequestDataPackageHandler(
            null,
            null,
            null,
            null
    );

    @Test
    public void responseShouldBeCreatedWithSuccessStatus() {
        final int givenReceivedDataCount = 1;

        final WialonResponseDataPackage actual = handler.createResponse(givenReceivedDataCount);
        final WialonResponseDataPackage expected = new WialonResponseDataPackage(PACKAGE_FIX_SUCCESS);
        assertEquals(expected, actual);
    }

    @Test
    public void responseShouldBeCreatedWithErrorStatus() {
        final int givenReceivedDataCount = 5;

        final WialonResponseDataPackage actual = handler.createResponse(givenReceivedDataCount);
        final WialonResponseDataPackage expected = new WialonResponseDataPackage(ERROR_PACKAGE_STRUCTURE);
        assertEquals(expected, actual);
    }
}
