package by.vladzuev.locationreceiver.validation.annotation;

import jakarta.validation.Payload;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

//@NotNull
//@Min(1)
//@Max(65535)
@Retention(RUNTIME)
@Target({PARAMETER, FIELD})
//@Constraint(validatedBy = {})
public @interface Port {
    String message() default "Invalid port";

    @SuppressWarnings("unused")
    Class<?>[] groups() default {};

    @SuppressWarnings("unused")
    Class<? extends Payload>[] payload() default {};
}
