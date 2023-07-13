package by.bsu.wialontransport.service.report.tabledrawer.exception;

public final class DistributedTableDrawingException extends RuntimeException {

    public DistributedTableDrawingException() {

    }

    public DistributedTableDrawingException(final String description) {
        super(description);
    }

    public DistributedTableDrawingException(final Exception cause) {
        super(cause);
    }

    public DistributedTableDrawingException(final String description, final Exception cause) {
        super(description, cause);
    }

}
