package by.bsu.wialontransport.service.geocoding.exception;

public final class GeocodingException extends RuntimeException {

    public GeocodingException() {

    }

    public GeocodingException(final String description) {
        super(description);
    }

    public GeocodingException(final Exception cause) {
        super(cause);
    }

    public GeocodingException(final String description, final Exception cause) {
        super(description, cause);
    }

}
