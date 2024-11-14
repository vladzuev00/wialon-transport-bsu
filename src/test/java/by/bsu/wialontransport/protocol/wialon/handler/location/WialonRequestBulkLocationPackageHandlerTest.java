package by.bsu.wialontransport.protocol.wialon.handler.location;

import by.bsu.wialontransport.protocol.wialon.model.packages.location.response.WialonResponseBulkLocationPackage;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public final class WialonRequestBulkLocationPackageHandlerTest {
    private final WialonRequestBulkLocationPackageHandler handler = new WialonRequestBulkLocationPackageHandler(
            null,
            null,
            null,
            null
    );

    @Test
    public void responseShouldBeCreated() {
        final int givenLocationCount = 10;

        final WialonResponseBulkLocationPackage actual = handler.createResponse(givenLocationCount);
        final WialonResponseBulkLocationPackage expected = new WialonResponseBulkLocationPackage(givenLocationCount);
        assertEquals(expected, actual);
    }
}
