package by.bsu.wialontransport.service.useraction.changepassword;

import by.bsu.wialontransport.crud.dto.User;
import by.bsu.wialontransport.crud.service.UserService;
import by.bsu.wialontransport.model.form.ChangePasswordForm;
import by.bsu.wialontransport.service.useraction.changepassword.exception.NewPasswordConfirmingException;
import by.bsu.wialontransport.service.useraction.changepassword.exception.OldPasswordConfirmingException;
import by.bsu.wialontransport.service.useraction.changepassword.exception.PasswordChangingException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Objects;

//TODO: don't work correctly
@Service
@RequiredArgsConstructor
public final class ChangingPasswordService {
    private final UserService userService;
    private final BCryptPasswordEncoder passwordEncoder;

    public void change(final User user, final ChangePasswordForm form)
            throws PasswordChangingException {
        this.checkConfirmingOldPassword(user, form);
        checkConfirmingNewPassword(form);
        this.userService.updatePasswordWithEncrypting(user, form.getNewPassword());
    }

    private void checkConfirmingOldPassword(final User user, final ChangePasswordForm form)
            throws OldPasswordConfirmingException {
        if (!this.isOldPasswordConfirmed(user, form)) {
            throw new OldPasswordConfirmingException();
        }
    }

    private boolean isOldPasswordConfirmed(final User user, final ChangePasswordForm form) {
        final String correctOldEncryptedPassword = user.getPassword();
        final String inputtedOldPassword = form.getOldPassword();
        return this.passwordEncoder.matches(inputtedOldPassword, correctOldEncryptedPassword);
    }

    private static void checkConfirmingNewPassword(final ChangePasswordForm form)
            throws NewPasswordConfirmingException {
        if (!isNewPasswordConfirmed(form)) {
            throw new NewPasswordConfirmingException();
        }
    }

    private static boolean isNewPasswordConfirmed(final ChangePasswordForm form) {
        final String newPassword = form.getNewPassword();
        final String confirmedNewPassword = form.getConfirmedNewPassword();
        return Objects.equals(newPassword, confirmedNewPassword);
    }

}
