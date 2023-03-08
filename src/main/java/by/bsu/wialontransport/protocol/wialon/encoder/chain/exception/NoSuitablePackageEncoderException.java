package by.bsu.wialontransport.protocol.wialon.encoder.chain.exception;

public final class NoSuitablePackageEncoderException extends RuntimeException {
    public NoSuitablePackageEncoderException() {

    }

    public NoSuitablePackageEncoderException(final String description) {
        super(description);
    }

    public NoSuitablePackageEncoderException(final Exception cause) {
        super(cause);
    }

    public NoSuitablePackageEncoderException(final String description, final Exception cause) {
        super(description, cause);
    }
}
