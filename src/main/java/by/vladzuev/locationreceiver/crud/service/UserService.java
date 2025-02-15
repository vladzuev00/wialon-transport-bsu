package by.vladzuev.locationreceiver.crud.service;

import by.vladzuev.locationreceiver.crud.dto.User;
import by.vladzuev.locationreceiver.crud.entity.UserEntity;
import by.vladzuev.locationreceiver.crud.mapper.UserMapper;
import by.vladzuev.locationreceiver.crud.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class UserService extends CRUDEncryptingPasswordService<Long, UserEntity, User, UserMapper, UserRepository> {

    public UserService(final UserMapper mapper,
                       final UserRepository repository,
                       final PasswordEncoder passwordEncoder) {
        super(mapper, repository, passwordEncoder);
    }

    @Transactional(readOnly = true)
    public Optional<User> findByEmail(final String email) {
        return findUniqueDto(repository -> repository.findByEmail(email));
    }

    @Transactional(readOnly = true)
    public boolean isExistByEmail(final String email) {
        return findBoolean(repository -> repository.existsByEmail(email));
    }

    public int updateEmail(final User user, final String newEmail) {
        return findInt(repository -> repository.updateEmail(user.getId(), newEmail));
    }
}
