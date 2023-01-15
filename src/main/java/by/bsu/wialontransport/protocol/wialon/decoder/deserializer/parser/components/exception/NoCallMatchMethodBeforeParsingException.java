package by.bsu.wialontransport.protocol.wialon.decoder.deserializer.parser.components.exception;

public final class NoCallMatchMethodBeforeParsingException extends RuntimeException {
    public NoCallMatchMethodBeforeParsingException() {
        super();
    }

    public NoCallMatchMethodBeforeParsingException(final String description) {
        super(description);
    }

    public NoCallMatchMethodBeforeParsingException(final Exception cause) {
        super(cause);
    }

    public NoCallMatchMethodBeforeParsingException(final String description, final Exception cause) {
        super(description, cause);
    }
}
