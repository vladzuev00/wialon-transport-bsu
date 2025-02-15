package by.vladzuev.locationreceiver.service.registration.exception;

public final class EmailAlreadyExistsException extends RegistrationException {

    @SuppressWarnings("unused")
    public EmailAlreadyExistsException() {

    }

    public EmailAlreadyExistsException(final String description) {
        super(description);
    }

    @SuppressWarnings("unused")
    public EmailAlreadyExistsException(final Exception cause) {
        super(cause);
    }

    @SuppressWarnings("unused")
    public EmailAlreadyExistsException(final String description, final Exception cause) {
        super(description, cause);
    }
}
