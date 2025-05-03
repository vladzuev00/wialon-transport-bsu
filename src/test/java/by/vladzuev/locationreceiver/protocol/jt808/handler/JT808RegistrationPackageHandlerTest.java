package by.vladzuev.locationreceiver.protocol.jt808.handler;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertThrows;

public final class JT808RegistrationPackageHandlerTest {
    private final JT808RegistrationPackageHandler handler = new JT808RegistrationPackageHandler(
            null,
            null,
            null,
            null,
            null
    );

    @Test
    public void noSuchImeiResponseShouldBeCreated() {
        assertThrows(UnsupportedOperationException.class, handler::createNoSuchImeiResponse);
    }

    @Test
    public void successShouldBeHandled() {
        assertThrows(UnsupportedOperationException.class, handler::onSuccess);
    }

    @Test
    public void successResponseShouldBeCreated() {
        assertThrows(UnsupportedOperationException.class, handler::createSuccessResponse);
    }
}
