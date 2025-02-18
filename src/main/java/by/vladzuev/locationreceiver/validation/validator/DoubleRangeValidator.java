package by.vladzuev.locationreceiver.validation.validator;

import by.vladzuev.locationreceiver.validation.annotation.DoubleRange;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.hibernate.validator.internal.engine.constraintvalidation.ConstraintValidatorContextImpl;

import static java.lang.Double.compare;

public final class DoubleRangeValidator implements ConstraintValidator<DoubleRange, Double> {

    @Override
    public boolean isValid(final Double value, final ConstraintValidatorContext context) {
        final DoubleRange range = getRange(context);
        return value != null && isBelong(value, range);
    }

    private DoubleRange getRange(final ConstraintValidatorContext context) {
        return (DoubleRange) ((ConstraintValidatorContextImpl) context)
                .getConstraintDescriptor()
                .getAnnotation();
    }

    private boolean isBelong(final double value, final DoubleRange range) {
        return compare(value, range.min()) >= 0 && compare(value, range.max()) <= 0;
    }
}
