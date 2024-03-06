package by.bsu.wialontransport.validation.validator.byregex;

import by.bsu.wialontransport.validation.annotation.Email;

public final class EmailValidator extends ValidatorByRegex<Email> {
    private static final String REGEX = "[A-Za-z0-9._%-]+@[A-Za-z0-9.-]+\\.[A-Za-z]+";

    public EmailValidator() {
        super(REGEX);
    }
}
