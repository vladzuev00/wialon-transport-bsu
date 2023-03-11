package by.bsu.wialontransport.protocol.core.service.receivingdata.fixer.exception;

public final class DataFixingException extends RuntimeException {
    public DataFixingException() {

    }

    public DataFixingException(final String description) {
        super(description);
    }

    public DataFixingException(final Exception cause) {
        super(cause);
    }

    public DataFixingException(final String description, final Exception cause) {
        super(description, cause);
    }
}
