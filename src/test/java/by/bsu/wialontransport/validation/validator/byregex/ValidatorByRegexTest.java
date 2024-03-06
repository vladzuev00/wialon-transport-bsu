package by.bsu.wialontransport.validation.validator.byregex;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import javax.validation.ConstraintValidatorContext;
import java.lang.annotation.Annotation;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verifyNoInteractions;

public final class ValidatorByRegexTest {
    private final TextValidator validator = new TextValidator();

    @ParameterizedTest
    @MethodSource("provideTextAndExpectedValidationResult")
    public void textShouldBeValidated(final String givenText, final boolean expected) {
        final ConstraintValidatorContext givenContext = mock(ConstraintValidatorContext.class);

        final boolean actual = validator.isValid(givenText, givenContext);
        assertEquals(expected, actual);

        verifyNoInteractions(givenContext);
    }

    private static Stream<Arguments> provideTextAndExpectedValidationResult() {
        return Stream.of(
                Arguments.of("test", true),
                Arguments.of("testt", false),
                Arguments.of(null, false)
        );
    }

    private static final class TextValidator extends ValidatorByRegex<Annotation> {
        private static final String REGEX = "test";

        public TextValidator() {
            super(REGEX);
        }
    }
}
