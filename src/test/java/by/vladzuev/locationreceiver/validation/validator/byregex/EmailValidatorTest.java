package by.vladzuev.locationreceiver.validation.validator.byregex;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import javax.validation.ConstraintValidatorContext;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verifyNoInteractions;

public final class EmailValidatorTest {
    private final EmailValidator validator = new EmailValidator();

    @ParameterizedTest
    @MethodSource("provideEmailAndExpectedValidationResult")
    public void emailShouldBeValidated(final String givenEmail, final boolean expected) {
        final ConstraintValidatorContext givenContext = mock(ConstraintValidatorContext.class);

        final boolean actual = validator.isValid(givenEmail, givenContext);
        assertEquals(expected, actual);

        verifyNoInteractions(givenContext);
    }

    private static Stream<Arguments> provideEmailAndExpectedValidationResult() {
        return Stream.of(
                Arguments.of("vladzuev.00@mail.ru", true),
                Arguments.of("ivan2000@gmail.com", true),
                Arguments.of("sergey-zuev@gmail.com", true),
                Arguments.of("ivan2000gmail.com", false),
                Arguments.of("     ", false),
                Arguments.of("12552.2225", false),
                Arguments.of(null, false)
        );
    }
}
