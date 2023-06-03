package by.bsu.wialontransport.service.useraction;

import by.bsu.wialontransport.controller.exception.NoSuchEntityException;
import by.bsu.wialontransport.crud.dto.Tracker;
import by.bsu.wialontransport.crud.dto.User;
import by.bsu.wialontransport.crud.service.TrackerService;
import by.bsu.wialontransport.model.form.TrackerForm;
import by.bsu.wialontransport.model.form.mapper.TrackerFormMapper;
import by.bsu.wialontransport.model.sortingkey.TrackerSortingKey;
import by.bsu.wialontransport.security.service.SecurityService;
import by.bsu.wialontransport.service.useraction.exception.TrackerImeiAlreadyExistsException;
import by.bsu.wialontransport.service.useraction.exception.TrackerPhoneNumberAlreadyExistsException;
import by.bsu.wialontransport.service.useraction.exception.TrackerUniqueConstraintException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public final class UserActionService {
    private static final String ATTRIBUTE_NAME_IMEI_ALREADY_EXISTS_ERROR = "imeiAlreadyExistsError";
    private static final String ATTRIBUTE_VALUE_IMEI_ALREADY_EXISTS = "Imei already exists";

    private static final String ATTRIBUTE_NAME_PHONE_NUMBER_ALREADY_EXISTS_ERROR = "phoneNumberAlreadyExistsError";
    private static final String ATTRIBUTE_VALUE_PHONE_NUMBER_ALREADY_EXISTS = "Phone number already exists";

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
        final TrackerForm trackerForm = this.findTrackerForm(trackerId);
        model.addAttribute(attributeName, trackerForm);
    }

    public void updateTracker(final TrackerForm trackerForm, final Model model)
            throws TrackerUniqueConstraintException {
        this.checkWhetherImeiAlreadyExists(trackerForm, model);
        this.checkWhetherPhoneNumberAlreadyExists(trackerForm, model);
        final Tracker updatedTracker = this.mapToTracker(trackerForm);
        this.trackerService.update(updatedTracker);
    }

    private List<Tracker> findListedTrackers(final int pageNumber,
                                             final int pageSize,
                                             final TrackerSortingKey sortingKey) {
        final User loggedOnUser = this.securityService.findLoggedOnUser();
        return sortingKey != null
                ? this.trackerService.findByUser(loggedOnUser, pageNumber, pageSize, sortingKey.getComparator())
                : this.trackerService.findByUser(loggedOnUser, pageNumber, pageSize);
    }

    private TrackerForm findTrackerForm(final Long trackerId) {
        final Optional<Tracker> optionalTracker = this.trackerService.findById(trackerId);
        final Tracker tracker = optionalTracker.orElseThrow(NoSuchEntityException::new);
        return this.trackerFormMapper.map(tracker);
    }

    private void checkWhetherImeiAlreadyExists(final TrackerForm trackerForm, final Model model)
            throws TrackerImeiAlreadyExistsException {
        final Optional<Tracker> optionalOtherTrackerWithGivenImei = this.findOtherTrackerWithGivenImei(trackerForm);
        if (optionalOtherTrackerWithGivenImei.isPresent()) {
            handleCaseTrackerImeiAlreadyExists(model);
        }
    }

    private Optional<Tracker> findOtherTrackerWithGivenImei(final TrackerForm trackerForm) {
        final String imei = trackerForm.getImei();
        final Optional<Tracker> optionalTrackerWithGivenImei = this.trackerService.findByImei(imei);
        return optionalTrackerWithGivenImei
                .filter(trackerWithGivenImei -> !isSameTracker(trackerWithGivenImei, trackerForm));
    }

    private static boolean isSameTracker(final Tracker tracker, final TrackerForm trackerForm) {
        final Long trackerId = tracker.getId();
        final Long trackerFormId = trackerForm.getId();
        return Objects.equals(trackerId, trackerFormId);
    }

    private static void handleCaseTrackerImeiAlreadyExists(final Model model)
            throws TrackerImeiAlreadyExistsException {
        addErrorMessageOfImeiAlreadyExists(model);
        throw new TrackerImeiAlreadyExistsException();
    }

    private static void addErrorMessageOfImeiAlreadyExists(final Model model) {
        model.addAttribute(ATTRIBUTE_NAME_IMEI_ALREADY_EXISTS_ERROR, ATTRIBUTE_VALUE_IMEI_ALREADY_EXISTS);
    }

    private void checkWhetherPhoneNumberAlreadyExists(final TrackerForm trackerForm, final Model model)
            throws TrackerPhoneNumberAlreadyExistsException {
        final Optional<Tracker> optionalOtherTrackerWithGivenPhoneNumber = this.findOtherTrackerWithGivenPhoneNumber(
                trackerForm, model
        );
        if (optionalOtherTrackerWithGivenPhoneNumber.isPresent()) {
            handleCaseTrackerPhoneNumberAlreadyExists(model);
        }
    }

    private Optional<Tracker> findOtherTrackerWithGivenPhoneNumber(final TrackerForm trackerForm, final Model model) {
        final String phoneNumber = trackerForm.getPhoneNumber();
        final Optional<Tracker> optionalTrackerWithGivenImei = this.trackerService.findByPhoneNumber(phoneNumber);
        return optionalTrackerWithGivenImei
                .filter(trackerWithGivenImei -> !isSameTracker(trackerWithGivenImei, trackerForm));
    }

    private static void handleCaseTrackerPhoneNumberAlreadyExists(final Model model)
            throws TrackerPhoneNumberAlreadyExistsException {
        addErrorMessageOfPhoneNumberAlreadyExists(model);
        throw new TrackerPhoneNumberAlreadyExistsException();
    }

    private static void addErrorMessageOfPhoneNumberAlreadyExists(final Model model) {
        model.addAttribute(
                ATTRIBUTE_NAME_PHONE_NUMBER_ALREADY_EXISTS_ERROR, ATTRIBUTE_VALUE_PHONE_NUMBER_ALREADY_EXISTS
        );
    }

    private Tracker mapToTracker(final TrackerForm trackerForm) {
        final User loggedOnUser = this.securityService.findLoggedOnUser();
        return this.trackerFormMapper.map(trackerForm, loggedOnUser);
    }

}
