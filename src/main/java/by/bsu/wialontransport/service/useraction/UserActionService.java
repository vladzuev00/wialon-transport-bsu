package by.bsu.wialontransport.service.useraction;

import by.bsu.wialontransport.controller.exception.NoSuchEntityException;
import by.bsu.wialontransport.crud.dto.Tracker;
import by.bsu.wialontransport.crud.dto.User;
import by.bsu.wialontransport.crud.service.TrackerService;
import by.bsu.wialontransport.model.DateInterval;
import by.bsu.wialontransport.model.form.ChangePasswordForm;
import by.bsu.wialontransport.model.form.TrackerForm;
import by.bsu.wialontransport.model.form.mapper.TrackerFormMapper;
import by.bsu.wialontransport.model.sortingkey.TrackerSortingKey;
import by.bsu.wialontransport.service.report.UserMovementReportBuildingService;
import by.bsu.wialontransport.service.security.service.SecurityService;
import by.bsu.wialontransport.service.useraction.changeinfo.ChangingUserInfoService;
import by.bsu.wialontransport.service.useraction.changeinfo.exception.password.PasswordChangingException;
import by.bsu.wialontransport.service.useraction.exception.TrackerImeiAlreadyExistsException;
import by.bsu.wialontransport.service.useraction.exception.TrackerPhoneNumberAlreadyExistsException;
import by.bsu.wialontransport.service.useraction.exception.TrackerUniqueConstraintException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

@Service
@RequiredArgsConstructor
public final class UserActionService {
    private static final String ATTRIBUTE_NAME_IMEI_ALREADY_EXISTS_ERROR = "imeiAlreadyExistsError";
    private static final String ATTRIBUTE_VALUE_IMEI_ALREADY_EXISTS_ERROR = "Imei already exists";

    private static final String ATTRIBUTE_NAME_PHONE_NUMBER_ALREADY_EXISTS_ERROR = "phoneNumberAlreadyExistsError";
    private static final String ATTRIBUTE_VALUE_PHONE_NUMBER_ALREADY_EXISTS_ERROR = "Phone number already exists";

    private final SecurityService securityService;
    private final TrackerService trackerService;
    private final TrackerFormMapper trackerFormMapper;
    private final ChangingUserInfoService changingUserInfoService;
    private final UserMovementReportBuildingService userMovementReportBuildingService;

    public void addAttributeOfTrackersToShowProfilePage(final int pageNumber,
                                                        final int pageSize,
                                                        final TrackerSortingKey sortingKey,
                                                        final Model model,
                                                        final String attributeName) {
        final List<Tracker> listedTrackers = this.findListedTrackers(pageNumber, pageSize, sortingKey);
        model.addAttribute(attributeName, listedTrackers);
    }

    public void addAttributeOfTrackerFormToAddTracker(final Model model, final String attributeName) {
        final TrackerForm trackerForm = new TrackerForm();
        model.addAttribute(attributeName, trackerForm);
    }

    public void addTracker(final TrackerForm trackerForm, final Model model)
            throws TrackerUniqueConstraintException {
        this.checkTrackerUniqueConstraints(trackerForm, model);
        final Tracker addedTracker = this.mapToTracker(trackerForm);
        this.trackerService.save(addedTracker);
    }

    public void addAttributeOfTrackerFormToUpdateTracker(final Long trackerId,
                                                         final Model model,
                                                         final String attributeName) {
        final TrackerForm trackerForm = this.findTrackerForm(trackerId);
        model.addAttribute(attributeName, trackerForm);
    }

    public void updateTracker(final TrackerForm trackerForm, final Model model)
            throws TrackerUniqueConstraintException {
        this.checkTrackerUniqueConstraints(trackerForm, model);
        final Tracker updatedTracker = this.mapToTracker(trackerForm);
        this.trackerService.update(updatedTracker);
    }

    public void deleteTracker(final Long trackerId) {
        this.trackerService.delete(trackerId);
    }

    public void addAttributeOfChangePasswordFormToChangePassword(final Model model, final String attributeName) {
        final ChangePasswordForm changePasswordForm = new ChangePasswordForm();
        model.addAttribute(attributeName, changePasswordForm);
    }

    public void updatePassword(final ChangePasswordForm form, final Model model)
            throws PasswordChangingException {
        try {
            final User loggedOnUser = this.securityService.findLoggedOnUser();
            this.changingUserInfoService.changePassword(loggedOnUser, form);
        } catch (final PasswordChangingException exception) {
            addErrorAttribute(model, exception);
            throw exception;
        }
    }

    //TODO: test
    public byte[] buildUserMovementReport(final DateInterval dateInterval) {
        final User loggedOnUser = this.securityService.findLoggedOnUser();
        return this.userMovementReportBuildingService.createReport(loggedOnUser, dateInterval);
    }

    private List<Tracker> findListedTrackers(final int pageNumber,
                                             final int pageSize,
                                             final TrackerSortingKey sortingKey) {
        final User loggedOnUser = this.securityService.findLoggedOnUser();
        final Comparator<Tracker> trackerComparator = sortingKey.getComparator();
//        return this.trackerService.findByUser(loggedOnUser, pageNumber, pageSize, trackerComparator);
        return null;
    }

    private TrackerForm findTrackerForm(final Long trackerId) {
        final Optional<Tracker> optionalTracker = this.trackerService.findById(trackerId);
        //TODO: add message
        final Tracker tracker = optionalTracker.orElseThrow(() -> new NoSuchEntityException(""));
        return this.trackerFormMapper.map(tracker);
    }

    private void checkTrackerUniqueConstraints(final TrackerForm trackerForm, final Model model)
            throws TrackerUniqueConstraintException {
        this.checkWhetherOtherTrackerWithGivenImeiExists(trackerForm, model);
        this.checkWhetherOtherTrackerWithGivenPhoneNumberExists(trackerForm, model);
    }

    private void checkWhetherOtherTrackerWithGivenImeiExists(final TrackerForm trackerForm, final Model model)
            throws TrackerImeiAlreadyExistsException {
        this.checkWhetherOtherTrackerWithGivenPropertyExists(
                trackerForm,
                TrackerForm::getImei,
                TrackerService::findByImei,
                model,
                UserActionService::addErrorAttributeOfImeiAlreadyExists,
                TrackerImeiAlreadyExistsException::new
        );
    }

    private static void addErrorAttributeOfImeiAlreadyExists(final Model model) {
        model.addAttribute(ATTRIBUTE_NAME_IMEI_ALREADY_EXISTS_ERROR, ATTRIBUTE_VALUE_IMEI_ALREADY_EXISTS_ERROR);
    }

    private void checkWhetherOtherTrackerWithGivenPhoneNumberExists(final TrackerForm trackerForm, final Model model)
            throws TrackerPhoneNumberAlreadyExistsException {
        this.checkWhetherOtherTrackerWithGivenPropertyExists(
                trackerForm,
                TrackerForm::getPhoneNumber,
                TrackerService::findByPhoneNumber,
                model,
                UserActionService::addErrorAttributeOfPhoneNumberAlreadyExists,
                TrackerPhoneNumberAlreadyExistsException::new
        );
    }

    private static void addErrorAttributeOfPhoneNumberAlreadyExists(final Model model) {
        model.addAttribute(
                ATTRIBUTE_NAME_PHONE_NUMBER_ALREADY_EXISTS_ERROR, ATTRIBUTE_VALUE_PHONE_NUMBER_ALREADY_EXISTS_ERROR
        );
    }

    private Tracker mapToTracker(final TrackerForm trackerForm) {
        final User loggedOnUser = this.securityService.findLoggedOnUser();
        return this.trackerFormMapper.map(trackerForm, loggedOnUser);
    }

    private <P, E extends TrackerUniqueConstraintException> void checkWhetherOtherTrackerWithGivenPropertyExists(
            final TrackerForm trackerForm,
            final Function<TrackerForm, P> getter,
            final BiFunction<TrackerService, P, Optional<Tracker>> searchingFunction,
            final Model model,
            final Consumer<Model> errorAttributeAdder,
            final Supplier<E> exceptionSupplier) throws E {
        final Optional<Tracker> optionalOtherTrackerWithGivenProperty = this.findOtherTrackerWithGivenProperty(
                trackerForm, getter, searchingFunction
        );
        if (optionalOtherTrackerWithGivenProperty.isPresent()) {
            errorAttributeAdder.accept(model);
            throw exceptionSupplier.get();
        }
    }

    private <P> Optional<Tracker> findOtherTrackerWithGivenProperty(final TrackerForm trackerForm,
                                                                    final Function<TrackerForm, P> getter,
                                                                    final BiFunction<TrackerService, P, Optional<Tracker>> searchingFunction) {
        final P property = getter.apply(trackerForm);
        final Optional<Tracker> optionalTracker = searchingFunction.apply(this.trackerService, property);
        return optionalTracker.filter(tracker -> areDifferentTrackers(tracker, trackerForm));
    }

    private static boolean areDifferentTrackers(final Tracker tracker, final TrackerForm trackerForm) {
        final Long trackerId = tracker.getId();
        final Long trackerFormId = trackerForm.getId();
        return !Objects.equals(trackerId, trackerFormId);
    }

    private static void addErrorAttribute(final Model model, final PasswordChangingException exception) {
        final String attributeName = exception.findErrorAttributeName();
        final String attributeValue = exception.findErrorAttributeValue();
        model.addAttribute(attributeName, attributeValue);
    }

}
