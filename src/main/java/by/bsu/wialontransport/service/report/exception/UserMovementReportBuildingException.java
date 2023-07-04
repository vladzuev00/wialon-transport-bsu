package by.bsu.wialontransport.service.report.exception;

public final class UserMovementReportBuildingException extends RuntimeException {

    public UserMovementReportBuildingException() {

    }

    public UserMovementReportBuildingException(final String description) {
        super(description);
    }

    public UserMovementReportBuildingException(final Exception cause) {
        super(cause);
    }

    public UserMovementReportBuildingException(final String description, final Exception cause) {
        super(description, cause);
    }

}
