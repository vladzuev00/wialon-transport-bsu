package by.bsu.wialontransport.kafka.serializer.exception;

public final class AvroGenericRecordSerializationException extends RuntimeException {
    public AvroGenericRecordSerializationException() {

    }

    public AvroGenericRecordSerializationException(final String description) {
        super(description);
    }

    public AvroGenericRecordSerializationException(final Exception cause) {
        super(cause);
    }

    public AvroGenericRecordSerializationException(final String description, final Exception cause) {
        super(description, cause);
    }
}
