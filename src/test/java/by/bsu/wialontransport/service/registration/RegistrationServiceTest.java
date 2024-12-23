package by.bsu.wialontransport.service.registration;

import by.bsu.wialontransport.crud.dto.User;
import by.bsu.wialontransport.crud.service.UserService;
import by.bsu.wialontransport.service.registration.exception.EmailAlreadyExistsException;
import by.bsu.wialontransport.service.registration.exception.PasswordConfirmException;
import by.bsu.wialontransport.service.registration.model.RegisteredUserRequest;
import by.bsu.wialontransport.service.registration.model.RegisteredUserResponse;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import static by.bsu.wialontransport.crud.entity.UserEntity.Role.USER;
import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.same;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public final class RegistrationServiceTest {

    @Mock
    private UserService mockedUserService;

    private RegistrationService registrationService;

    @Before
    public void initializeRegistrationService() {
        registrationService = new RegistrationService(mockedUserService);
    }

    @Test
    public void registrationShouldBeSuccess() {
        final String givenEmail = "vladzuev.00@mail.ru";
        final String givenPassword = "password";
        final RegisteredUserRequest givenRequest = RegisteredUserRequest.builder()
                .email(givenEmail)
                .password(givenPassword)
                .confirmedPassword(givenPassword)
                .build();

        when(mockedUserService.isExistByEmail(same(givenEmail))).thenReturn(false);

        final User expectedUser = User.builder()
                .email(givenEmail)
                .password(givenPassword)
                .role(USER)
                .build();
        final Long givenSavedUserId = 255L;
        final User givenSavedUser = User.builder()
                .id(givenSavedUserId)
                .email(givenEmail)
                .password(givenPassword)
                .role(USER)
                .build();
        when(mockedUserService.save(eq(expectedUser))).thenReturn(givenSavedUser);

        final RegisteredUserResponse actual = registrationService.checkIn(givenRequest);
        final RegisteredUserResponse expected = new RegisteredUserResponse(givenSavedUserId, givenEmail, USER);
        assertEquals(expected, actual);
    }

    @Test(expected = PasswordConfirmException.class)
    public void registrationShouldBeFailedBecauseOfWrongPasswordConfirming() {
        final RegisteredUserRequest givenRequest = RegisteredUserRequest.builder()
                .email("vladzuev.00@mail.ru")
                .password("password")
                .confirmedPassword("passwordd")
                .build();

        registrationService.checkIn(givenRequest);

        verifyNoInteractions(mockedUserService);
    }

    @Test(expected = EmailAlreadyExistsException.class)
    public void registrationShouldBeFailedBecauseOfEmailAlreadyExists() {
        final String givenEmail = "vladzuev.00@mail.ru";
        final String givenPassword = "password";
        final RegisteredUserRequest givenRequest = RegisteredUserRequest.builder()
                .email(givenEmail)
                .password(givenPassword)
                .confirmedPassword(givenPassword)
                .build();

        when(mockedUserService.isExistByEmail(same(givenEmail))).thenReturn(true);

        registrationService.checkIn(givenRequest);
    }
}
