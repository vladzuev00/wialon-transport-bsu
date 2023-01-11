package by.bsu.wialontransport.protocol.core.exception;

public final class AnswerableException extends RuntimeException {
    private final String answer;

    public AnswerableException(final String answer) {
        this.answer = answer;
    }

    public AnswerableException(final String answer, final String description) {
        super(description);
        this.answer = answer;
    }

    public AnswerableException(final String answer, final Exception cause) {
        super(cause);
        this.answer = answer;
    }

    public AnswerableException(final String answer, final String description, final Exception cause) {
        super(description, cause);
        this.answer = answer;
    }

    public String getAnswer() {
        return this.answer;
    }
}