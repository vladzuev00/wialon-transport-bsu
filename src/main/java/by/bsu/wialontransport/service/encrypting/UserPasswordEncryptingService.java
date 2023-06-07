package by.bsu.wialontransport.service.encrypting;

import by.bsu.wialontransport.crud.dto.User;
import by.bsu.wialontransport.crud.service.UserService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public final class UserPasswordEncryptingService extends AbstractPasswordEncryptingService<User, UserService> {

    public UserPasswordEncryptingService(final BCryptPasswordEncoder encoder, final UserService crudService) {
        super(encoder, crudService);
    }

    @Override
    protected User createWithEncryptedPassword(final User source, final String encryptedPassword) {
        return new User(
                source.getId(),
                source.getEmail(),
                encryptedPassword,
                source.getRole()
        );
    }
}
