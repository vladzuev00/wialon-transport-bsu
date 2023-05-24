package by.bsu.wialontransport.controller.signin;

import by.bsu.wialontransport.model.SignInStatus;
import by.bsu.wialontransport.service.signin.SignInService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;

@Controller
@RequestMapping("/signIn")
@SessionAttributes(value = {"user"})
@RequiredArgsConstructor
public class SignInController {
    private static final String NAME_OF_PAGE_TO_SIGN_IN = "sign_in.jsp";

    private final SignInService signInService;

    @GetMapping("/page")
    public String signIn() {
        return NAME_OF_PAGE_TO_SIGN_IN;
    }

    @GetMapping
    public String signIn(@RequestParam(name = "email") final String email,
                         @RequestParam(name = "password") final String password,
                         final Model model) {
        final SignInStatus status = this.signInService.signIn(email, password, model);
        return status.getNameOfView();
    }

}
