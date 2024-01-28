//package by.bsu.wialontransport.protocol.core.handler.packages.login;
//
//import by.bsu.wialontransport.crud.dto.Tracker;
//import by.bsu.wialontransport.protocol.core.model.packages.Package;
//import by.bsu.wialontransport.protocol.core.model.packages.login.ProtectedLoginPackage;
//import org.junit.Before;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.mockito.Mock;
//import org.mockito.junit.MockitoJUnitRunner;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//
//import java.util.Optional;
//
//import static org.junit.Assert.assertSame;
//import static org.junit.Assert.assertTrue;
//import static org.mockito.ArgumentMatchers.same;
//import static org.mockito.Mockito.when;
//
//@RunWith(MockitoJUnitRunner.class)
//public final class ProtectedLoginPackageHandlerTest {
//    private static final Package GIVEN_WRONG_PASSWORD_RESPONSE = new Package() {
//    };
//
//    @Mock
//    private BCryptPasswordEncoder mockedPasswordEncoder;
//
//    private ProtectedLoginPackageHandler loginPackageHandler;
//
//    @Before
//    public void initializeLoginPackageHandler() {
//        loginPackageHandler = new TestProtectedLoginPackageHandler(
//                mockedPasswordEncoder,
//                GIVEN_WRONG_PASSWORD_RESPONSE
//        );
//    }
//
//    @Test
//    public void loginShouldBeSuccessful() {
//        final String givenTrackerPassword = "$2a$10$8y9hC00YePN";
//        final Tracker givenTracker = createTracker(255L, givenTrackerPassword);
//
//        final String givenPackagePassword = "111";
//        final ProtectedLoginPackage givenLoginPackage = createLoginPackage(givenPackagePassword);
//
//        when(mockedPasswordEncoder.matches(same(givenPackagePassword), same(givenTrackerPassword)))
//                .thenReturn(true);
//
//        final Optional<Package> optionalActual = loginPackageHandler.loginCreatingResponseIfFailed(
//                givenTracker,
//                givenLoginPackage
//        );
//        assertTrue(optionalActual.isEmpty());
//    }
//
//    @Test
//    public void loginShouldBeFailed() {
//        final String givenTrackerPassword = "$2a$10$8y9hC00YeP";
//        final Tracker givenTracker = createTracker(256L, givenTrackerPassword);
//
//        final String givenPackagePassword = "1111";
//        final ProtectedLoginPackage givenLoginPackage = createLoginPackage(givenPackagePassword);
//
//        when(mockedPasswordEncoder.matches(same(givenPackagePassword), same(givenTrackerPassword)))
//                .thenReturn(false);
//
//        final Optional<Package> optionalActual = loginPackageHandler.loginCreatingResponseIfFailed(
//                givenTracker,
//                givenLoginPackage
//        );
//        assertTrue(optionalActual.isPresent());
//        final Package actual = optionalActual.get();
//        assertSame(GIVEN_WRONG_PASSWORD_RESPONSE, actual);
//    }
//
//    private static Tracker createTracker(final Long id, final String password) {
//        return Tracker.builder()
//                .id(id)
//                .password(password)
//                .build();
//    }
//
//    private static ProtectedLoginPackage createLoginPackage(final String password) {
//        return new ProtectedLoginPackage() {
//
//            @Override
//            public String getPassword() {
//                return password;
//            }
//
//            @Override
//            public String getImei() {
//                throw new UnsupportedOperationException();
//            }
//        };
//    }
//
//    private static final class TestProtectedLoginPackageHandler extends ProtectedLoginPackageHandler {
//        private final Package wrongPasswordResponse;
//
//        public TestProtectedLoginPackageHandler(final BCryptPasswordEncoder passwordEncoder,
//                                                final Package wrongPasswordResponse) {
//            super(null, null, null, null, passwordEncoder);
//            this.wrongPasswordResponse = wrongPasswordResponse;
//        }
//
//        @Override
//        protected Package createNoSuchImeiResponse() {
//            throw new UnsupportedOperationException();
//        }
//
//        @Override
//        protected Package createSuccessResponse() {
//            throw new UnsupportedOperationException();
//        }
//
//        @Override
//        protected Package createWrongPasswordResponse() {
//            return wrongPasswordResponse;
//        }
//    }
//}
