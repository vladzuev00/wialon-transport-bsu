package by.vladzuev.locationreceiver.controller.user;

import by.vladzuev.locationreceiver.controller.user.view.UserView;
import by.vladzuev.locationreceiver.crud.dto.User;
import by.vladzuev.locationreceiver.service.security.service.SecurityService;
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

    @GetMapping("/authorized")
    public ResponseEntity<UserView> getAuthorizedUser() {
        final User user = securityService.findLoggedOnUser();
        final UserView view = new UserView(user);
        return ok(view);
    }
}
