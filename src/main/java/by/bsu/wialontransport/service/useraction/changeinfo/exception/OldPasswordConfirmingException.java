package by.bsu.wialontransport.service.useraction.changeinfo.exception;

public final class OldPasswordConfirmingException extends PasswordChangingException {
    private static final String ATTRIBUTE_NAME_OF_ERROR = "oldPasswordConfirmingError";
    private static final String ATTRIBUTE_VALUE_OF_ERROR = "Old password isn't correct";

    public OldPasswordConfirmingException() {

    }

    public OldPasswordConfirmingException(final String description) {
        super(description);
    }

    public OldPasswordConfirmingException(final Exception cause) {
        super(cause);
    }

    public OldPasswordConfirmingException(final String description, final Exception cause) {
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
