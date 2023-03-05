package by.bsu.wialontransport.protocol.core.service.receivingdata.exception;

public final class NoTrackerInContextException extends RuntimeException {
    public NoTrackerInContextException() {
        super();
    }

    public NoTrackerInContextException(final String description) {
        super(description);
    }

    public NoTrackerInContextException(final Exception cause) {
        super(cause);
    }

    public NoTrackerInContextException(final String description, final Exception cause) {
        super(description, cause);
    }
}
