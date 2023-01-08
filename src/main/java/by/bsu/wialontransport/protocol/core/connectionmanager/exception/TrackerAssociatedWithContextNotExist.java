package by.bsu.wialontransport.protocol.core.connectionmanager.exception;

public final class TrackerAssociatedWithContextNotExist extends RuntimeException {
    public TrackerAssociatedWithContextNotExist() {

    }

    public TrackerAssociatedWithContextNotExist(final String description) {
        super(description);
    }

    public TrackerAssociatedWithContextNotExist(final Exception cause) {
        super(cause);
    }

    public TrackerAssociatedWithContextNotExist(final String description, final Exception cause) {
        super(description, cause);
    }
}
