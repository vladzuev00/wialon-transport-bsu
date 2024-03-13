package by.bsu.wialontransport.validation.validator.byregex;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import javax.validation.ConstraintValidatorContext;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verifyNoInteractions;

public final class ImeiValidatorTest {
    private final ImeiValidator validator = new ImeiValidator();

    @ParameterizedTest
    @MethodSource("provideImeiAndExpectedValidationResult")
    public void imeiShouldBeValidated(final String givenImei, final boolean expected) {
        final ConstraintValidatorContext givenContext = mock(ConstraintValidatorContext.class);

        final boolean actual = validator.isValid(givenImei, givenContext);
        assertEquals(expected, actual);

        verifyNoInteractions(givenContext);
    }

    private static Stream<Arguments> provideImeiAndExpectedValidationResult() {
        return Stream.of(
                Arguments.of("11112222333344445555", true),
                Arguments.of("1111222233334444555", false),
                Arguments.of("111122223333444455555", false),
                Arguments.of("111122223333a4445555", false),
                Arguments.of("     ", false),
                Arguments.of("", false),
                Arguments.of(null, false)
        );
    }
}
