package by.bsu.wialontransport.model.validation.annotation;

import by.bsu.wialontransport.model.validation.validator.PasswordValidator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;

@Target({FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = PasswordValidator.class)
public @interface Password {
    String message() default "not valid password";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
