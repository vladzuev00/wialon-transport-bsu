package by.vladzuev.locationreceiver.protocol.core.handler.packages.login;

import by.vladzuev.locationreceiver.crud.dto.Tracker;
import by.vladzuev.locationreceiver.protocol.core.model.LoginPackage;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;

public final class UnprotectedLoginPackageHandlerTest {
    private final TestUnprotectedLoginPackageHandler handler = new TestUnprotectedLoginPackageHandler();

    @Test
    public void loginShouldBeSuccess() {
        final Tracker givenTracker = Tracker.builder().build();
        final LoginPackage givenRequest = mock(LoginPackage.class);

        final Optional<Object> optionalActual = handler.loginCreatingFailedResponse(givenTracker, givenRequest);
        assertTrue(optionalActual.isEmpty());
    }

    private static final class TestUnprotectedLoginPackageHandler extends UnprotectedLoginPackageHandler<LoginPackage> {

        public TestUnprotectedLoginPackageHandler() {
            super(LoginPackage.class, null, null, null, null, null);
        }

        @Override
        protected Object createNoSuchImeiResponse() {
            throw new UnsupportedOperationException();
        }

        @Override
        protected void onSuccess() {
            throw new UnsupportedOperationException();
        }

        @Override
        protected Object createSuccessResponse() {
            throw new UnsupportedOperationException();
        }
    }
}
