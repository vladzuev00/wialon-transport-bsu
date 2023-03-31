package by.bsu.wialontransport.protocol.wialon.server.exception;

public final class WialonRunningServerException extends RuntimeException {
    public WialonRunningServerException() {

    }

    public WialonRunningServerException(final String description) {
        super(description);
    }

    public WialonRunningServerException(final Exception cause) {
        super(cause);
    }

    public WialonRunningServerException(final String description, final Exception cause) {
        super(description, cause);
    }
}
