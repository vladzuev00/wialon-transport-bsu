package by.vladzuev.locationreceiver.protocol.jt808.handler;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

public final class JT808HeartBeatPackageHandlerTest {
    private final JT808HeartBeatPackageHandler handler = new JT808HeartBeatPackageHandler();

    @Test
    public void responseShouldBeCreated() {
        assertThrows(UnsupportedOperationException.class, handler::createResponse);
    }
}
