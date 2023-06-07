package by.bsu.wialontransport.service.registration;

import by.bsu.wialontransport.crud.service.UserService;
import by.bsu.wialontransport.model.RegistrationStatus;
import by.bsu.wialontransport.model.form.UserForm;
import by.bsu.wialontransport.model.form.mapper.UserFormMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import static by.bsu.wialontransport.model.RegistrationStatus.*;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public final class RegistrationServiceTest {

    @Mock
    private UserService mockedUserService;

    @Mock
    private UserFormMapper mockedMapper;

    private RegistrationService registrationService;

    @Before
    public void initializeRegistrationService() {
        this.registrationService = new RegistrationService(this.mockedUserService, this.mockedMapper);
    }

    @Test
    public void registrationStatusShouldBeSuccess() {
        final UserForm givenForm = UserForm.builder()
                .email("vladzuev.00@mail.ru")
                .password("password")
                .confirmedPassword("password")
                .build();
        final BindingResult givenBindingResult = mock(BindingResult.class);
        final Model givenModel = mock(Model.class);

        when(givenBindingResult.hasErrors()).thenReturn(false);
        when(this.mockedUserService.isExistByEmail("vladzuev.00@mail.ru")).thenReturn(false);

        final RegistrationStatus actual = this.registrationService.checkIn(givenForm, givenBindingResult, givenModel);
        assertSame(SUCCESS, actual);
    }

    @Test
    public void registrationStatusShouldBeBindingError() {
        final UserForm givenForm = UserForm.builder()
                .email("vladzuev.00@mail.ru")
                .password("password")
                .confirmedPassword("password")
                .build();
        final BindingResult givenBindingResult = mock(BindingResult.class);
        final Model givenModel = mock(Model.class);

        when(givenBindingResult.hasErrors()).thenReturn(true);

        final RegistrationStatus actual = this.registrationService.checkIn(givenForm, givenBindingResult, givenModel);
        assertSame(BINDING_ERROR, actual);
    }

    @Test
    public void registrationStatusShouldBeConfirmingPasswordError() {
        final UserForm givenForm = UserForm.builder()
                .email("vladzuev.00@mail.ru")
                .password("password")
                .confirmedPassword("password1")
                .build();
        final BindingResult givenBindingResult = mock(BindingResult.class);
        final Model givenModel = mock(Model.class);

        when(givenBindingResult.hasErrors()).thenReturn(false);

        final RegistrationStatus actual = this.registrationService.checkIn(givenForm, givenBindingResult, givenModel);
        assertSame(CONFIRMING_PASSWORD_ERROR, actual);
    }

    @Test
    public void registrationStatusShouldBeEmailAlreadyExists() {
        final UserForm givenForm = UserForm.builder()
                .email("vladzuev.00@mail.ru")
                .password("password")
                .confirmedPassword("password")
                .build();
        final BindingResult givenBindingResult = mock(BindingResult.class);
        final Model givenModel = mock(Model.class);

        when(givenBindingResult.hasErrors()).thenReturn(false);
        when(this.mockedUserService.isExistByEmail("vladzuev.00@mail.ru")).thenReturn(true);

        final RegistrationStatus actual = this.registrationService.checkIn(givenForm, givenBindingResult, givenModel);
        assertSame(EMAIL_ALREADY_EXISTS, actual);
    }

}
