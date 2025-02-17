package by.vladzuev.locationreceiver.validation.validator;

import by.vladzuev.locationreceiver.validation.annotation.Port;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

//TODO: remove all validators and do as @Hdop
public final class PortValidator implements ConstraintValidator<Port, Integer> {
    private static final int MIN_VALID = 1;
    private static final int MAX_VALID = 65535;

    @Override
    public boolean isValid(final Integer value, final ConstraintValidatorContext context) {
        return value != null && MIN_VALID <= value && value <= MAX_VALID;
    }
}
