package by.vladzuev.locationreceiver.validation.validator;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import javax.validation.ConstraintValidatorContext;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verifyNoInteractions;

public final class PortValidatorTest {
    private final PortValidator validator = new PortValidator();

    @ParameterizedTest
    @MethodSource("providePortAndExpectedValidationResult")
    public void portShouldBeValidated(final Integer givenPort, final boolean expected) {
        final ConstraintValidatorContext givenContext = mock(ConstraintValidatorContext.class);

        final boolean actual = validator.isValid(givenPort, givenContext);
        assertEquals(expected, actual);

        verifyNoInteractions(givenContext);
    }

    private static Stream<Arguments> providePortAndExpectedValidationResult() {
        return Stream.of(
                Arguments.of(8080, true),
                Arguments.of(null, false),
                Arguments.of(0, false),
                Arguments.of(65536, false)
        );
    }
}
