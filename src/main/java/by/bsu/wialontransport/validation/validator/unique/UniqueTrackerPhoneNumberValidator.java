package by.bsu.wialontransport.validation.validator.unique;

import by.bsu.wialontransport.crud.service.TrackerService;
import by.bsu.wialontransport.validation.annotation.UniqueTrackerPhoneNumber;
import org.springframework.stereotype.Component;

@Component
public final class UniqueTrackerPhoneNumberValidator extends UniquePropertyValidator<UniqueTrackerPhoneNumber, String, TrackerService> {

    public UniqueTrackerPhoneNumberValidator(final TrackerService service) {
        super(service);
    }

    @Override
    protected boolean isExist(final TrackerService service, final String value) {
        return service.isExistByPhoneNumber(value);
    }
}
