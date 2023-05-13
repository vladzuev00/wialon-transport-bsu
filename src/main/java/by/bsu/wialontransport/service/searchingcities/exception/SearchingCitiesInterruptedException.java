package by.bsu.wialontransport.service.searchingcities.exception;

public final class SearchingCitiesInterruptedException extends RuntimeException {

    public SearchingCitiesInterruptedException() {

    }

    public SearchingCitiesInterruptedException(final String description) {
        super(description);
    }

    public SearchingCitiesInterruptedException(final Exception cause) {
        super(cause);
    }

    public SearchingCitiesInterruptedException(final String description, final Exception cause) {
        super(description, cause);
    }

}
