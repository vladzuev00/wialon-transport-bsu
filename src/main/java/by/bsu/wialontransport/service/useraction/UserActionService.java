package by.bsu.wialontransport.service.useraction;

import by.bsu.wialontransport.controller.exception.NoSuchEntityException;
import by.bsu.wialontransport.crud.dto.Tracker;
import by.bsu.wialontransport.crud.dto.User;
import by.bsu.wialontransport.crud.service.TrackerService;
import by.bsu.wialontransport.model.form.TrackerForm;
import by.bsu.wialontransport.model.form.mapper.TrackerFormMapper;
import by.bsu.wialontransport.model.sortingkey.TrackerSortingKey;
import by.bsu.wialontransport.security.service.SecurityService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public final class UserActionService {
    private final SecurityService securityService;
    private final TrackerService trackerService;
    private final TrackerFormMapper trackerFormMapper;

    public void addAttributeOfTrackersToShowProfilePage(final int pageNumber,
                                                        final int pageSize,
                                                        final TrackerSortingKey sortingKey,
                                                        final Model model,
                                                        final String attributeName) {
        final List<Tracker> listedTrackers = this.findListedTrackers(pageNumber, pageSize, sortingKey);
        model.addAttribute(attributeName, listedTrackers);
    }

    public void addAttributeOfTrackerFormToUpdateTracker(final Long trackerId,
                                                         final Model model,
                                                         final String attributeName) {
        final Optional<Tracker> optionalTracker = this.trackerService.findById(trackerId);
        final Tracker tracker = optionalTracker.orElseThrow(NoSuchEntityException::new);
        final TrackerForm trackerForm = this.trackerFormMapper.map(tracker);
        model.addAttribute(attributeName, trackerForm);
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
