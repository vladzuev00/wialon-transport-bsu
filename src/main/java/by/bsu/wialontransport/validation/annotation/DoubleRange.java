package by.bsu.wialontransport.validation.annotation;

import by.bsu.wialontransport.validation.validator.DoubleRangeValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Retention(RUNTIME)
@Target({PARAMETER, FIELD})
@Constraint(validatedBy = DoubleRangeValidator.class)
public @interface DoubleRange {
    double min();

    double max();

    String message() default "Not valid double";

    @SuppressWarnings("unused")
    Class<?>[] groups() default {};

    @SuppressWarnings("unused")
    Class<? extends Payload>[] payload() default {};
}
