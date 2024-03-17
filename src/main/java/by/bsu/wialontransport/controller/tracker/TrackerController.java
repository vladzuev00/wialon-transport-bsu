package by.bsu.wialontransport.controller.tracker;

import by.bsu.wialontransport.controller.abstraction.CRUDController;
import by.bsu.wialontransport.controller.tracker.view.SaveTrackerView;
import by.bsu.wialontransport.controller.tracker.view.TrackerView;
import by.bsu.wialontransport.controller.tracker.view.UpdateTrackerView;
import by.bsu.wialontransport.crud.dto.Tracker;
import by.bsu.wialontransport.crud.service.TrackerService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RestController
@RequestMapping("/tracker")
public class TrackerController extends CRUDController<Long, Tracker, TrackerService, TrackerView, SaveTrackerView, UpdateTrackerView> {

    public TrackerController(final TrackerService service) {
        super(service);
    }

    @Override
    protected TrackerView createResponseView(final Tracker dto) {
        return new TrackerView(dto.getId(), dto.getImei(), dto.getPhoneNumber());
    }
}
