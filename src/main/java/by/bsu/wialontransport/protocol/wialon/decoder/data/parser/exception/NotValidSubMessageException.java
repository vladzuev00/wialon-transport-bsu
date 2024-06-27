package by.bsu.wialontransport.protocol.wialon.decoder.data.parser.exception;

public final class NotValidSubMessageException extends RuntimeException {

    @SuppressWarnings("unused")
    public NotValidSubMessageException() {

    }

    public NotValidSubMessageException(final String description) {
        super(description);
    }

    @SuppressWarnings("unused")
    public NotValidSubMessageException(final Exception cause) {
        super(cause);
    }

    @SuppressWarnings("unused")
    public NotValidSubMessageException(final String description, final Exception cause) {
        super(description, cause);
    }
}
