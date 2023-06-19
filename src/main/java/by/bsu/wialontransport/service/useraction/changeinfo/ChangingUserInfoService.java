package by.bsu.wialontransport.service.useraction.changeinfo;

import by.bsu.wialontransport.crud.dto.User;
import by.bsu.wialontransport.crud.service.UserService;
import by.bsu.wialontransport.model.form.ChangePasswordForm;
import by.bsu.wialontransport.service.useraction.changeinfo.exception.EmailAlreadyExistsException;
import by.bsu.wialontransport.service.useraction.changeinfo.exception.password.NewPasswordConfirmingException;
import by.bsu.wialontransport.service.useraction.changeinfo.exception.password.OldPasswordConfirmingException;
import by.bsu.wialontransport.service.useraction.changeinfo.exception.password.PasswordChangingException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Objects;

import static java.lang.String.format;

@Service
@RequiredArgsConstructor
public final class ChangingUserInfoService {
    private static final String TEMPLATE_DESCRIPTION_EMAIL_ALREADY_EXISTS_EXCEPTION = "Email '%s' already exists";

    private final UserService userService;
    private final BCryptPasswordEncoder passwordEncoder;

    public void changePassword(final User user, final ChangePasswordForm form)
            throws PasswordChangingException {
        this.checkConfirmingOldPassword(user, form);
        checkConfirmingNewPassword(form);
        this.userService.updatePassword(user, form.getNewPassword());
    }

    //TODO: add transaction isolation level for 'check than action' and also for change tracker imei
    public void changeEmail(final User user, final String newEmail)
            throws EmailAlreadyExistsException {
        this.checkUniqueEmailConstraint(newEmail);
        this.userService.updateEmail(user, newEmail);
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

    private void checkUniqueEmailConstraint(final String newEmail)
            throws EmailAlreadyExistsException {
        final boolean emailAlreadyExists = this.userService.isExistByEmail(newEmail);
        if (emailAlreadyExists) {
            throw new EmailAlreadyExistsException(
                    format(TEMPLATE_DESCRIPTION_EMAIL_ALREADY_EXISTS_EXCEPTION, newEmail)
            );
        }
    }

}
