package by.bsu.wialontransport.protocol.core.handler.packages.login;

import by.bsu.wialontransport.crud.dto.Tracker;
import by.bsu.wialontransport.protocol.core.model.login.ProtectedLoginPackage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.same;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public final class ProtectedLoginPackageHandlerTest {

    @Mock
    private BCryptPasswordEncoder mockedPasswordEncoder;

    private TestProtectedLoginPackageHandler handler;

    @BeforeEach
    public void initializeHandler() {
        handler = new TestProtectedLoginPackageHandler(mockedPasswordEncoder);
    }

    @Test
    public void loginShouldBeSuccess() {
        final String givenTrackerPassword = "sdffsdsj324243sdsfsdf";
        final Tracker givenTracker = Tracker.builder().password(givenTrackerPassword).build();
        final String givenRequestPassword = "111";
        final TestRequest givenRequest = new TestRequest(givenRequestPassword);

        when(mockedPasswordEncoder.matches(same(givenRequestPassword), same(givenTrackerPassword))).thenReturn(true);

        final Optional<Object> optionalActual = handler.loginCreatingFailedResponse(givenTracker, givenRequest);
        assertTrue(optionalActual.isEmpty());
    }

    @Test
    public void loginShouldBeFailed() {
        final String givenTrackerPassword = "sdffsdsj324243sdsfsdf";
        final Tracker givenTracker = Tracker.builder().password(givenTrackerPassword).build();
        final String givenRequestPassword = "111";
        final TestRequest givenRequest = new TestRequest(givenRequestPassword);

        when(mockedPasswordEncoder.matches(same(givenRequestPassword), same(givenTrackerPassword))).thenReturn(false);

        final Optional<Object> optionalActual = handler.loginCreatingFailedResponse(givenTracker, givenRequest);
        assertTrue(optionalActual.isPresent());
        final Object actual = optionalActual.get();
        assertTrue(actual instanceof TestWrongPasswordResponse);
    }

    private static final class TestRequest extends ProtectedLoginPackage {

        public TestRequest(final String password) {
            super(null, password);
        }
    }

    private static final class TestWrongPasswordResponse {

    }

    private static final class TestProtectedLoginPackageHandler extends ProtectedLoginPackageHandler<TestRequest> {

        public TestProtectedLoginPackageHandler(final BCryptPasswordEncoder passwordEncoder) {
            super(TestRequest.class, null, null, null, null, passwordEncoder);
        }

        @Override
        protected Object createNoSuchImeiResponse() {
            throw new UnsupportedOperationException();
        }

        @Override
        protected Object createSuccessResponse() {
            throw new UnsupportedOperationException();
        }

        @Override
        protected TestWrongPasswordResponse createWrongPasswordResponse() {
            return new TestWrongPasswordResponse();
        }
    }
}
