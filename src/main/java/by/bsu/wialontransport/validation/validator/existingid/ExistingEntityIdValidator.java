package by.bsu.wialontransport.validation.validator.existingid;

import by.bsu.wialontransport.crud.service.CRUDService;
import lombok.RequiredArgsConstructor;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.lang.annotation.Annotation;

//TODO: test
@RequiredArgsConstructor
public abstract class ExistingEntityIdValidator<A extends Annotation, ID> implements ConstraintValidator<A, ID> {
    private final CRUDService<ID, ?, ?, ?, ?> service;

    @Override
    public final boolean isValid(final ID value, final ConstraintValidatorContext context) {
        return service.isExist(value);
    }
}
