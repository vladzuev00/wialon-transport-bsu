package by.vladzuev.locationreceiver.controller.exception;

public final class NoSuchEntityException extends RuntimeException {

    @SuppressWarnings("unused")
    public NoSuchEntityException() {

    }

    public NoSuchEntityException(final String description) {
        super(description);
    }

    @SuppressWarnings("unused")
    public NoSuchEntityException(final Exception cause) {
        super(cause);
    }

    @SuppressWarnings("unused")
    public NoSuchEntityException(final String description, final Exception cause) {
        super(description, cause);
    }

}
