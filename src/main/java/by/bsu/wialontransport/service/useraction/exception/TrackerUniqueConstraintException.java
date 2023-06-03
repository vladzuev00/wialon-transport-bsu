package by.bsu.wialontransport.service.useraction.exception;

public abstract class TrackerUniqueConstraintException extends Exception {

    public TrackerUniqueConstraintException() {

    }

    public TrackerUniqueConstraintException(final String description) {
        super(description);
    }

    public TrackerUniqueConstraintException(final Exception cause) {
        super(cause);
    }

    public TrackerUniqueConstraintException(final String description, final Exception cause) {
        super(description, cause);
    }

}
