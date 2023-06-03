package by.bsu.wialontransport.service.useraction.changepassword.exception;

public final class NewPasswordConfirmingException extends PasswordChangingException {
    private static final String ATTRIBUTE_NAME_OF_ERROR = "newPasswordConfirmingError";
    private static final String ATTRIBUTE_VALUE_OF_ERROR = "Confirmed new password doesn't match";

    public NewPasswordConfirmingException() {

    }

    public NewPasswordConfirmingException(final String description) {
        super(description);
    }

    public NewPasswordConfirmingException(final Exception cause) {
        super(cause);
    }

    public NewPasswordConfirmingException(final String description, final Exception cause) {
        super(description, cause);
    }

    @Override
    public String findErrorAttributeName() {
        return ATTRIBUTE_NAME_OF_ERROR;
    }

    @Override
    public String findErrorAttributeValue() {
        return ATTRIBUTE_VALUE_OF_ERROR;
    }

}
