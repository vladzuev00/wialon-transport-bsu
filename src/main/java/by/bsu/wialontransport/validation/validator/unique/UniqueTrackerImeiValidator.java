package by.bsu.wialontransport.validation.validator.unique;

import by.bsu.wialontransport.controller.tracker.view.TrackerView;
import by.bsu.wialontransport.crud.dto.Tracker;
import by.bsu.wialontransport.crud.service.TrackerService;
import by.bsu.wialontransport.validation.annotation.UniqueTrackerImei;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public final class UniqueTrackerImeiValidator extends UniquePropertyValidator<UniqueTrackerImei, Tracker, TrackerView, String, TrackerService> {

    public UniqueTrackerImeiValidator(final TrackerService service) {
        super(service);
    }

    @Override
    protected String getProperty(final TrackerView view) {
        return view.getImei();
    }

    @Override
    protected Optional<Tracker> findByProperty(final String imei, final TrackerService service) {
        return service.findByImei(imei);
    }
}
