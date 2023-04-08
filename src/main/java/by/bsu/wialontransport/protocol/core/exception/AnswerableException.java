package by.bsu.wialontransport.protocol.core.exception;

import by.bsu.wialontransport.protocol.wialon.wialonpackage.Package;

public final class AnswerableException extends RuntimeException {
    private final Package answer;

    public AnswerableException(final Package answer) {
        this.answer = answer;
    }

    public AnswerableException(final Package answer, final String description) {
        super(description);
        this.answer = answer;
    }

    public AnswerableException(final Package answer, final Exception cause) {
        super(cause);
        this.answer = answer;
    }

    public AnswerableException(final Package answer, final String description, final Exception cause) {
        super(description, cause);
        this.answer = answer;
    }

    public Package getAnswer() {
        return this.answer;
    }
}
