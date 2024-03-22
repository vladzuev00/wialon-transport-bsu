package by.bsu.wialontransport.validation.validator.unique;

import by.bsu.wialontransport.controller.tracker.view.TrackerView;
import by.bsu.wialontransport.crud.dto.Tracker;
import by.bsu.wialontransport.crud.service.TrackerService;
import by.bsu.wialontransport.validation.annotation.UniqueTrackerPhoneNumber;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public final class UniqueTrackerPhoneNumberValidator extends UniquePropertyValidator<UniqueTrackerPhoneNumber, Tracker, TrackerView, String, TrackerService> {

    public UniqueTrackerPhoneNumberValidator(final TrackerService service) {
        super(service);
    }


    @Override
    protected String getProperty(final TrackerView view) {
        return view.getPhoneNumber();
    }

    @Override
    protected Optional<Tracker> findByProperty(final String phoneNumber, final TrackerService service) {
        return service.findByPhoneNumber(phoneNumber);
    }
}
