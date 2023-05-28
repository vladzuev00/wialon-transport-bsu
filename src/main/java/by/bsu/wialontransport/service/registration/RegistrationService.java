package by.bsu.wialontransport.service.registration;

import by.bsu.wialontransport.controller.registration.model.UserForm;
import by.bsu.wialontransport.crud.service.UserService;
import by.bsu.wialontransport.model.RegistrationStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import static by.bsu.wialontransport.model.RegistrationStatus.*;

@Service
@RequiredArgsConstructor
public final class RegistrationService {
    private final UserService userService;

    public RegistrationStatus checkIn(final UserForm userForm, final BindingResult bindingResult, final Model model) {
        if (bindingResult.hasErrors()) {
            return BINDING_ERROR;
        } else if (!isPasswordConfirmedCorrectly(userForm)) {
            addErrorMessageOfConfirmingPassword(model);
            return CONFIRMING_PASSWORD_ERROR;
        } else if (this.isEmailAlreadyExist(userForm)) {
            return EMAIL_ALREADY_EXIST;
        } else {
            return SUCCESS;
        }
    }

    private static boolean isPasswordConfirmedCorrectly(final UserForm userForm) {
        final String password = userForm.getPassword();
        final String confirmedPassword = userForm.getConfirmedPassword();
        return password.equals(confirmedPassword);
    }

    private static void addErrorMessageOfConfirmingPassword(final Model model) {
        model.addAttribute("confirmingPasswordError", "Password isn't confirmed correctly");
    }

    private boolean isEmailAlreadyExist(final UserForm userForm) {
        final String inputtedEmail = userForm.getEmail();
        return this.userService.isExistByEmail(inputtedEmail);
    }

}
