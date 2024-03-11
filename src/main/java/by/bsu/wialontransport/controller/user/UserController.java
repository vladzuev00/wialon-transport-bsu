package by.bsu.wialontransport.controller.user;

import by.bsu.wialontransport.controller.user.view.UserView;
import by.bsu.wialontransport.crud.dto.User;
import by.bsu.wialontransport.service.security.service.SecurityService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {
    private final SecurityService securityService;

    @GetMapping("/authorizedUser")
    public ResponseEntity<UserView> getAuthorizedUser() {
        final User user = securityService.findLoggedOnUser();
        final UserView view = new UserView(user);
        return ok(view);
    }
}
