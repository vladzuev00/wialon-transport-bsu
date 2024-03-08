package by.bsu.wialontransport.validation.validator.byregex;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import javax.validation.ConstraintValidatorContext;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verifyNoInteractions;

public final class PasswordValidatorTest {
    private final PasswordValidator validator = new PasswordValidator();

    @ParameterizedTest
    @MethodSource("providePasswordAndExpectedValidationResult")
    public void passwordShouldBeValidated(final String givenPassword, final boolean expected) {
        final ConstraintValidatorContext givenContext = mock(ConstraintValidatorContext.class);

        final boolean actual = validator.isValid(givenPassword, givenContext);
        assertEquals(expected, actual);

        verifyNoInteractions(givenContext);
    }

    private static Stream<Arguments> providePasswordAndExpectedValidationResult() {
        return Stream.of(
                Arguments.of("ec2-11-111-222-333cd-blahblah-1computeamazonawscom", true),
                Arguments.of("password", true),
                Arguments.of("111", true),
                Arguments.of("11", false),
                Arguments.of("", false),
                Arguments.of(null, false)
        );
    }
}
