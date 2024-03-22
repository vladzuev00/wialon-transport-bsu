package by.bsu.wialontransport.validation.validator.unique;

import by.bsu.wialontransport.crud.service.TrackerService;
import by.bsu.wialontransport.validation.annotation.UniqueTrackerImei;
import org.springframework.stereotype.Component;

@Component
public final class UniqueTrackerImeiValidator extends UniquePropertyValidator<UniqueTrackerImei, Long,  String, TrackerService> {

    public UniqueTrackerImeiValidator(final TrackerService service) {
        super(service);
    }
}
