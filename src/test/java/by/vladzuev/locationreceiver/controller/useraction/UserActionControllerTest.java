package by.vladzuev.locationreceiver.controller.useraction;

import by.vladzuev.locationreceiver.model.form.ChangePasswordForm;
import by.vladzuev.locationreceiver.model.form.TrackerForm;
import by.vladzuev.locationreceiver.model.sortingkey.TrackerSortingKey;
import by.vladzuev.locationreceiver.service.useraction.UserActionService;
import by.vladzuev.locationreceiver.service.useraction.changeinfo.exception.password.NewPasswordConfirmingException;
import by.vladzuev.locationreceiver.service.useraction.changeinfo.exception.password.PasswordChangingException;
import by.vladzuev.locationreceiver.service.useraction.exception.TrackerImeiAlreadyExistsException;
import by.vladzuev.locationreceiver.service.useraction.exception.TrackerUniqueConstraintException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public final class UserActionControllerTest {

    @Mock
    private UserActionService mockedUserActionService;

    private UserActionController controller;

    @Before
    public void initializeController() {
        this.controller = new UserActionController(this.mockedUserActionService);
    }

    @Test
    public void profilePageShouldBeShowed() {
        final int givenPageNumber = 0;
        final int givenPageSize = 5;
        final TrackerSortingKey givenSortingKey = TrackerSortingKey.IMEI;
        final Model givenModel = mock(Model.class);

        final String actual = this.controller.showProfilePage(
                givenPageNumber, givenPageSize, givenSortingKey, givenModel
        );
        final String expected = "user_profile_page";
        assertEquals(expected, actual);

        verify(this.mockedUserActionService, times(1)).addAttributeOfTrackersToShowProfilePage(
                eq(givenPageNumber),
                eq(givenPageSize),
                same(givenSortingKey),
                same(givenModel),
                eq("listed_trackers")
        );
    }

    @Test
    public void addTrackedPageShouldBeShowed() {
        final Model givenModel = mock(Model.class);

        final String actual = this.controller.showAddTrackerPage(givenModel);
        final String expected = "add_tracker";
        assertEquals(expected, actual);

        verify(this.mockedUserActionService, times(1)).addTrackerFormAsAttribute(
                same(givenModel), eq("added_tracker_form")
        );
    }

    @Test
    public void trackerShouldBeAdded()
            throws TrackerUniqueConstraintException {
        final TrackerForm givenForm = new TrackerForm();
        final BindingResult givenBindingResult = mock(BindingResult.class);
        final Model givenModel = mock(Model.class);

        when(givenBindingResult.hasErrors()).thenReturn(false);

        final String actual = this.controller.addTracker(givenForm, givenBindingResult, givenModel);
        final String expected = "redirect:/user/profile";
        assertEquals(expected, actual);

        verify(this.mockedUserActionService, times(1)).addTracker(
                same(givenForm), same(givenModel)
        );
    }

    @Test
    public void trackerShouldNotBeAddedBecauseOfBindingResultHasErrors()
            throws TrackerUniqueConstraintException {
        final TrackerForm givenForm = new TrackerForm();
        final BindingResult givenBindingResult = mock(BindingResult.class);
        final Model givenModel = mock(Model.class);

        when(givenBindingResult.hasErrors()).thenReturn(true);

        final String actual = this.controller.addTracker(givenForm, givenBindingResult, givenModel);
        final String expected = "add_tracker";
        assertEquals(expected, actual);

        verify(this.mockedUserActionService, times(0)).addTracker(
                same(givenForm), same(givenModel)
        );
    }

    @Test
    public void trackerShouldNotBeAddedBecauseOfTrackerUniqueConstraintException()
            throws TrackerUniqueConstraintException {
        final TrackerForm givenForm = new TrackerForm();
        final BindingResult givenBindingResult = mock(BindingResult.class);
        final Model givenModel = mock(Model.class);

        when(givenBindingResult.hasErrors()).thenReturn(false);
        Mockito.doThrow(TrackerImeiAlreadyExistsException.class).when(this.mockedUserActionService).addTracker(
                any(TrackerForm.class), any(Model.class)
        );

        final String actual = this.controller.addTracker(givenForm, givenBindingResult, givenModel);
        final String expected = "add_tracker";
        assertEquals(expected, actual);

        verify(this.mockedUserActionService, times(1)).addTracker(
                same(givenForm), same(givenModel)
        );
    }

    @Test
    public void updateTrackerPageShouldBeShowed() {
        final Long givenTrackerId = 255L;
        final Model givenModel = mock(Model.class);

        final String actual = this.controller.showUpdateTrackerPage(givenTrackerId, givenModel);
        final String expected = "update_tracker";
        assertEquals(expected, actual);

        verify(this.mockedUserActionService, times(1)).addTrackerFormAsAttribute(
                same(givenTrackerId), same(givenModel), eq("updated_tracker_form")
        );
    }

    @Test
    public void trackerShouldBeUpdated()
            throws TrackerUniqueConstraintException {
        final TrackerForm givenForm = new TrackerForm();
        final BindingResult givenBindingResult = mock(BindingResult.class);
        final Model givenModel = mock(Model.class);

        when(givenBindingResult.hasErrors()).thenReturn(false);

        final String actual = this.controller.updateTracker(givenForm, givenBindingResult, givenModel);
        final String expected = "redirect:/user/profile";
        assertEquals(expected, actual);

        verify(this.mockedUserActionService, times(1)).updateTracker(
                same(givenForm), same(givenModel)
        );
    }

    @Test
    public void trackerShouldNotBeUpdatedBecauseOfBindingResultHasErrors()
            throws TrackerUniqueConstraintException {
        final TrackerForm givenForm = new TrackerForm();
        final BindingResult givenBindingResult = mock(BindingResult.class);
        final Model givenModel = mock(Model.class);

        when(givenBindingResult.hasErrors()).thenReturn(true);

        final String actual = this.controller.updateTracker(givenForm, givenBindingResult, givenModel);
        final String expected = "update_tracker";
        assertEquals(expected, actual);

        verify(this.mockedUserActionService, times(0)).updateTracker(
                same(givenForm), same(givenModel)
        );
    }

    @Test
    public void trackerShouldNotBeUpdatedBecauseOfTrackerUniqueConstraintException()
            throws TrackerUniqueConstraintException {
        final TrackerForm givenForm = new TrackerForm();
        final BindingResult givenBindingResult = mock(BindingResult.class);
        final Model givenModel = mock(Model.class);

        when(givenBindingResult.hasErrors()).thenReturn(false);

        doThrow(TrackerImeiAlreadyExistsException.class).when(this.mockedUserActionService).updateTracker(
                any(TrackerForm.class), any(Model.class)
        );

        final String actual = this.controller.updateTracker(givenForm, givenBindingResult, givenModel);
        final String expected = "update_tracker";
        assertEquals(expected, actual);

        verify(this.mockedUserActionService, times(1)).updateTracker(
                same(givenForm), same(givenModel)
        );
    }

    @Test
    public void trackerShouldBeDeleted() {
        final Long givenTrackerId = 255L;

        final String actual = this.controller.deleteTracker(givenTrackerId);
        final String expected = "redirect:/user/profile";
        assertEquals(expected, actual);

        verify(this.mockedUserActionService, times(1)).deleteTracker(givenTrackerId);
    }

    @Test
    public void changePasswordPageShouldBeShowed() {
        final Model givenModel = mock(Model.class);

        final String actual = this.controller.showChangePasswordPage(givenModel);
        final String expected = "change_password";
        assertEquals(expected, actual);

        verify(this.mockedUserActionService, times(1))
                .addChangePasswordFormAsAttribute(
                        same(givenModel),
                        eq("change_password_form")
                );
    }

    @Test
    public void passwordShouldBeChanged()
            throws PasswordChangingException {
        final ChangePasswordForm givenForm = new ChangePasswordForm();
        final BindingResult givenBindingResult = mock(BindingResult.class);
        final Model givenModel = mock(Model.class);

        when(givenBindingResult.hasErrors()).thenReturn(false);

        final String actual = this.controller.changePassword(givenForm, givenBindingResult, givenModel);
        final String expected = "redirect:/user/profile";
        assertEquals(expected, actual);

        verify(this.mockedUserActionService, times(1)).updatePassword(
                same(givenForm), same(givenModel)
        );
    }

    @Test
    public void passwordShouldNotBeChangedBecauseOfBindingResultHasErrors()
            throws PasswordChangingException {
        final ChangePasswordForm givenForm = new ChangePasswordForm();
        final BindingResult givenBindingResult = mock(BindingResult.class);
        final Model givenModel = mock(Model.class);

        when(givenBindingResult.hasErrors()).thenReturn(true);

        final String actual = this.controller.changePassword(givenForm, givenBindingResult, givenModel);
        final String expected = "change_password";
        assertEquals(expected, actual);

        verify(this.mockedUserActionService, times(0)).updatePassword(
                same(givenForm), same(givenModel)
        );
    }

    @Test
    public void passwordShouldNotBeChangedBecauseOfException()
            throws PasswordChangingException {
        final ChangePasswordForm givenForm = new ChangePasswordForm();
        final BindingResult givenBindingResult = mock(BindingResult.class);
        final Model givenModel = mock(Model.class);

        when(givenBindingResult.hasErrors()).thenReturn(false);
        doThrow(NewPasswordConfirmingException.class).when(this.mockedUserActionService).updatePassword(
                same(givenForm), same(givenModel)
        );

        final String actual = this.controller.changePassword(givenForm, givenBindingResult, givenModel);
        final String expected = "change_password";
        assertEquals(expected, actual);

        verify(this.mockedUserActionService, times(1)).updatePassword(
                same(givenForm), same(givenModel)
        );
    }
}
