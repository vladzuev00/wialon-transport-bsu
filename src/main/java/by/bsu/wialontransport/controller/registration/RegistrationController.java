package by.bsu.wialontransport.controller.registration;

import by.bsu.wialontransport.controller.registration.model.UserForm;
import by.bsu.wialontransport.model.RegistrationStatus;
import by.bsu.wialontransport.service.registration.RegistrationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@Controller
@RequestMapping("/registration")
@RequiredArgsConstructor
public class RegistrationController {
    private final RegistrationService registrationService;

    @GetMapping
    public String checkIn(final Model model) {
        model.addAttribute("userForm", new UserForm());
        return "registration";
    }

    @PostMapping
    public String checkIn(@ModelAttribute("userForm") @Valid final UserForm userForm,
                          final BindingResult bindingResult,
                          final Model model) {
        final RegistrationStatus status = this.registrationService.checkIn(userForm, bindingResult, model);
        return status.getViewName();
    }

}
