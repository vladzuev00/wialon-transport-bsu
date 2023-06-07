package by.bsu.wialontransport.model.form.mapper;

import by.bsu.wialontransport.model.form.UserForm;
import by.bsu.wialontransport.crud.dto.User;
import org.springframework.stereotype.Component;

import static by.bsu.wialontransport.crud.entity.UserEntity.Role.USER;

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
