package by.vladzuev.locationreceiver.service.registration;

import by.vladzuev.locationreceiver.crud.dto.User;
import by.vladzuev.locationreceiver.crud.enumeration.UserRole;
import by.vladzuev.locationreceiver.crud.service.UserService;
import by.vladzuev.locationreceiver.service.registration.exception.EmailAlreadyExistsException;
import by.vladzuev.locationreceiver.service.registration.exception.PasswordConfirmException;
import by.vladzuev.locationreceiver.service.registration.model.RegisteredUserRequest;
import by.vladzuev.locationreceiver.service.registration.model.RegisteredUserResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public final class RegistrationService {
    private final UserService userService;

    public RegisteredUserResponse checkIn(final RegisteredUserRequest request) {
        checkPasswordConfirming(request);
        checkEmailAlreadyExisting(request);
        final User user = createUser(request);
        final User savedUser = userService.save(user);
        return createResponse(savedUser);
    }

    private static void checkPasswordConfirming(final RegisteredUserRequest request) {
        final String password = request.getPassword();
        final String confirmedPassword = request.getConfirmedPassword();
        if (!Objects.equals(password, confirmedPassword)) {
            throw new PasswordConfirmException("Password and confirmed password aren't equal");
        }
    }

    private void checkEmailAlreadyExisting(final RegisteredUserRequest request) {
        final String email = request.getEmail();
        if (userService.isExistByEmail(email)) {
            throw new EmailAlreadyExistsException("Email '%s' already exists".formatted(email));
        }
    }

    private static User createUser(final RegisteredUserRequest request) {
        return User.builder()
                .email(request.getEmail())
                .password(request.getPassword())
                .role(UserRole.USER)
                .build();
    }

    private static RegisteredUserResponse createResponse(final User user) {
        return new RegisteredUserResponse(user.getId(), user.getEmail(), user.getRole());
    }
}
