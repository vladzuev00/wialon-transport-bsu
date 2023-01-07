package by.bsu.wialontransport.crud.mapper;

import by.bsu.wialontransport.crud.dto.User;
import by.bsu.wialontransport.crud.entity.UserEntity;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public final class UserMapper extends AbstractMapper<UserEntity, User> {

    public UserMapper(final ModelMapper modelMapper) {
        super(modelMapper, UserEntity.class, User.class);
    }

    @Override
    protected User createDto(final UserEntity entity) {
        return new User(entity.getId(), entity.getEmail(), entity.getPassword(), entity.getRole());
    }
}
