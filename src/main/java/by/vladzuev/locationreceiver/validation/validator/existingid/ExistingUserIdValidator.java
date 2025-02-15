package by.vladzuev.locationreceiver.validation.validator.existingid;

import by.vladzuev.locationreceiver.crud.service.UserService;
import by.vladzuev.locationreceiver.validation.annotation.ExistingUserId;
import org.springframework.stereotype.Component;

@Component
public final class ExistingUserIdValidator extends ExistingEntityIdValidator<ExistingUserId, Long> {

    public ExistingUserIdValidator(final UserService service) {
        super(service);
    }
}
