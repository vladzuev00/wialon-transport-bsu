package by.bsu.wialontransport.service.useraction.exception;

public final class TrackerImeiAlreadyExistsException extends TrackerUniqueConstraintException {

    public TrackerImeiAlreadyExistsException() {

    }

    public TrackerImeiAlreadyExistsException(final String description) {
        super(description);
    }

    public TrackerImeiAlreadyExistsException(final Exception cause) {
        super(cause);
    }

    public TrackerImeiAlreadyExistsException(final String description, final Exception cause) {
        super(description, cause);
    }

}
