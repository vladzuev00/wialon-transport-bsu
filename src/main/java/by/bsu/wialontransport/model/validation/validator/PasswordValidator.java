package by.bsu.wialontransport.model.validation.validator;

import by.bsu.wialontransport.model.validation.annotation.Password;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public final class PasswordValidator implements ConstraintValidator<Password, String> {
    private static final String REGEX_VALID_PASSWORD = "[a-zA-Z0-9-]{3,128}";

    @Override
    public boolean isValid(final String research, final ConstraintValidatorContext context) {
        return research != null && research.matches(REGEX_VALID_PASSWORD);
    }

}
