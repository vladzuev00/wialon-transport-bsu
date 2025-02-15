package by.vladzuev.locationreceiver.model.form.mapper;

import by.vladzuev.locationreceiver.model.form.UserForm;
import by.vladzuev.locationreceiver.crud.dto.User;
import org.springframework.stereotype.Component;

import static by.vladzuev.locationreceiver.crud.entity.UserEntity.Role.USER;

@Component
public final class UserFormMapper {

    public User map(final UserForm userForm) {
        return User.builder()
                .email(userForm.getEmail())
                .password(userForm.getPassword())
                .role(USER)
                .build();
    }

}
