package by.bsu.wialontransport.service.nominatim.exception;

public final class NominatimException extends RuntimeException {

    public NominatimException() {

    }

    public NominatimException(final String description) {
        super(description);
    }

    public NominatimException(final Exception cause) {
        super(cause);
    }

    public NominatimException(final String description, final Exception cause) {
        super(description, cause);
    }
}
