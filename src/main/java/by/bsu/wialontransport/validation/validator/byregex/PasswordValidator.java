package by.bsu.wialontransport.validation.validator.byregex;

import by.bsu.wialontransport.validation.annotation.Password;

public final class PasswordValidator extends ValidatorByRegex<Password> {
    private static final String REGEX = "[a-zA-Z0-9-]{3,128}";

    public PasswordValidator() {
        super(REGEX);
    }
}
