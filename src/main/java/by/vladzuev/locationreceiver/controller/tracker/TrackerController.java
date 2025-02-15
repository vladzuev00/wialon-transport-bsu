package by.vladzuev.locationreceiver.controller.tracker;

import by.vladzuev.locationreceiver.controller.abstraction.CRUDController;
import by.vladzuev.locationreceiver.controller.tracker.view.ResponseTrackerView;
import by.vladzuev.locationreceiver.controller.tracker.view.SaveTrackerView;
import by.vladzuev.locationreceiver.controller.tracker.view.UpdateTrackerView;
import by.vladzuev.locationreceiver.crud.dto.Tracker;
import by.vladzuev.locationreceiver.crud.dto.User;
import by.vladzuev.locationreceiver.crud.service.TrackerService;
import by.vladzuev.locationreceiver.crud.service.UserService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Validated
@RestController
@RequestMapping("/tracker")
public class TrackerController extends CRUDController<Long, Tracker, TrackerService, ResponseTrackerView, SaveTrackerView, UpdateTrackerView> {
    private final UserService userService;

    public TrackerController(final TrackerService service, final UserService userService) {
        super(service);
        this.userService = userService;
    }

    @Override
    protected Tracker createDtoBySaveView(final SaveTrackerView view) {
        return Tracker.builder()
                .imei(view.getImei())
                .password(view.getPassword())
                .phoneNumber(view.getPhoneNumber())
                .user(findUser(view))
                .build();
    }

    @Override
    protected Tracker createDtoByUpdateView(final UpdateTrackerView view) {
        return Tracker.builder()
                .id(view.getId())
                .imei(view.getImei())
                .password(view.getPassword())
                .phoneNumber(view.getPhoneNumber())
                .build();
    }

    @Override
    protected ResponseTrackerView createResponseView(final Tracker dto) {
        return new ResponseTrackerView(dto.getImei(), dto.getPhoneNumber(), dto.getId());
    }

    private User findUser(final SaveTrackerView view) {
        return findRelation(userService, view.getUserId());
    }
}
