package by.vladzuev.locationreceiver.validation.validator.existingid;

import by.vladzuev.locationreceiver.crud.service.TrackerService;
import by.vladzuev.locationreceiver.validation.annotation.ExistingTrackerId;
import org.springframework.stereotype.Component;

@Component
public final class ExistingTrackerIdValidator extends ExistingEntityIdValidator<ExistingTrackerId, Long> {

    public ExistingTrackerIdValidator(final TrackerService service) {
        super(service);
    }
}
