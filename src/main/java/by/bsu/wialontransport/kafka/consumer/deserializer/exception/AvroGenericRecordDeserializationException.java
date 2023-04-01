package by.bsu.wialontransport.kafka.consumer.deserializer.exception;

public final class AvroGenericRecordDeserializationException extends RuntimeException {

    public AvroGenericRecordDeserializationException() {

    }

    public AvroGenericRecordDeserializationException(final String description) {
        super(description);
    }

    public AvroGenericRecordDeserializationException(final Exception cause) {
        super(cause);
    }

    public AvroGenericRecordDeserializationException(final String description, final Exception cause) {
        super(description, cause);
    }
}
