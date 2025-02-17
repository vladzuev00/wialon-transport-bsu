package by.vladzuev.locationreceiver.validation.annotation;

import by.vladzuev.locationreceiver.validation.validator.unique.UniqueTrackerImeiValidator;
import jakarta.validation.Payload;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target(TYPE)
@Retention(RUNTIME)
//@Constraint(validatedBy = UniqueTrackerImeiValidator.class)
public @interface UniqueTrackerImei {
    String message() default "Imei should be unique";

    @SuppressWarnings("unused")
    Class<?>[] groups() default {};

    @SuppressWarnings("unused")
    Class<? extends Payload>[] payload() default {};
}
