package by.vladzuev.locationreceiver.validation.validator.byregex;

import by.vladzuev.locationreceiver.validation.annotation.Host;

public final class HostValidator extends ValidatorByRegex<Host> {
    private static final String REGEX = "^([a-zA-Z0-9]|[a-zA-Z0-9][a-zA-Z0-9\\-]{0,61}[a-zA-Z0-9])"
            + "(\\.([a-zA-Z0-9]|[a-zA-Z0-9][a-zA-Z0-9\\-]{0,61}[a-zA-Z0-9]))*$";

    public HostValidator() {
        super(REGEX);
    }
}
