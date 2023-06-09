package by.bsu.wialontransport.service.useraction.changepassword;

import by.bsu.wialontransport.crud.dto.User;
import by.bsu.wialontransport.crud.service.UserService;
import by.bsu.wialontransport.model.form.ChangePasswordForm;
import by.bsu.wialontransport.service.useraction.changepassword.exception.NewPasswordConfirmingException;
import by.bsu.wialontransport.service.useraction.changepassword.exception.OldPasswordConfirmingException;
import by.bsu.wialontransport.service.useraction.changepassword.exception.PasswordChangingException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public final class ChangingPasswordServiceTest {

    @Mock
    private UserService mockedUserService;

    @Mock
    private BCryptPasswordEncoder mockedPasswordEncoder;

    private ChangingPasswordService changingPasswordService;

    @Before
    public void initializeChangingPasswordService() {
        this.changingPasswordService = new ChangingPasswordService(this.mockedUserService, this.mockedPasswordEncoder);
    }

    @Test
    public void passwordShouldBeChanged()
            throws PasswordChangingException {
        final User givenUser = createUserByPassword("old-password");
        final ChangePasswordForm givenForm = createForm(
                "old-password", "new-password", "new-password"
        );

        when(this.mockedPasswordEncoder.matches(eq("old-password"), eq("old-password")))
                .thenReturn(true);

        this.changingPasswordService.change(givenUser, givenForm);

        verify(this.mockedUserService, times(1)).updatePassword(
                same(givenUser), eq("new-password")
        );
    }

    @Test(expected = OldPasswordConfirmingException.class)
    public void passwordShouldNotBeChangedBecauseOfFailedConfirmingOldPassword()
            throws PasswordChangingException {
        final User givenUser = createUserByPassword("old-password");
        final ChangePasswordForm givenForm = createForm(
                "old-password", "new-password", "new-password"
        );

        when(this.mockedPasswordEncoder.matches(eq("old-password"), eq("old-password")))
                .thenReturn(false);

        this.changingPasswordService.change(givenUser, givenForm);

        verify(this.mockedUserService, times(0)).updatePassword(
                same(givenUser), any()
        );
    }

    @Test(expected = NewPasswordConfirmingException.class)
    public void passwordShouldNotBeChangedBecauseOfFailedConfirmingNewPassword()
            throws PasswordChangingException {
        final User givenUser = createUserByPassword("old-password");
        final ChangePasswordForm givenForm = createForm(
                "old-password", "new-password", "ne-password"
        );

        when(this.mockedPasswordEncoder.matches(eq("old-password"), eq("old-password")))
                .thenReturn(true);

        this.changingPasswordService.change(givenUser, givenForm);

        verify(this.mockedUserService, times(0)).updatePassword(
                same(givenUser), any()
        );
    }

    private static User createUserByPassword(final String password) {
        return User.builder()
                .password(password)
                .build();
    }

    private static ChangePasswordForm createForm(final String oldPassword,
                                                 final String newPassword,
                                                 final String confirmedNewPassword) {
        return ChangePasswordForm.builder()
                .oldPassword(oldPassword)
                .newPassword(newPassword)
                .confirmedNewPassword(confirmedNewPassword)
                .build();
    }
}
