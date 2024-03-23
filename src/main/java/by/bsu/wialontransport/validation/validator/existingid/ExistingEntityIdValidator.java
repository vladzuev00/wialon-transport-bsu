package by.bsu.wialontransport.validation.validator.existingid;

import by.bsu.wialontransport.crud.service.CRUDService;
import lombok.RequiredArgsConstructor;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.lang.annotation.Annotation;

@RequiredArgsConstructor
public abstract class ExistingEntityIdValidator<A extends Annotation, ID> implements ConstraintValidator<A, ID> {
    private final CRUDService<ID, ?, ?, ?, ?> service;

    @Override
    public final boolean isValid(final ID id, final ConstraintValidatorContext context) {
        return id != null && service.isExist(id);
    }
}
