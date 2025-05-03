package by.vladzuev.locationreceiver.protocol.wialon.handler.location;

import by.vladzuev.locationreceiver.protocol.wialon.model.location.request.WialonRequestLocationPackage;
import by.vladzuev.locationreceiver.protocol.wialon.model.location.response.WialonResponseSingleLocationPackage;
import org.junit.jupiter.api.Test;

import static by.vladzuev.locationreceiver.protocol.wialon.model.location.response.WialonResponseSingleLocationPackage.Status.PACKAGE_FIX_SUCCESS;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verifyNoInteractions;

public final class WialonRequestSingleLocationPackageHandlerTest {
    private final WialonRequestSingleLocationPackageHandler handler = new WialonRequestSingleLocationPackageHandler(
            null,
            null,
            null,
            null
    );

    @Test
    public void responseShouldBeCreated() {
        final WialonRequestLocationPackage givenRequest = mock(WialonRequestLocationPackage.class);

        final WialonResponseSingleLocationPackage actual = handler.createSuccessResponse(givenRequest);
        final var expected = new WialonResponseSingleLocationPackage(PACKAGE_FIX_SUCCESS);
        assertEquals(expected, actual);

        verifyNoInteractions(givenRequest);
    }
}
