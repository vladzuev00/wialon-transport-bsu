package by.vladzuev.locationreceiver.service.useraction.changeinfo.exception.password;

public abstract class PasswordChangingException extends Exception {

    public PasswordChangingException() {

    }

    public PasswordChangingException(final String description) {
        super(description);
    }

    public PasswordChangingException(final Exception cause) {
        super(cause);
    }

    public PasswordChangingException(final String description, final Exception cause) {
        super(description, cause);
    }

    public abstract String findErrorAttributeName();

    public abstract String findErrorAttributeValue();

}
