package by.bsu.wialontransport.controller.exception;

public final class CustomValidationException extends RuntimeException {

    public CustomValidationException() {

    }

    public CustomValidationException(final String description) {
        super(description);
    }

    public CustomValidationException(final Exception cause) {
        super(cause);
    }

    public CustomValidationException(final String description, final Exception cause) {
        super(description, cause);
    }

}
