package by.bsu.wialontransport.validation.validator.existingid;

import by.bsu.wialontransport.crud.service.TrackerService;
import by.bsu.wialontransport.validation.annotation.ExistingTrackerId;
import org.springframework.stereotype.Component;

@Component
public final class ExistingTrackerIdValidator extends ExistingEntityIdValidator<ExistingTrackerId, Long> {

    public ExistingTrackerIdValidator(final TrackerService service) {
        super(service);
    }
}
