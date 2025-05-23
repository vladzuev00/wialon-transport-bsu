package by.vladzuev.locationreceiver.protocol.core.handler.packages.login;

import by.vladzuev.locationreceiver.crud.dto.Tracker;
import by.vladzuev.locationreceiver.protocol.core.model.ProtectedLoginPackage;
import lombok.Value;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;
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

        when(mockedPasswordEncoder.matches(same(givenRequest.getPassword()), same(givenTracker.getPassword()))).thenReturn(true);

        final Optional<Object> optionalActual = handler.loginCreatingFailedResponse(givenTracker, givenRequest);
        assertTrue(optionalActual.isEmpty());
    }

    @Test
    public void loginShouldBeFailed() {
        final Tracker givenTracker = Tracker.builder().password("sdffsdsj324243sdsfsdf").build();
        final TestRequest givenRequest = new TestRequest("111");

        when(mockedPasswordEncoder.matches(same(givenRequest.getPassword()), same(givenTracker.getPassword()))).thenReturn(false);

        final Optional<Object> optionalActual = handler.loginCreatingFailedResponse(givenTracker, givenRequest);
        assertTrue(optionalActual.isPresent());
        final Object actual = optionalActual.get();
        assertInstanceOf(TestWrongPasswordResponse.class, actual);
    }

    @Value
    private static class TestRequest implements ProtectedLoginPackage {
        String password;

        @Override
        public String getImei() {
            throw new UnsupportedOperationException();
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
        protected void onSuccess() {
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
