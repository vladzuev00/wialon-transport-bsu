package by.bsu.wialontransport.validation.validator.unique;

import by.bsu.wialontransport.crud.service.CRUDService;
import lombok.RequiredArgsConstructor;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.lang.annotation.Annotation;

@RequiredArgsConstructor
public abstract class UniquePropertyValidator<A extends Annotation, V, S extends CRUDService<?, ?, ?, ?, ?>>
        implements ConstraintValidator<A, V> {
    private final S service;

    @Override
    public final boolean isValid(final V value, final ConstraintValidatorContext context) {
        return !isExist(service, value);
    }

    protected abstract boolean isExist(final S service, final V value);
}
