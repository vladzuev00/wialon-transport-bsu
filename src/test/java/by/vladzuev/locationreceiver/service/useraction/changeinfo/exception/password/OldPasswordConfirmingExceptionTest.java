package by.vladzuev.locationreceiver.service.useraction.changeinfo.exception.password;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public final class OldPasswordConfirmingExceptionTest {
    private static final String ATTRIBUTE_NAME_OF_ERROR = "oldPasswordConfirmingError";
    private static final String ATTRIBUTE_VALUE_OF_ERROR = "Old password isn't correct";

    @Test
    public void errorAttributeNameShouldBeFound() {
        final PasswordChangingException givenException = new OldPasswordConfirmingException();

        final String actual = givenException.findErrorAttributeName();
        assertEquals(ATTRIBUTE_NAME_OF_ERROR, actual);
    }

    @Test
    public void errorAttributeValueShouldBeFound() {
        final PasswordChangingException givenException = new OldPasswordConfirmingException();

        final String actual = givenException.findErrorAttributeValue();
        assertEquals(ATTRIBUTE_VALUE_OF_ERROR, actual);
    }

}
