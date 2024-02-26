package by.bsu.wialontransport.validation.validator;

import by.bsu.wialontransport.validation.annotation.Host;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.regex.Pattern;

import static java.util.regex.Pattern.compile;

public final class HostValidator implements ConstraintValidator<Host, String> {
    private static final String HOST_REGEX = "^([a-zA-Z0-9]|[a-zA-Z0-9][a-zA-Z0-9\\-]{0,61}[a-zA-Z0-9])"
            + "(\\.([a-zA-Z0-9]|[a-zA-Z0-9][a-zA-Z0-9\\-]{0,61}[a-zA-Z0-9]))*$";
    private static final Pattern HOST_PATTERN = compile(HOST_REGEX);

    @Override
    public boolean isValid(final String value, final ConstraintValidatorContext context) {
        return value != null && HOST_PATTERN.matcher(value).matches();
    }
}
