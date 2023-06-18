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
public class UserService
        extends AbstractCRUDEncryptingPasswordService<Long, UserEntity, User, UserMapper, UserRepository> {

    public UserService(final UserMapper mapper,
                       final UserRepository repository,
                       final BCryptPasswordEncoder passwordEncoder) {
        super(mapper, repository, passwordEncoder);
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

    public void updateEmail(final User user, final String email) {
        final Long userId = user.getId();
        super.repository.updateEmail(userId, email);
    }
}
