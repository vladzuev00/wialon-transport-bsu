package by.vladzuev.locationreceiver.validation.validator.unique;

import by.vladzuev.locationreceiver.controller.abstraction.View;
import by.vladzuev.locationreceiver.crud.dto.Dto;
import by.vladzuev.locationreceiver.crud.service.CRUDService;
import lombok.RequiredArgsConstructor;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.lang.annotation.Annotation;
import java.util.Objects;
import java.util.Optional;

@RequiredArgsConstructor
public abstract class UniquePropertyValidator<
        ANNOTATION extends Annotation,
        DTO extends Dto<?>,
        VIEW extends View<?>,
        PROPERTY,
        SERVICE extends CRUDService<?, ?, DTO, ?, ?>
        >
        implements ConstraintValidator<ANNOTATION, VIEW> {
    private final SERVICE service;

    @Override
    public final boolean isValid(final VIEW view, final ConstraintValidatorContext context) {
        return findByProperty(view)
                .filter(dto -> isViewDuplicateDto(view, dto))
                .isEmpty();
    }

    protected abstract PROPERTY getProperty(final VIEW view);

    protected abstract Optional<DTO> findByProperty(final PROPERTY property, final SERVICE service);

    private Optional<DTO> findByProperty(final VIEW view) {
        final PROPERTY property = getProperty(view);
        return findByProperty(property, service);
    }

    private boolean isViewDuplicateDto(final VIEW view, final DTO dto) {
        return view.findId()
                .filter(viewId -> Objects.equals(viewId, dto.getId()))
                .isEmpty();
    }
}
