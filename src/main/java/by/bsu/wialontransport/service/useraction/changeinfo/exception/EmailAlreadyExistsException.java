package by.bsu.wialontransport.service.useraction.changeinfo.exception;

public final class EmailAlreadyExistsException extends Exception {

    public EmailAlreadyExistsException() {

    }

    public EmailAlreadyExistsException(final String description) {
        super(description);
    }

    public EmailAlreadyExistsException(final Exception cause) {
        super(cause);
    }

    public EmailAlreadyExistsException(final String description, final Exception cause) {
        super(description, cause);
    }

}
