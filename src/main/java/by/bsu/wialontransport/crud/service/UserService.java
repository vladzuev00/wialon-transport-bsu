package by.bsu.wialontransport.crud.service;

import by.bsu.wialontransport.crud.dto.User;
import by.bsu.wialontransport.crud.entity.UserEntity;
import by.bsu.wialontransport.crud.mapper.UserMapper;
import by.bsu.wialontransport.crud.repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class UserService extends AbstractCRUDService<Long, UserEntity, User, UserMapper, UserRepository> {

    //TODO: do encrypting in another service
    private final BCryptPasswordEncoder passwordEncoder;

    public UserService(final UserMapper mapper,
                       final UserRepository repository,
                       final BCryptPasswordEncoder passwordEncoder) {
        super(mapper, repository);
        this.passwordEncoder = passwordEncoder;
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

    public User saveWithEncryptingPassword(final User user) {
        final UserEntity entity = super.mapper.mapToEntity(user);
        this.setEncryptedPassword(entity);
        final UserEntity savedEntity = super.repository.save(entity);
        return super.mapper.mapToDto(savedEntity);
    }

    //TODO: test
    public void updatePasswordWithEncrypting(final User user, final String newPassword) {
        final Long userId = user.getId();
        final String encryptedNewPassword = this.passwordEncoder.encode(newPassword);
        super.repository.updatePassword(userId, encryptedNewPassword);
    }

    private void setEncryptedPassword(final UserEntity user) {
        final String password = user.getPassword();
        final String encryptedPassword = this.passwordEncoder.encode(password);
        user.setPassword(encryptedPassword);
    }
}
