package by.bsu.wialontransport.controller.exception;

public final class CustomValidationException extends RuntimeException {

    @SuppressWarnings("unused")
    public CustomValidationException() {

    }

    public CustomValidationException(final String description) {
        super(description);
    }

    @SuppressWarnings("unused")
    public CustomValidationException(final Exception cause) {
        super(cause);
    }

    @SuppressWarnings("unused")
    public CustomValidationException(final String description, final Exception cause) {
        super(description, cause);
    }

}
