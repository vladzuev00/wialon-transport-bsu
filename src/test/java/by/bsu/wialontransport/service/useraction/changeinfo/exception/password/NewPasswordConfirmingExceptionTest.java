package by.bsu.wialontransport.service.useraction.changeinfo.exception.password;

import by.bsu.wialontransport.service.useraction.changeinfo.exception.password.NewPasswordConfirmingException;
import by.bsu.wialontransport.service.useraction.changeinfo.exception.password.PasswordChangingException;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public final class NewPasswordConfirmingExceptionTest {
    private static final String ATTRIBUTE_NAME_OF_ERROR = "newPasswordConfirmingError";
    private static final String ATTRIBUTE_VALUE_OF_ERROR = "Confirmed new password doesn't match";

    @Test
    public void errorAttributeNameShouldBeFound() {
        final PasswordChangingException givenException = new NewPasswordConfirmingException();

        final String actual = givenException.findErrorAttributeName();
        assertEquals(ATTRIBUTE_NAME_OF_ERROR, actual);
    }

    @Test
    public void errorAttributeValueShouldBeFound() {
        final PasswordChangingException givenException = new NewPasswordConfirmingException();

        final String actual = givenException.findErrorAttributeValue();
        assertEquals(ATTRIBUTE_VALUE_OF_ERROR, actual);
    }

}
