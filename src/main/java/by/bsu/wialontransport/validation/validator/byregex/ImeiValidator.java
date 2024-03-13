package by.bsu.wialontransport.validation.validator.byregex;

import by.bsu.wialontransport.validation.annotation.Imei;

//TODO: test
public final class ImeiValidator extends ValidatorByRegex<Imei> {
    private static final String REGEX = "\\d{20}";

    public ImeiValidator() {
        super(REGEX);
    }
}
