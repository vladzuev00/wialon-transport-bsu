package by.bsu.wialontransport.crud.service;

import by.bsu.wialontransport.crud.dto.User;
import by.bsu.wialontransport.crud.entity.UserEntity;
import by.bsu.wialontransport.crud.mapper.UserMapper;
import by.bsu.wialontransport.crud.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserService extends AbstractCRUDService<Long, UserEntity, User, UserMapper, UserRepository> {

    public UserService(UserMapper mapper, UserRepository repository) {
        super(mapper, repository);
    }
}
