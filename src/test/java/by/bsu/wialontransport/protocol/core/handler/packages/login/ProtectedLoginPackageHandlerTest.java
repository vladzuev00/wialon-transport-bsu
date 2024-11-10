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
        final Tracker givenTracker = Tracker.builder().password("sdffsdsj324243sdsfsdf").build();
        final TestRequest givenRequest = new TestRequest("111");

        mockPasswordMatching(givenTracker, givenRequest, true);

        final Optional<Object> optionalActual = handler.loginCreatingFailedResponse(givenTracker, givenRequest);
        assertTrue(optionalActual.isEmpty());
    }

    @Test
    public void loginShouldBeFailed() {
        final Tracker givenTracker = Tracker.builder().password("sdffsdsj324243sdsfsdf").build();
        final TestRequest givenRequest = new TestRequest("111");

        mockPasswordMatching(givenTracker, givenRequest, false);

        final Optional<Object> optionalActual = handler.loginCreatingFailedResponse(givenTracker, givenRequest);
        assertTrue(optionalActual.isPresent());
        final Object actual = optionalActual.get();
        assertTrue(actual instanceof TestWrongPasswordResponse);
    }

    private void mockPasswordMatching(final Tracker tracker, final TestRequest request, final boolean match) {
        when(mockedPasswordEncoder.matches(same(request.getPassword()), same(tracker.getPassword()))).thenReturn(match);
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
            super(TestRequest.class, null, null, null, null, null, passwordEncoder);
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
