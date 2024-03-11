package by.bsu.wialontransport.service.security.exception;

public final class NoAuthorizedUserException extends RuntimeException {

    public NoAuthorizedUserException() {

    }

    @SuppressWarnings("unused")
    public NoAuthorizedUserException(final String description) {
        super(description);
    }

    @SuppressWarnings("unused")
    public NoAuthorizedUserException(final Exception cause) {
        super(cause);
    }

    @SuppressWarnings("unused")
    public NoAuthorizedUserException(final String description, final Exception cause) {
        super(description, cause);
    }
}
