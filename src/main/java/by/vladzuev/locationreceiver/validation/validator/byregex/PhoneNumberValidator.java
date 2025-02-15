package by.vladzuev.locationreceiver.validation.validator.byregex;

import by.vladzuev.locationreceiver.validation.annotation.PhoneNumber;

public final class PhoneNumberValidator extends ValidatorByRegex<PhoneNumber> {
    private static final String REGEX = "\\d{9}";

    public PhoneNumberValidator() {
        super(REGEX);
    }
}
