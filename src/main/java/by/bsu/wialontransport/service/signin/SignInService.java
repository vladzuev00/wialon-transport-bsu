package by.bsu.wialontransport.service.signin;

import by.bsu.wialontransport.crud.dto.User;
import by.bsu.wialontransport.crud.service.UserService;
import by.bsu.wialontransport.model.SignInStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;

import java.util.Optional;

import static by.bsu.wialontransport.model.SignInStatus.*;

@Service
@RequiredArgsConstructor
public final class SignInService {
    private static final String NAME_OF_MODEL_ATTRIBUTE_OF_SIGNED_IN_USER = "user";

    private final UserService userService;

    public SignInStatus signIn(final String email, final String password, final Model model) {
        final Optional<User> optionalUser = this.userService.findByEmail(email);
        final SignInStatus status = optionalUser
                .map(user -> checkPassword(user, password))
                .orElse(WRONG_EMAIL);
        if (status == SUCCESS) {
            model.addAttribute(NAME_OF_MODEL_ATTRIBUTE_OF_SIGNED_IN_USER, optionalUser.get());
        }
        return status;
    }

    private static SignInStatus checkPassword(final User user, final String password) {
        final String userPassword = user.getPassword();
        return password.equals(userPassword) ? SUCCESS : WRONG_PASSWORD;
    }

}
