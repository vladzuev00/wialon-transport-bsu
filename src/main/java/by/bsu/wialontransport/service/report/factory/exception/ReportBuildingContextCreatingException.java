package by.bsu.wialontransport.service.report.factory.exception;

public final class ReportBuildingContextCreatingException extends RuntimeException {

    public ReportBuildingContextCreatingException() {

    }

    public ReportBuildingContextCreatingException(final String description) {
        super(description);
    }

    public ReportBuildingContextCreatingException(final Exception cause) {
        super(cause);
    }

    public ReportBuildingContextCreatingException(final String description, final Exception cause) {
        super(description, cause);
    }

}
