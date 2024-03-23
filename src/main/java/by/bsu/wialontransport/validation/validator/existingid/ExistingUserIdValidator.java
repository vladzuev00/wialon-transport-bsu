package by.bsu.wialontransport.validation.validator.existingid;

import by.bsu.wialontransport.crud.service.CRUDService;
import by.bsu.wialontransport.validation.annotation.ExistingUserId;
import org.springframework.stereotype.Component;

@Component
public final class ExistingUserIdValidator extends ExistingEntityIdValidator<ExistingUserId, Long> {

    public ExistingUserIdValidator(final CRUDService<Long, ?, ?, ?, ?> service) {
        super(service);
    }
}
