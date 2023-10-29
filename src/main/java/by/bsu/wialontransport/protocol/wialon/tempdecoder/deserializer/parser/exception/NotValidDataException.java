package by.bsu.wialontransport.protocol.wialon.tempdecoder.deserializer.parser.exception;

public final class NotValidDataException extends RuntimeException {
    public NotValidDataException() {

    }

    public NotValidDataException(String description) {
        super(description);
    }

    public NotValidDataException(Exception cause) {
        super(cause);
    }

    public NotValidDataException(String description, Exception cause) {
        super(description, cause);
    }
}
