package by.bsu.wialontransport.model.validation.validator;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public final class PasswordValidatorTest {
    private final PasswordValidator validator = new PasswordValidator();

    @Test
    public void passwordShouldBeValid() {
        final boolean actual = this.validator.isValid("password", null);
        assertTrue(actual);
    }

    @Test
    @SuppressWarnings("ConstantConditions")
    public void passwordShouldNotBeValidBecauseOfItIsNull() {
        final boolean actual = this.validator.isValid(null, null);
        assertFalse(actual);
    }

    @Test
    public void passwordShouldNotBeValidBecauseOfItDoesNotMatchRegex() {
        final boolean actual = this.validator.isValid("password password", null);
        assertFalse(actual);
    }

}
