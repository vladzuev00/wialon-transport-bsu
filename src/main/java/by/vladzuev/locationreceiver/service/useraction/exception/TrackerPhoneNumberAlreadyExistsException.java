package by.vladzuev.locationreceiver.service.useraction.exception;

public final class TrackerPhoneNumberAlreadyExistsException extends TrackerUniqueConstraintException {

    public TrackerPhoneNumberAlreadyExistsException() {

    }

    public TrackerPhoneNumberAlreadyExistsException(final String description) {
        super(description);
    }

    public TrackerPhoneNumberAlreadyExistsException(final Exception cause) {
        super(cause);
    }

    public TrackerPhoneNumberAlreadyExistsException(final String description, final Exception cause) {
        super(description, cause);
    }

}
