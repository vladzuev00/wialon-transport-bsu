package by.bsu.wialontransport.controller.tracker;

import by.bsu.wialontransport.controller.abstraction.CRUDController;
import by.bsu.wialontransport.controller.tracker.view.SaveTrackerView;
import by.bsu.wialontransport.controller.tracker.view.ResponseTrackerView;
import by.bsu.wialontransport.controller.tracker.view.UpdateTrackerView;
import by.bsu.wialontransport.crud.dto.Tracker;
import by.bsu.wialontransport.crud.dto.User;
import by.bsu.wialontransport.crud.service.TrackerService;
import by.bsu.wialontransport.crud.service.UserService;
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
        return new ResponseTrackerView(dto.getId(), dto.getImei(), dto.getPhoneNumber());
    }

    private User findUser(final SaveTrackerView view) {
        return findRelation(userService, view.getUserId());
    }
}
