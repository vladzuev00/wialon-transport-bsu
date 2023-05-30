package by.bsu.wialontransport.service.registration;

import by.bsu.wialontransport.model.form.UserForm;
import by.bsu.wialontransport.crud.dto.User;
import by.bsu.wialontransport.crud.service.UserService;
import by.bsu.wialontransport.model.RegistrationStatus;
import by.bsu.wialontransport.service.registration.mapper.UserFormToUserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;

import static by.bsu.wialontransport.model.RegistrationStatus.*;

@Service
@RequiredArgsConstructor
public final class RegistrationService {
    private final UserService userService;
    private final UserFormToUserMapper mapper;

    public RegistrationStatus checkIn(final UserForm userForm, final BindingResult bindingResult, final Model model) {
        if (bindingResult.hasErrors()) {
            return onBindingError();
        } else if (!isPasswordConfirmedCorrectly(userForm)) {
            return onConfirmingPasswordError(model);
        } else if (this.isEmailAlreadyExist(userForm)) {
            return onEmailAlreadyExists(model);
        } else {
            return onSuccess(userForm);
        }
    }

    private static RegistrationStatus onBindingError() {
        return BINDING_ERROR;
    }

    private static RegistrationStatus onConfirmingPasswordError(final Model model) {
        addErrorMessageOfConfirmingPassword(model);
        return CONFIRMING_PASSWORD_ERROR;
    }

    private static RegistrationStatus onEmailAlreadyExists(final Model model) {
        addErrorMessageOfEmailAlreadyExists(model);
        return EMAIL_ALREADY_EXISTS;
    }

    private RegistrationStatus onSuccess(final UserForm userForm) {
        final User user = this.mapper.map(userForm);
        this.userService.saveWithEncryptingPassword(user);
        return SUCCESS;
    }

    private static boolean isPasswordConfirmedCorrectly(final UserForm userForm) {
        final String password = userForm.getPassword();
        final String confirmedPassword = userForm.getConfirmedPassword();
        return password.equals(confirmedPassword);
    }

    private static void addErrorMessageOfConfirmingPassword(final Model model) {
        model.addAttribute("confirmingPasswordError", "Password isn't confirmed correctly");
    }

    private static void addErrorMessageOfEmailAlreadyExists(final Model model) {
        model.addAttribute("emailAlreadyExistsError", "Email already exists");
    }

    private boolean isEmailAlreadyExist(final UserForm userForm) {
        final String inputtedEmail = userForm.getEmail();
        return this.userService.isExistByEmail(inputtedEmail);
    }

}
