package by.bsu.wialontransport.controller.registration;

import by.bsu.wialontransport.model.form.UserForm;
import by.bsu.wialontransport.model.RegistrationStatus;
import by.bsu.wialontransport.service.registration.RegistrationService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import static by.bsu.wialontransport.model.RegistrationStatus.SUCCESS;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public final class RegistrationControllerTest {
    private static final String ATTRIBUTE_NAME_USER_FORM = "userForm";
    private static final String VIEW_NAME_REGISTRATION_PAGE = "registration";

    @Mock
    private RegistrationService mockedRegistrationService;

    private RegistrationController controller;

    @Captor
    private ArgumentCaptor<UserForm> userFormArgumentCaptor;

    @Captor
    private ArgumentCaptor<String> stringArgumentCaptor;

    @Before
    public void initializeController() {
        this.controller = new RegistrationController(this.mockedRegistrationService);
    }

    @Test
    public void viewNameOfRegistrationShouldBeGot() {
        final Model givenModel = mock(Model.class);

        final String actual = this.controller.checkIn(givenModel);
        assertEquals(VIEW_NAME_REGISTRATION_PAGE, actual);

        verify(givenModel, times(1)).addAttribute(
                this.stringArgumentCaptor.capture(), this.userFormArgumentCaptor.capture()
        );
        assertEquals(ATTRIBUTE_NAME_USER_FORM, this.stringArgumentCaptor.getValue());
        assertEquals(new UserForm(), this.userFormArgumentCaptor.getValue());
    }

    @Test
    public void viewNameOfGivenRegistrationStatusShouldBeReturned() {
        final UserForm givenUserForm = UserForm.builder().build();
        final BindingResult givenBindingResult = mock(BindingResult.class);
        final Model givenModel = mock(Model.class);

        final RegistrationStatus givenStatus = SUCCESS;
        when(this.mockedRegistrationService.checkIn(same(givenUserForm), same(givenBindingResult), same(givenModel)))
                .thenReturn(givenStatus);

        final String actual = this.controller.checkIn(givenUserForm, givenBindingResult, givenModel);
        final String expected = givenStatus.getViewName();
        assertEquals(expected, actual);
    }

}