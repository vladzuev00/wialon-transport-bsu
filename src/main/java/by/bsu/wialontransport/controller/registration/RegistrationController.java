package by.bsu.wialontransport.controller.registration;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.SessionAttributes;

@Controller
@RequestMapping("/registration")
@SessionAttributes(value = {"logged_on_user"})
@RequiredArgsConstructor
public class RegistrationController {



}
