package by.bsu.wialontransport.protocol.wialon.temphandler.chain.exception;

public final class NoSuitablePackageHandlerException extends RuntimeException {
    public NoSuitablePackageHandlerException() {

    }

    public NoSuitablePackageHandlerException(final String description) {
        super(description);
    }

    public NoSuitablePackageHandlerException(final Exception cause) {
        super(cause);
    }

    public NoSuitablePackageHandlerException(final String description, final Exception cause) {
        super(description, cause);
    }
}
