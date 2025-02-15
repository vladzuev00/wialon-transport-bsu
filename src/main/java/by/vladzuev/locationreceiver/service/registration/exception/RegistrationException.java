package by.vladzuev.locationreceiver.service.registration.exception;

public abstract class RegistrationException extends RuntimeException {

    public RegistrationException() {

    }

    public RegistrationException(final String description) {
        super(description);
    }

    public RegistrationException(final Exception cause) {
        super(cause);
    }

    public RegistrationException(final String description, final Exception cause) {
        super(description, cause);
    }
}
