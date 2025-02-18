package by.vladzuev.locationreceiver.validation.annotation;

import jakarta.validation.Payload;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@DoubleRange(min = 1, max = 10)
@Retention(RUNTIME)
@Target({PARAMETER, FIELD})
//@Constraint(validatedBy = {})
public @interface Hdop {
    String message() default "Invalid hdop";

    @SuppressWarnings("unused")
    Class<?>[] groups() default {};

    @SuppressWarnings("unused")
    Class<? extends Payload>[] payload() default {};
}
