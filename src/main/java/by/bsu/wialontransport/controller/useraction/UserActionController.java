package by.bsu.wialontransport.controller.useraction;

import by.bsu.wialontransport.crud.dto.Tracker;
import by.bsu.wialontransport.crud.dto.User;
import by.bsu.wialontransport.crud.service.TrackerService;
import by.bsu.wialontransport.model.sortingkey.TrackerSortingKey;
import by.bsu.wialontransport.security.service.SecurityService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserActionController {
    private static final String MODEL_ATTRIBUTE_NAME_OF_LISTED_TRACKERS = "listed_trackers";
    private static final String VIEW_NAME_PROFILE_PAGE = "user_profile_page";

    private final SecurityService securityService;
    private final TrackerService trackerService;

    @GetMapping("/profile")
    public String showProfilePage(@RequestParam(name = "pageNumber") final int pageNumber,
                                  @RequestParam(name = "pageSize") final int pageSize,
                                  @RequestParam(name = "trackerSortingKey", required = false) final TrackerSortingKey trackerSortingKey,
                                  final Model model) {
        final User loggedOnUser = this.securityService.findLoggedOnUser();
        final List<Tracker> listedTrackers = this.findListedTrackers(
                loggedOnUser, pageNumber, pageSize, trackerSortingKey
        );
        model.addAttribute(MODEL_ATTRIBUTE_NAME_OF_LISTED_TRACKERS, listedTrackers);
        return VIEW_NAME_PROFILE_PAGE;
    }

    private List<Tracker> findListedTrackers(final User loggedOnUser,
                                             final int pageNumber,
                                             final int pageSize,
                                             final TrackerSortingKey sortingKey) {
        return sortingKey != null
                ? this.trackerService.findByUser(loggedOnUser, pageNumber, pageSize, sortingKey.getComparator())
                : this.trackerService.findByUser(loggedOnUser, pageNumber, pageSize);
    }

}
