package by.bsu.wialontransport.crud.service;

import by.bsu.wialontransport.crud.dto.User;
import by.bsu.wialontransport.crud.entity.UserEntity;
import by.bsu.wialontransport.crud.mapper.UserMapper;
import by.bsu.wialontransport.crud.repository.UserRepository;
import by.bsu.wialontransport.service.encrypting.crud.PasswordEncryptingService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class UserService
        extends AbstractCRUDService<Long, UserEntity, User, UserMapper, UserRepository>
        implements PasswordEncryptingService<User> {

    public UserService(final UserMapper mapper, final UserRepository repository) {
        super(mapper, repository);
    }

    @Transactional(readOnly = true)
    public Optional<User> findByEmail(final String email) {
        final Optional<UserEntity> optionalEntity = super.repository.findByEmail(email);
        return optionalEntity.map(super.mapper::mapToDto);
    }

    @Transactional(readOnly = true)
    public boolean isExistByEmail(final String email) {
        return super.repository.existsByEmail(email);
    }

    @Override
    public User updatePassword(final User source, final String encryptedPassword) {
        //TODO
        throw new UnsupportedOperationException();
    }

    //TODO: remove
    public User saveWithEncryptingPassword(final User user) {
        throw new UnsupportedOperationException();
    }

    //TODO: remove
    public void updatePasswordWithEncrypting(final User user, final String newPassword) {
        throw new UnsupportedOperationException();
    }
}
