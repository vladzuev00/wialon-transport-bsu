package by.bsu.wialontransport.validation.validator.byregex;

import by.bsu.wialontransport.validation.annotation.PhoneNumber;

public final class PhoneNumberValidator extends ValidatorByRegex<PhoneNumber> {
    private static final String REGEX = "\\d{9}";

    public PhoneNumberValidator() {
        super(REGEX);
    }
}
