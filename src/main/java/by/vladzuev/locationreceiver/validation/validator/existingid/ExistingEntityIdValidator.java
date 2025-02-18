package by.vladzuev.locationreceiver.validation.validator.existingid;

import by.vladzuev.locationreceiver.crud.service.CRUDService;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.RequiredArgsConstructor;

import java.lang.annotation.Annotation;

@RequiredArgsConstructor
public abstract class ExistingEntityIdValidator<A extends Annotation, ID> implements ConstraintValidator<A, ID> {
    private final CRUDService<ID, ?, ?, ?, ?> service;

    @Override
    public final boolean isValid(final ID id, final ConstraintValidatorContext context) {
        return id != null && service.isExist(id);
    }
}
