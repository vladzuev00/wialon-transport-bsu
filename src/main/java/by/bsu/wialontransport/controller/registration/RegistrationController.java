package by.bsu.wialontransport.controller.registration;

import by.bsu.wialontransport.model.RegistrationStatus;
import by.bsu.wialontransport.model.form.UserForm;
import by.bsu.wialontransport.service.registration.RegistrationService;
import lombok.RequiredArgsConstructor;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

//TODO: do as rest controller
@RestController
@RequestMapping("/registration")
@RequiredArgsConstructor
public class RegistrationController {
    private final RegistrationService registrationService;

    @PostMapping
    public String checkIn(@Valid final UserForm userForm,
                          final BindingResult bindingResult,
                          final Model model) {
        final RegistrationStatus status = registrationService.checkIn(userForm, bindingResult, model);
        return status.getViewName();
    }
}
