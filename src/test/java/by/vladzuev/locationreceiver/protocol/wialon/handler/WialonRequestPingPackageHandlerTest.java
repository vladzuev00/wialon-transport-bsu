package by.vladzuev.locationreceiver.protocol.wialon.handler;

import by.vladzuev.locationreceiver.protocol.wialon.model.ping.WialonResponsePingPackage;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public final class WialonRequestPingPackageHandlerTest {
    private final WialonRequestPingPackageHandler handler = new WialonRequestPingPackageHandler();

    @Test
    public void responseShouldBeCreated() {
        final WialonResponsePingPackage actual = handler.createResponse();
        assertNotNull(actual);
    }
}
