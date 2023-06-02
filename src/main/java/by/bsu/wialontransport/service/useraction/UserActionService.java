package by.bsu.wialontransport.service.useraction;

import by.bsu.wialontransport.crud.dto.Tracker;
import by.bsu.wialontransport.crud.dto.User;
import by.bsu.wialontransport.crud.service.TrackerService;
import by.bsu.wialontransport.model.sortingkey.TrackerSortingKey;
import by.bsu.wialontransport.security.service.SecurityService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.util.List;

@Service
@RequiredArgsConstructor
public final class UserActionService {
    private final SecurityService securityService;
    private final TrackerService trackerService;

    public void addAttributeOfTrackersToShowProfilePage(final int pageNumber,
                                                        final int pageSize,
                                                        final TrackerSortingKey sortingKey,
                                                        final Model model,
                                                        final String attributeName) {
        final List<Tracker> listedTrackers = this.findListedTrackers(pageNumber, pageSize, sortingKey);
        model.addAttribute(attributeName, listedTrackers);
    }

    private List<Tracker> findListedTrackers(final int pageNumber,
                                             final int pageSize,
                                             final TrackerSortingKey sortingKey) {
        final User loggedOnUser = this.securityService.findLoggedOnUser();
        return sortingKey != null
                ? this.trackerService.findByUser(loggedOnUser, pageNumber, pageSize, sortingKey.getComparator())
                : this.trackerService.findByUser(loggedOnUser, pageNumber, pageSize);
    }

}
