package by.bsu.wialontransport.protocol.core.exception;

import by.bsu.wialontransport.protocol.wialon.wialonpackage.WialonPackage;

public final class AnswerableException extends RuntimeException {
    private final WialonPackage answer;

    public AnswerableException(final WialonPackage answer) {
        this.answer = answer;
    }

    public AnswerableException(final WialonPackage answer, final String description) {
        super(description);
        this.answer = answer;
    }

    public AnswerableException(final WialonPackage answer, final Exception cause) {
        super(cause);
        this.answer = answer;
    }

    public AnswerableException(final WialonPackage answer, final String description, final Exception cause) {
        super(description, cause);
        this.answer = answer;
    }

    public WialonPackage getAnswer() {
        return this.answer;
    }
}
