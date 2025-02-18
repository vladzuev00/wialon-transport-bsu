//package by.vladzuev.locationreceiver.validation.validator.byregex;
//
//import org.junit.jupiter.params.ParameterizedTest;
//import org.junit.jupiter.params.provider.Arguments;
//import org.junit.jupiter.params.provider.MethodSource;
//
//import javax.validation.ConstraintValidatorContext;
//import java.util.stream.Stream;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.mockito.Mockito.mock;
//import static org.mockito.Mockito.verifyNoInteractions;
//
//public final class HostValidatorTest {
//    private final HostValidator validator = new HostValidator();
//
//    @ParameterizedTest
//    @MethodSource("provideHostAndExpectedValidationResult")
//    public void hostShouldBeValidated(final String givenHost, final boolean expected) {
//        final ConstraintValidatorContext givenContext = mock(ConstraintValidatorContext.class);
//
//        final boolean actual = validator.isValid(givenHost, givenContext);
//        assertEquals(expected, actual);
//
//        verifyNoInteractions(givenContext);
//    }
//
//    private static Stream<Arguments> provideHostAndExpectedValidationResult() {
//        return Stream.of(
//                Arguments.of("ec2-11-111-222-333.cd-blahblah-1.compute.amazonaws.com", true),
//                Arguments.of("domaine.com", true),
//                Arguments.of("subdomain.domain.com", true),
//                Arguments.of("12533d5.dkkkd.com", true),
//                Arguments.of("1dotextension.c", true),
//                Arguments.of("12552.2225", true),
//                Arguments.of("112.25.25", true),
//                Arguments.of("12345.com", true),
//                Arguments.of("12345.123.com", true),
//                Arguments.of("domaine.123", true),
//                Arguments.of("whatever", true),
//                Arguments.of("9999-ee.99", true),
//                Arguments.of("112.25.25", true),
//                Arguments.of("localhost", true),
//                Arguments.of("ekkej_dhh.com", false),
//                Arguments.of("email@domain.com", false),
//                Arguments.of(".jjdj.kkd", false),
//                Arguments.of("-subdomain.domain.com", false),
//                Arguments.of("", false),
//                Arguments.of(null, false)
//        );
//    }
//}
