package by.bsu.wialontransport.protocol.core.handler.packages.login;

import by.bsu.wialontransport.crud.dto.Tracker;
import by.bsu.wialontransport.protocol.core.model.packages.Package;
import by.bsu.wialontransport.protocol.core.model.packages.login.LoginPackage;
import org.junit.Test;

import java.util.Optional;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

public final class UnprotectedLoginPackageHandlerTest {
    private final UnprotectedLoginPackageHandler loginPackageHandler = new TestUnprotectedLoginPackageHandler();

    @Test
    public void loginPackageShouldBeCheckedWithoutCreatingResponseBecauseOfSuccess() {
        final Tracker givenTracker = Tracker.builder().build();
        final LoginPackage givenLoginPackage = mock(LoginPackage.class);

        final Optional<Package> optionalActual = loginPackageHandler.loginCreatingResponseIfFailed(
                givenTracker,
                givenLoginPackage
        );
        assertTrue(optionalActual.isEmpty());
    }

    private static final class TestUnprotectedLoginPackageHandler extends UnprotectedLoginPackageHandler {

        public TestUnprotectedLoginPackageHandler() {
            super(null, null, null, null);
        }

        @Override
        protected Package createNoSuchImeiResponse() {
            throw new UnsupportedOperationException();
        }

        @Override
        protected Package createSuccessResponse() {
            throw new UnsupportedOperationException();
        }
    }
}
