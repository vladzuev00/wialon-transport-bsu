package by.bsu.wialontransport.kafka.producer.exception;

public final class KafkaProducerMappingToGenericRecordException extends RuntimeException {

    public KafkaProducerMappingToGenericRecordException() {

    }

    public KafkaProducerMappingToGenericRecordException(final String description) {
        super(description);
    }

    public KafkaProducerMappingToGenericRecordException(final Exception cause) {
        super(cause);
    }

    public KafkaProducerMappingToGenericRecordException(final String description, final Exception cause) {
        super(description, cause);
    }
}
