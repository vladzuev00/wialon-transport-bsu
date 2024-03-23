package by.bsu.wialontransport.validation.validator.existingid;

import by.bsu.wialontransport.crud.service.UserService;
import by.bsu.wialontransport.validation.annotation.ExistingUserId;
import org.springframework.stereotype.Component;

@Component
public final class ExistingUserIdValidator extends ExistingEntityIdValidator<ExistingUserId, Long> {

    public ExistingUserIdValidator(final UserService service) {
        super(service);
    }
}
