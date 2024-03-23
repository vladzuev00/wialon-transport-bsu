package by.bsu.wialontransport.validation.annotation;

import by.bsu.wialontransport.validation.validator.existingid.ExistingUserIdValidator;

import javax.validation.Constraint;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Retention(RUNTIME)
@Target({PARAMETER, FIELD})
@Constraint(validatedBy = ExistingUserIdValidator.class)
public @interface ExistingUserId {

}
