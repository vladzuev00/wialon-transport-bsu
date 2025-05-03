package by.vladzuev.locationreceiver.protocol.wialon.handler.location;

import by.vladzuev.locationreceiver.protocol.wialon.model.location.request.WialonLocation;
import by.vladzuev.locationreceiver.protocol.wialon.model.location.request.WialonRequestBulkLocationPackage;
import by.vladzuev.locationreceiver.protocol.wialon.model.location.response.WialonResponseBulkLocationPackage;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public final class WialonRequestBulkLocationPackageHandlerTest {
    private final WialonRequestBulkLocationPackageHandler handler = new WialonRequestBulkLocationPackageHandler(
            null,
            null,
            null,
            null
    );

    @Test
    public void successResponseShouldBeCreated() {
        final WialonRequestBulkLocationPackage givenRequest = new WialonRequestBulkLocationPackage(
                List.of(
                        WialonLocation.builder().build(),
                        WialonLocation.builder().build(),
                        WialonLocation.builder().build(),
                        WialonLocation.builder().build(),
                        WialonLocation.builder().build(),
                        WialonLocation.builder().build()
                )
        );

        final WialonResponseBulkLocationPackage actual = handler.createSuccessResponse(givenRequest);
        final WialonResponseBulkLocationPackage expected = new WialonResponseBulkLocationPackage(6);
        assertEquals(expected, actual);
    }
}
