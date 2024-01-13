package by.bsu.wialontransport.protocol.wialon.decoder.packages.data.parser.exception;

public final class NotValidMessageException extends RuntimeException {

    @SuppressWarnings("unused")
    public NotValidMessageException() {

    }

    public NotValidMessageException(final String description) {
        super(description);
    }

    @SuppressWarnings("unused")
    public NotValidMessageException(final Exception cause) {
        super(cause);
    }

    @SuppressWarnings("unused")
    public NotValidMessageException(final String description, final Exception cause) {
        super(description, cause);
    }
}
