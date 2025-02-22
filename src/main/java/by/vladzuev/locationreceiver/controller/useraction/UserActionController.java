package by.vladzuev.locationreceiver.controller.useraction;

import by.vladzuev.locationreceiver.model.form.ChangePasswordForm;
import by.vladzuev.locationreceiver.model.form.TrackerForm;
import by.vladzuev.locationreceiver.model.sortingkey.TrackerSortingKey;
import by.vladzuev.locationreceiver.service.useraction.UserActionService;
import by.vladzuev.locationreceiver.service.useraction.changeinfo.exception.password.PasswordChangingException;
import by.vladzuev.locationreceiver.service.useraction.exception.TrackerUniqueConstraintException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserActionController {
    private static final String MODEL_ATTRIBUTE_NAME_LISTED_TRACKERS = "listed_trackers";
    private static final String MODEL_ATTRIBUTE_NAME_ADDED_TRACKER_FORM = "added_tracker_form";
    private static final String MODEL_ATTRIBUTE_NAME_UPDATED_TRACKER_FORM = "updated_tracker_form";
    private static final String MODEL_ATTRIBUTE_NAME_CHANGE_PASSWORD_FORM = "change_password_form";

    private static final String VIEW_NAME_PROFILE_PAGE = "user_profile_page";
    private static final String VIEW_NAME_ADD_TRACKER_PAGE = "add_tracker";
    private static final String VIEW_NAME_UPDATE_TRACKER_PAGE = "update_tracker";
    private static final String VIEW_NAME_CHANGE_PASSWORD_PAGE = "change_password";

    private static final String VIEW_TO_REDIRECT_TO_PROFILE_PAGE = "redirect:/user/profile";

    private final UserActionService userActionService;

    @GetMapping("/profile")
    public String showProfilePage(@RequestParam(name = "pageNumber", defaultValue = "0") final int pageNumber,
                                  @RequestParam(name = "pageSize", defaultValue = "5") final int pageSize,
                                  @RequestParam(name = "trackerSortingKey", defaultValue = "IMEI") final TrackerSortingKey sortingKey,
                                  final Model model) {
        userActionService.addUserTrackersAsAttribute(
                PageRequest.of(pageNumber, pageSize),
                sortingKey,
                model,
                MODEL_ATTRIBUTE_NAME_LISTED_TRACKERS
        );
        return VIEW_NAME_PROFILE_PAGE;
    }

    @GetMapping("/addTracker")
    public String showAddTrackerPage(final Model model) {
        userActionService.addTrackerFormAsAttribute(model, MODEL_ATTRIBUTE_NAME_ADDED_TRACKER_FORM);
        return VIEW_NAME_ADD_TRACKER_PAGE;
    }

    @PostMapping("/addTracker")
    public String addTracker(@Valid @ModelAttribute(MODEL_ATTRIBUTE_NAME_ADDED_TRACKER_FORM) final TrackerForm form,
                             final BindingResult bindingResult,
                             final Model model) {
        //TODO: refactor
        try {
            if (bindingResult.hasErrors()) {
                return VIEW_NAME_ADD_TRACKER_PAGE;
            }
            userActionService.addTracker(form, model);
            return VIEW_TO_REDIRECT_TO_PROFILE_PAGE;
        } catch (final TrackerUniqueConstraintException exception) {
            return VIEW_NAME_ADD_TRACKER_PAGE;
        }
    }

    @GetMapping("/updateTracker")
    public String showUpdateTrackerPage(@RequestParam(name = "trackerId") final Long trackerId,
                                        final Model model) {
        userActionService.addTrackerFormAsAttribute(trackerId, model, MODEL_ATTRIBUTE_NAME_UPDATED_TRACKER_FORM);
        return VIEW_NAME_UPDATE_TRACKER_PAGE;
    }

    @PostMapping("/updateTracker")
    public String updateTracker(@Valid @ModelAttribute(MODEL_ATTRIBUTE_NAME_UPDATED_TRACKER_FORM) final TrackerForm form,
                                final BindingResult bindingResult,
                                final Model model) {
        //TODO: refactor
        try {
            if (bindingResult.hasErrors()) {
                return VIEW_NAME_UPDATE_TRACKER_PAGE;
            }
            userActionService.updateTracker(form, model);
            return VIEW_TO_REDIRECT_TO_PROFILE_PAGE;
        } catch (final TrackerUniqueConstraintException exception) {
            return VIEW_NAME_UPDATE_TRACKER_PAGE;
        }
    }

    @GetMapping("/deleteTracker")
    public String deleteTracker(@RequestParam(name = "trackerId") final Long trackerId) {
        userActionService.deleteTracker(trackerId);
        return VIEW_TO_REDIRECT_TO_PROFILE_PAGE;
    }

    @GetMapping("/changePassword")
    public String showChangePasswordPage(final Model model) {
        userActionService.addChangePasswordFormAsAttribute(model, MODEL_ATTRIBUTE_NAME_CHANGE_PASSWORD_FORM);
        return VIEW_NAME_CHANGE_PASSWORD_PAGE;
    }

    @PostMapping("/changePassword")
    public String changePassword(@Valid @ModelAttribute(MODEL_ATTRIBUTE_NAME_CHANGE_PASSWORD_FORM) final ChangePasswordForm form,
                                 final BindingResult bindingResult,
                                 final Model model) {
        //TODO: refactor
        try {
            if (bindingResult.hasErrors()) {
                return VIEW_NAME_CHANGE_PASSWORD_PAGE;
            }
            userActionService.updatePassword(form, model);
            return VIEW_TO_REDIRECT_TO_PROFILE_PAGE;
        } catch (final PasswordChangingException exception) {
            return VIEW_NAME_CHANGE_PASSWORD_PAGE;
        }
    }
}
