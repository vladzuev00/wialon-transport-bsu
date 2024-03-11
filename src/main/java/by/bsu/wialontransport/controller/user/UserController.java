package by.bsu.wialontransport.controller.user;

import by.bsu.wialontransport.controller.user.view.UserView;
import by.bsu.wialontransport.service.security.service.SecurityService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {
    private final SecurityService securityService;

    @GetMapping("/authorizedUser")
    public ResponseEntity<UserView> getAuthorizedUser() {
        return securityService.findLoggedOnUser()
    }
}
