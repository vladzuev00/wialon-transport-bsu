package by.vladzuev.locationreceiver.validation.annotation;

import by.vladzuev.locationreceiver.validation.validator.byregex.HostValidator;
import jakarta.validation.Payload;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Retention(RUNTIME)
@Target({PARAMETER, FIELD})
//@Constraint(validatedBy = HostValidator.class)
public @interface Host {
    String message() default "Invalid host";

    @SuppressWarnings("unused")
    Class<?>[] groups() default {};

    @SuppressWarnings("unused")
    Class<? extends Payload>[] payload() default {};
}
