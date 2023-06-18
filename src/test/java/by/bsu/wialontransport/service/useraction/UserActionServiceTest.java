package by.bsu.wialontransport.service.useraction;

import by.bsu.wialontransport.controller.exception.NoSuchEntityException;
import by.bsu.wialontransport.crud.dto.Tracker;
import by.bsu.wialontransport.crud.dto.User;
import by.bsu.wialontransport.crud.service.TrackerService;
import by.bsu.wialontransport.model.form.ChangePasswordForm;
import by.bsu.wialontransport.model.form.TrackerForm;
import by.bsu.wialontransport.model.form.mapper.TrackerFormMapper;
import by.bsu.wialontransport.model.sortingkey.TrackerSortingKey;
import by.bsu.wialontransport.security.service.SecurityService;
import by.bsu.wialontransport.service.useraction.changeinfo.ChangingUserInfoService;
import by.bsu.wialontransport.service.useraction.changeinfo.exception.NewPasswordConfirmingException;
import by.bsu.wialontransport.service.useraction.changeinfo.exception.PasswordChangingException;
import by.bsu.wialontransport.service.useraction.exception.TrackerImeiAlreadyExistsException;
import by.bsu.wialontransport.service.useraction.exception.TrackerPhoneNumberAlreadyExistsException;
import by.bsu.wialontransport.service.useraction.exception.TrackerUniqueConstraintException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.ui.Model;

import java.util.List;
import java.util.Optional;

import static by.bsu.wialontransport.model.sortingkey.TrackerSortingKey.IMEI;
import static java.util.Optional.empty;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.same;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public final class UserActionServiceTest {
    private static final String ATTRIBUTE_NAME_IMEI_ALREADY_EXISTS_ERROR = "imeiAlreadyExistsError";
    private static final String ATTRIBUTE_VALUE_IMEI_ALREADY_EXISTS_ERROR = "Imei already exists";

    private static final String ATTRIBUTE_NAME_PHONE_NUMBER_ALREADY_EXISTS_ERROR = "phoneNumberAlreadyExistsError";
    private static final String ATTRIBUTE_VALUE_PHONE_NUMBER_ALREADY_EXISTS_ERROR = "Phone number already exists";

    private static final String ATTRIBUTE_NAME_PASSWORD_CHANGING_ERROR = "newPasswordConfirmingError";
    private static final String ATTRIBUTE_VALUE_PASSWORD_CHANGING_ERROR = "Confirmed new password doesn't match";

    @Mock
    private SecurityService mockedSecurityService;

    @Mock
    private TrackerService mockedTrackerService;

    @Mock
    private TrackerFormMapper mockedMapper;

    @Mock
    private ChangingUserInfoService mockedChangingUserInfoService;

    private UserActionService userActionService;

    @Before
    public void initializeUserActionService() {
        this.userActionService = new UserActionService(
                this.mockedSecurityService,
                this.mockedTrackerService,
                this.mockedMapper,
                this.mockedChangingUserInfoService
        );
    }

    @Test
    public void attributeOfTrackersShouldBeAddedToShowProfilePage() {
        final int givenPageNumber = 0;
        final int givenPageSize = 5;
        final TrackerSortingKey givenSortingKey = IMEI;
        final Model givenModel = mock(Model.class);
        final String givenAttributeName = "listed_trackers";

        final User givenLoggedOnUser = createUser(255L);
        when(this.mockedSecurityService.findLoggedOnUser()).thenReturn(givenLoggedOnUser);

        final List<Tracker> givenListedTrackers = List.of(createTracker(256L), createTracker(257L));
        when(this.mockedTrackerService.findByUser(
                same(givenLoggedOnUser),
                eq(givenPageNumber),
                eq(givenPageSize),
                same(givenSortingKey.getComparator())
        )).thenReturn(givenListedTrackers);

        this.userActionService.addAttributeOfTrackersToShowProfilePage(
                givenPageNumber, givenPageSize, givenSortingKey, givenModel, givenAttributeName
        );

        verify(givenModel, times(1)).addAttribute(
                same(givenAttributeName), same(givenListedTrackers)
        );
    }

    @Test
    public void attributeOfTrackerFormShouldBeAddedToAddTracker() {
        final Model givenModel = mock(Model.class);
        final String givenAttributeName = "tracker_form";

        this.userActionService.addAttributeOfTrackerFormToAddTracker(givenModel, givenAttributeName);

        final TrackerForm expectedTrackerForm = new TrackerForm();
        verify(givenModel, times(1)).addAttribute(
                same(givenAttributeName), eq(expectedTrackerForm)
        );
    }

    @Test
    public void trackerShouldBeAdded()
            throws TrackerUniqueConstraintException {
        final TrackerForm givenForm = createTrackerForm("11112222333344445555", "447336934");
        final Model givenModel = mock(Model.class);

        when(this.mockedTrackerService.findByImei(eq("11112222333344445555"))).thenReturn(empty());
        when(this.mockedTrackerService.findByPhoneNumber(eq("447336934"))).thenReturn(empty());

        final User givenLoggedOnUser = createUser(255L);
        when(this.mockedSecurityService.findLoggedOnUser()).thenReturn(givenLoggedOnUser);

        final Tracker givenTracker = createTracker(256L);
        when(this.mockedMapper.map(same(givenForm), same(givenLoggedOnUser))).thenReturn(givenTracker);

        this.userActionService.addTracker(givenForm, givenModel);

        verify(this.mockedTrackerService, times(1)).save(same(givenTracker));
    }

    @Test
    public void trackerShouldNotBeAddedBecauseOfImeiUniqueConstraint()
            throws TrackerUniqueConstraintException {
        final TrackerForm givenForm = createTrackerForm("11112222333344445555", "447336934");
        final Model givenModel = mock(Model.class);

        final Tracker givenTrackerWithSameImei = createTracker(256L);
        when(this.mockedTrackerService.findByImei(eq("11112222333344445555")))
                .thenReturn(Optional.of(givenTrackerWithSameImei));

        boolean testSuccess;
        try {
            this.userActionService.addTracker(givenForm, givenModel);
            testSuccess = false;
        } catch (final TrackerImeiAlreadyExistsException exception) {
            testSuccess = true;
        }
        assertTrue(testSuccess);

        verify(givenModel, times(1)).addAttribute(
                eq(ATTRIBUTE_NAME_IMEI_ALREADY_EXISTS_ERROR), eq(ATTRIBUTE_VALUE_IMEI_ALREADY_EXISTS_ERROR)
        );
    }

    @Test
    public void trackerShouldNotBeAddedBecauseOfPhoneNumberUniqueConstraint()
            throws TrackerUniqueConstraintException {
        final TrackerForm givenForm = createTrackerForm("11112222333344445555", "447336934");
        final Model givenModel = mock(Model.class);

        when(this.mockedTrackerService.findByImei(eq("11112222333344445555"))).thenReturn(empty());

        final Tracker givenTrackerWithSamePhoneNumber = createTracker(256L);
        when(this.mockedTrackerService.findByPhoneNumber(eq("447336934"))).thenReturn(
                Optional.of(givenTrackerWithSamePhoneNumber)
        );

        boolean testSuccess;
        try {
            this.userActionService.addTracker(givenForm, givenModel);
            testSuccess = false;
        } catch (final TrackerPhoneNumberAlreadyExistsException exception) {
            testSuccess = true;
        }
        assertTrue(testSuccess);

        verify(givenModel, times(1)).addAttribute(
                eq(ATTRIBUTE_NAME_PHONE_NUMBER_ALREADY_EXISTS_ERROR),
                eq(ATTRIBUTE_VALUE_PHONE_NUMBER_ALREADY_EXISTS_ERROR)
        );
    }

    @Test
    public void attributeOfTrackerFormToUpdateTrackerShouldBeAdded() {
        final Long givenTrackerId = 255L;
        final Model givenModel = mock(Model.class);
        final String givenAttributeName = "attribute";

        final Tracker givenTracker = createTracker(givenTrackerId);
        when(this.mockedTrackerService.findById(givenTrackerId)).thenReturn(Optional.of(givenTracker));

        final TrackerForm givenTrackerForm = createTrackerForm("11112222333344445555", "447336934");
        when(this.mockedMapper.map(givenTracker)).thenReturn(givenTrackerForm);

        this.userActionService.addAttributeOfTrackerFormToUpdateTracker(givenTrackerId, givenModel, givenAttributeName);

        verify(givenModel, times(1)).addAttribute(
                same(givenAttributeName), same(givenTrackerForm)
        );
    }

    @Test(expected = NoSuchEntityException.class)
    public void attributeOfTrackerFormToUpdateTrackerShouldNotBeAddedBecauseOfNoTrackerWithGivenId() {
        final Long givenTrackerId = 255L;
        final Model givenModel = mock(Model.class);
        final String givenAttributeName = "attribute";

        when(this.mockedTrackerService.findById(givenTrackerId)).thenReturn(empty());

        this.userActionService.addAttributeOfTrackerFormToUpdateTracker(givenTrackerId, givenModel, givenAttributeName);
    }

    @Test
    public void trackerShouldBeUpdated()
            throws TrackerUniqueConstraintException {
        final TrackerForm givenTrackerForm = createTrackerForm(
                255L, "11112222333344445555", "447336934"
        );
        final Model givenModel = mock(Model.class);

        final Tracker givenExistingTracker = createTracker(255L);
        when(this.mockedTrackerService.findByImei(eq("11112222333344445555"))).thenReturn(
                Optional.of(givenExistingTracker)
        );
        when(this.mockedTrackerService.findByPhoneNumber(eq("447336934"))).thenReturn(
                Optional.of(givenExistingTracker)
        );

        final User givenLoggedOnUser = createUser(256L);
        when(this.mockedSecurityService.findLoggedOnUser()).thenReturn(givenLoggedOnUser);

        final Tracker givenTracker = createTracker(255L);
        when(this.mockedMapper.map(same(givenTrackerForm), same(givenLoggedOnUser))).thenReturn(givenTracker);

        this.userActionService.updateTracker(givenTrackerForm, givenModel);

        verify(this.mockedTrackerService, times(1)).update(same(givenTracker));
    }

    @Test
    public void trackerShouldNotBeUpdatedBecauseOfImeiUniqueConstraint()
            throws TrackerUniqueConstraintException {
        final TrackerForm givenTrackerForm = createTrackerForm(
                255L, "11112222333344445555", "447336934"
        );
        final Model givenModel = mock(Model.class);

        final Tracker givenExistingTracker = createTracker(256L);
        when(this.mockedTrackerService.findByImei(eq("11112222333344445555"))).thenReturn(
                Optional.of(givenExistingTracker)
        );

        boolean testSuccess;
        try {
            this.userActionService.updateTracker(givenTrackerForm, givenModel);
            testSuccess = false;
        } catch (final TrackerImeiAlreadyExistsException exception) {
            testSuccess = true;
        }
        assertTrue(testSuccess);

        verify(givenModel, times(1)).addAttribute(
                eq(ATTRIBUTE_NAME_IMEI_ALREADY_EXISTS_ERROR), eq(ATTRIBUTE_VALUE_IMEI_ALREADY_EXISTS_ERROR)
        );
    }

    @Test
    public void trackerShouldNotBeFoundBecauseOfPhoneNumberUniqueConstraint()
            throws TrackerUniqueConstraintException {
        final TrackerForm givenTrackerForm = createTrackerForm(
                255L, "11112222333344445555", "447336934"
        );
        final Model givenModel = mock(Model.class);

        final Tracker givenExistingTrackerWithSameImei = createTracker(255L);
        when(this.mockedTrackerService.findByImei(eq("11112222333344445555"))).thenReturn(
                Optional.of(givenExistingTrackerWithSameImei)
        );

        final Tracker givenExistingTrackerWithSamePhoneNumber = createTracker(256L);
        when(this.mockedTrackerService.findByPhoneNumber(eq("447336934"))).thenReturn(
                Optional.of(givenExistingTrackerWithSamePhoneNumber)
        );

        boolean testSuccess;
        try {
            this.userActionService.updateTracker(givenTrackerForm, givenModel);
            testSuccess = false;
        } catch (final TrackerPhoneNumberAlreadyExistsException exception) {
            testSuccess = true;
        }
        assertTrue(testSuccess);

        verify(givenModel, times(1)).addAttribute(
                eq(ATTRIBUTE_NAME_PHONE_NUMBER_ALREADY_EXISTS_ERROR),
                eq(ATTRIBUTE_VALUE_PHONE_NUMBER_ALREADY_EXISTS_ERROR)
        );
    }

    @Test
    public void trackerShouldBeDeletedById() {
        final Long givenTrackerId = 255L;

        this.userActionService.deleteTracker(givenTrackerId);

        verify(this.mockedTrackerService, times(1)).delete(same(givenTrackerId));
    }

    @Test
    public void attributeOfChangePasswordFormToChangePasswordShouldBeAdded() {
        final Model givenModel = mock(Model.class);
        final String givenAttributeName = "attribute";

        this.userActionService.addAttributeOfChangePasswordFormToChangePassword(givenModel, givenAttributeName);

        final ChangePasswordForm expectedChangePasswordForm = new ChangePasswordForm();
        verify(givenModel, times(1)).addAttribute(
                same(givenAttributeName), eq(expectedChangePasswordForm)
        );
    }

    @Test
    public void userPasswordShouldBeUpdated()
            throws PasswordChangingException {
        final ChangePasswordForm givenForm = ChangePasswordForm.builder()
                .oldPassword("old-password")
                .newPassword("new-password")
                .confirmedNewPassword("new-password")
                .build();
        final Model givenModel = mock(Model.class);

        final User givenLoggedOnUser = createUser(255L);
        when(this.mockedSecurityService.findLoggedOnUser()).thenReturn(givenLoggedOnUser);

        this.userActionService.updatePassword(givenForm, givenModel);

        verify(this.mockedChangingUserInfoService, times(1)).changePassword(
                same(givenLoggedOnUser), same(givenForm)
        );
    }

    @Test(expected = NewPasswordConfirmingException.class)
    public void userPasswordShouldNotBeUpdatedBecauseOfPasswordChangingException()
            throws PasswordChangingException {
        final ChangePasswordForm givenForm = ChangePasswordForm.builder()
                .oldPassword("old-password")
                .newPassword("new-password")
                .confirmedNewPassword("new-password")
                .build();
        final Model givenModel = mock(Model.class);

        final User givenLoggedOnUser = createUser(255L);
        when(this.mockedSecurityService.findLoggedOnUser()).thenReturn(givenLoggedOnUser);

        doThrow(NewPasswordConfirmingException.class).when(this.mockedChangingUserInfoService).changePassword(
                same(givenLoggedOnUser), same(givenForm)
        );

        this.userActionService.updatePassword(givenForm, givenModel);

        verify(givenModel, times(1)).addAttribute(
                ATTRIBUTE_NAME_PASSWORD_CHANGING_ERROR, ATTRIBUTE_VALUE_PASSWORD_CHANGING_ERROR
        );
    }

    private static User createUser(final Long id) {
        return User.builder()
                .id(id)
                .build();
    }

    private static Tracker createTracker(final Long id) {
        return Tracker.builder()
                .id(id)
                .build();
    }

    private static TrackerForm createTrackerForm(final String imei, final String phoneNumber) {
        return TrackerForm.builder()
                .imei(imei)
                .phoneNumber(phoneNumber)
                .build();
    }

    private static TrackerForm createTrackerForm(final Long id, final String imei, final String phoneNumber) {
        return TrackerForm.builder()
                .id(id)
                .imei(imei)
                .phoneNumber(phoneNumber)
                .build();
    }

}
