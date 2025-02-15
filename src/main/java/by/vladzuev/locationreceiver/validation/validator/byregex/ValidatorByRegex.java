package by.vladzuev.locationreceiver.validation.validator.byregex;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.lang.annotation.Annotation;
import java.util.regex.Pattern;

import static java.util.regex.Pattern.compile;

public abstract class ValidatorByRegex<A extends Annotation> implements ConstraintValidator<A, String> {
    private final Pattern pattern;

    public ValidatorByRegex(final String regex) {
        pattern = compile(regex);
    }

    @Override
    public final boolean isValid(final String value, final ConstraintValidatorContext context) {
        return value != null && pattern.matcher(value).matches();
    }
}
