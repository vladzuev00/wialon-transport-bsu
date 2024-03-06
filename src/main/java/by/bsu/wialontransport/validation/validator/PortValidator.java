package by.bsu.wialontransport.validation.validator;

import by.bsu.wialontransport.validation.annotation.Port;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public final class PortValidator implements ConstraintValidator<Port, Integer> {
    private static final int MIN_VALID = 1;
    private static final int MAX_VALID = 65535;

    @Override
    public boolean isValid(final Integer value, final ConstraintValidatorContext context) {
        return value != null && MIN_VALID <= value && value <= MAX_VALID;
    }
}
