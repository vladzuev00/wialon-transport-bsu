package by.bsu.wialontransport.protocol.core.exception;

import by.bsu.wialontransport.protocol.core.model.packages.Package;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public final class AnswerableException extends RuntimeException {
    private final Package answer;

    @SuppressWarnings("unused")
    public AnswerableException(final Package answer, final String description) {
        super(description);
        this.answer = answer;
    }

    public AnswerableException(final Package answer, final Exception cause) {
        super(cause);
        this.answer = answer;
    }

    @SuppressWarnings("unused")
    public AnswerableException(final Package answer, final String description, final Exception cause) {
        super(description, cause);
        this.answer = answer;
    }
}
