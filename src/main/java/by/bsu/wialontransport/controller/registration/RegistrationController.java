package by.bsu.wialontransport.controller.registration;

import by.bsu.wialontransport.model.form.UserForm;
import by.bsu.wialontransport.model.RegistrationStatus;
import by.bsu.wialontransport.service.registration.RegistrationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;

@Controller
@RequestMapping("/registration")
@RequiredArgsConstructor
public class RegistrationController {
    private static final String ATTRIBUTE_NAME_USER_FORM = "userForm";
    private static final String VIEW_NAME_REGISTRATION_PAGE = "registration";

    private final RegistrationService registrationService;

    @GetMapping
    public String checkIn(final Model model) {
        model.addAttribute(ATTRIBUTE_NAME_USER_FORM, new UserForm());
        return VIEW_NAME_REGISTRATION_PAGE;
    }

    @PostMapping
    public String checkIn(@Valid @ModelAttribute(ATTRIBUTE_NAME_USER_FORM) final UserForm userForm,
                          final BindingResult bindingResult,
                          final Model model) {
        final RegistrationStatus status = registrationService.checkIn(userForm, bindingResult, model);
        return status.getViewName();
    }

}
