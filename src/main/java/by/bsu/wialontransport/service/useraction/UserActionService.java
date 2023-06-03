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
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

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
        this.checkWhetherOtherTrackerWithGivenImeiExists(trackerForm, model);
        this.checkWhetherOtherTrackerWithGivenPhoneNumberExists(trackerForm, model);
        final Tracker updatedTracker = this.mapToTracker(trackerForm);
        this.trackerService.update(updatedTracker);
    }

    private List<Tracker> findListedTrackers(final int pageNumber,
                                             final int pageSize,
                                             final TrackerSortingKey sortingKey) {
        final User loggedOnUser = this.securityService.findLoggedOnUser();
        return this.trackerService.findByUser(loggedOnUser, pageNumber, pageSize, sortingKey.getComparator());
    }

    private TrackerForm findTrackerForm(final Long trackerId) {
        final Optional<Tracker> optionalTracker = this.trackerService.findById(trackerId);
        final Tracker tracker = optionalTracker.orElseThrow(NoSuchEntityException::new);
        return this.trackerFormMapper.map(tracker);
    }

    private void checkWhetherOtherTrackerWithGivenImeiExists(final TrackerForm trackerForm, final Model model)
            throws TrackerImeiAlreadyExistsException {
        this.checkWhetherOtherTrackerWithGivenPropertyExists(
                trackerForm,
                TrackerForm::getImei,
                TrackerService::findByImei,
                model,
                UserActionService::addErrorMessageOfImeiAlreadyExists,
                TrackerImeiAlreadyExistsException::new
        );
    }

    private static void addErrorMessageOfImeiAlreadyExists(final Model model) {
        model.addAttribute(ATTRIBUTE_NAME_IMEI_ALREADY_EXISTS_ERROR, ATTRIBUTE_VALUE_IMEI_ALREADY_EXISTS);
    }

    private void checkWhetherOtherTrackerWithGivenPhoneNumberExists(final TrackerForm trackerForm, final Model model)
            throws TrackerPhoneNumberAlreadyExistsException {
        this.checkWhetherOtherTrackerWithGivenPropertyExists(
                trackerForm,
                TrackerForm::getPhoneNumber,
                TrackerService::findByPhoneNumber,
                model,
                UserActionService::addErrorMessageOfPhoneNumberAlreadyExists,
                TrackerPhoneNumberAlreadyExistsException::new
        );
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

    private <P, E extends TrackerUniqueConstraintException> void checkWhetherOtherTrackerWithGivenPropertyExists(
            final TrackerForm trackerForm,
            final Function<TrackerForm, P> getter,
            final BiFunction<TrackerService, P, Optional<Tracker>> searchingFunction,
            final Model model,
            final Consumer<Model> errorMessageAdder,
            final Supplier<E> exceptionSupplier) throws E {
        final Optional<Tracker> optionalOtherTrackerWithGivenProperty = this.findOtherTrackerWithGivenProperty(
                trackerForm, getter, searchingFunction
        );
        if (optionalOtherTrackerWithGivenProperty.isPresent()) {
            errorMessageAdder.accept(model);
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

}
