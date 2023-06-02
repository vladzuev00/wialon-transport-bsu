package by.bsu.wialontransport.controller.useraction;

import by.bsu.wialontransport.controller.exception.NoSuchEntityException;
import by.bsu.wialontransport.crud.dto.Tracker;
import by.bsu.wialontransport.crud.dto.User;
import by.bsu.wialontransport.crud.service.TrackerService;
import by.bsu.wialontransport.model.form.TrackerForm;
import by.bsu.wialontransport.model.form.mapper.TrackerFormMapper;
import by.bsu.wialontransport.model.sortingkey.TrackerSortingKey;
import by.bsu.wialontransport.security.service.SecurityService;
import by.bsu.wialontransport.service.useraction.UserActionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Optional;

@Controller
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserActionController {
    private static final String MODEL_ATTRIBUTE_NAME_OF_LISTED_TRACKERS = "listed_trackers";
    private static final String MODEL_ATTRIBUTE_NAME_OF_UPDATED_TRACKER = "updated_tracker_form";

    private static final String VIEW_NAME_PROFILE_PAGE = "user_profile_page";
    private static final String VIEW_NAME_UPDATE_TRACKER_PAGE = "update_tracker";

    private static final String VIEW_TO_REDIRECT_TO_PROFILE_PAGE = "redirect:/user/profile"
            + "?pageNumber=0&pageSize=5&trackerSortingKey=IMEI";

    private final UserActionService userActionService;

    private final SecurityService securityService;
    private final TrackerService trackerService;
    private final TrackerFormMapper trackerFormMapper;

    @GetMapping("/profile")
    public String showProfilePage(@RequestParam(name = "pageNumber") final int pageNumber,
                                  @RequestParam(name = "pageSize") final int pageSize,
                                  @RequestParam(name = "trackerSortingKey", required = false) final TrackerSortingKey sortingKey,
                                  final Model model) {
        this.userActionService.addAttributeOfTrackersToShowProfilePage(
                pageNumber, pageSize, sortingKey, model, MODEL_ATTRIBUTE_NAME_OF_LISTED_TRACKERS
        );
        return VIEW_NAME_PROFILE_PAGE;
    }

    @GetMapping("/updateTracker")
    public String showUpdateTrackerPage(@RequestParam(name = "trackerId") final Long trackerId,
                                        final Model model) {
        final Optional<Tracker> optionalTracker = this.trackerService.findById(trackerId);
        final Tracker tracker = optionalTracker.orElseThrow(NoSuchEntityException::new);
        final TrackerForm trackerForm = this.trackerFormMapper.map(tracker);
        model.addAttribute(MODEL_ATTRIBUTE_NAME_OF_UPDATED_TRACKER, trackerForm);
        return VIEW_NAME_UPDATE_TRACKER_PAGE;
    }

    @PostMapping("/updateTracker")
    public String updateTracker(@Valid @ModelAttribute(MODEL_ATTRIBUTE_NAME_OF_UPDATED_TRACKER) final TrackerForm trackerForm,
                                final BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return VIEW_NAME_UPDATE_TRACKER_PAGE;
        }
        final User user = this.securityService.findLoggedOnUser();
        final Tracker tracker = this.trackerFormMapper.map(trackerForm, user);
        this.trackerService.update(tracker);
        return VIEW_TO_REDIRECT_TO_PROFILE_PAGE;
    }

}
