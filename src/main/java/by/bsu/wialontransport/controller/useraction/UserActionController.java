package by.bsu.wialontransport.controller.useraction;

import by.bsu.wialontransport.model.form.TrackerForm;
import by.bsu.wialontransport.model.sortingkey.TrackerSortingKey;
import by.bsu.wialontransport.service.useraction.UserActionService;
import by.bsu.wialontransport.service.useraction.exception.TrackerUniqueConstraintException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

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
        this.userActionService.addAttributeOfTrackerFormToUpdateTracker(
                trackerId, model, MODEL_ATTRIBUTE_NAME_OF_UPDATED_TRACKER
        );
        return VIEW_NAME_UPDATE_TRACKER_PAGE;
    }

    @PostMapping("/updateTracker")
    public String updateTracker(@Valid @ModelAttribute(MODEL_ATTRIBUTE_NAME_OF_UPDATED_TRACKER) final TrackerForm trackerForm,
                                final BindingResult bindingResult,
                                final Model model) {
        try {
            if (bindingResult.hasErrors()) {
                return VIEW_NAME_UPDATE_TRACKER_PAGE;
            }
            this.userActionService.updateTracker(trackerForm, model);
            return VIEW_TO_REDIRECT_TO_PROFILE_PAGE;
        } catch (final TrackerUniqueConstraintException exception) {
            return VIEW_NAME_UPDATE_TRACKER_PAGE;
        }
    }

}
