package by.bsu.wialontransport.service.searchingcities.exception;

public final class SearchingCitiesException extends RuntimeException {

    public SearchingCitiesException() {

    }

    public SearchingCitiesException(final String description) {
        super(description);
    }

    public SearchingCitiesException(final Exception cause) {
        super(cause);
    }

    public SearchingCitiesException(final String description, final Exception cause) {
        super(description, cause);
    }

}
