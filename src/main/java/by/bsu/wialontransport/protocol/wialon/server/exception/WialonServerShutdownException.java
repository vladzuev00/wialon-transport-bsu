package by.bsu.wialontransport.protocol.wialon.server.exception;

public final class WialonServerShutdownException extends RuntimeException {
    public WialonServerShutdownException() {

    }

    public WialonServerShutdownException(final String description) {
        super(description);
    }

    public WialonServerShutdownException(final Exception cause) {
        super(cause);
    }

    public WialonServerShutdownException(final String description, final Exception cause) {
        super(description, cause);
    }
}
