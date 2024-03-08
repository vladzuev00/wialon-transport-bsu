package by.bsu.wialontransport.service.registration.exception;

public final class PasswordConfirmException extends RegistrationException {

    @SuppressWarnings("unused")
    public PasswordConfirmException() {

    }

    public PasswordConfirmException(final String description) {
        super(description);
    }

    @SuppressWarnings("unused")
    public PasswordConfirmException(final Exception cause) {
        super(cause);
    }

    @SuppressWarnings("unused")
    public PasswordConfirmException(final String description, final Exception cause) {
        super(description, cause);
    }
}
