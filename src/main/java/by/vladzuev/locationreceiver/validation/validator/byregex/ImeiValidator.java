package by.vladzuev.locationreceiver.validation.validator.byregex;

import by.vladzuev.locationreceiver.validation.annotation.Imei;

public final class ImeiValidator extends ValidatorByRegex<Imei> {
    private static final String REGEX = "\\d{20}";

    public ImeiValidator() {
        super(REGEX);
    }
}
