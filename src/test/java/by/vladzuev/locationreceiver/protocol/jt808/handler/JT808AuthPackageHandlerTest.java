package by.vladzuev.locationreceiver.protocol.jt808.handler;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

public final class JT808AuthPackageHandlerTest {
    private final JT808AuthPackageHandler handler = new JT808AuthPackageHandler();

    @Test
    public void responseShouldBeCreated() {
        assertThrows(UnsupportedOperationException.class, handler::createResponse);
    }
}
