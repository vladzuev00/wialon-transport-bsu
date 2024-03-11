package by.bsu.wialontransport.controller.user.view;

import by.bsu.wialontransport.crud.dto.User;
import by.bsu.wialontransport.crud.entity.UserEntity.Role;
import lombok.Value;

@Value
public class UserView {
    Long id;
    String email;
    Role role;

    public UserView(final User user) {
        id = user.getId();
        email = user.getEmail();
        role = user.getRole();
    }
}
