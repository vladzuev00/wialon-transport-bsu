package by.bsu.wialontransport.kafka.consumer.exception;

public final class DataConsumingException extends RuntimeException {

    public DataConsumingException() {

    }

    public DataConsumingException(final String description) {
        super(description);
    }

    public DataConsumingException(final Exception cause) {
        super(cause);
    }

    public DataConsumingException(final String description, final Exception cause) {
        super(description, cause);
    }
}
