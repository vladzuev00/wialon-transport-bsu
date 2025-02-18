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
//public final class PhoneNumberValidatorTest {
//    private final PhoneNumberValidator validator = new PhoneNumberValidator();
//
//    @ParameterizedTest
//    @MethodSource("providePhoneNumberAndExpectedValidationResult")
//    public void phoneNumberShouldBeValidated(final String givenPhoneNumber, final boolean expected) {
//        final ConstraintValidatorContext givenContext = mock(ConstraintValidatorContext.class);
//
//        final boolean actual = validator.isValid(givenPhoneNumber, givenContext);
//        assertEquals(expected, actual);
//
//        verifyNoInteractions(givenContext);
//    }
//
//    private static Stream<Arguments> providePhoneNumberAndExpectedValidationResult() {
//        return Stream.of(
//                Arguments.of("447336934", true),
//                Arguments.of("44733693", false),
//                Arguments.of("4473369344", false),
//                Arguments.of("4473a6934", false),
//                Arguments.of("     ", false),
//                Arguments.of("", false),
//                Arguments.of(null, false)
//        );
//    }
//}
