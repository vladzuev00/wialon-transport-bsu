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
public class UserService extends CRUDEncryptingPasswordService<Long, UserEntity, User, UserMapper, UserRepository> {

    public UserService(final UserMapper mapper,
                       final UserRepository repository,
                       final BCryptPasswordEncoder passwordEncoder) {
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
