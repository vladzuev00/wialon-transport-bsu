package by.vladzuev.locationreceiver.crud.mapper;

import by.vladzuev.locationreceiver.crud.dto.User;
import by.vladzuev.locationreceiver.crud.entity.UserEntity;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Component;

@Component
public final class UserMapper extends Mapper<UserEntity, User> {

    public UserMapper(final ModelMapper modelMapper) {
        super(modelMapper, UserEntity.class, User.class);
    }

    @Override
    protected User createDto(final UserEntity source) {
        return new User(
                source.getId(),
                source.getEmail(),
                source.getPassword(),
                source.getRole()
        );
    }

    @Override
    protected void mapSpecificFields(final User source, final UserEntity destination) {

    }
}
