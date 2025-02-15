package by.vladzuev.locationreceiver.controller.user.view;

import by.vladzuev.locationreceiver.crud.dto.User;
import by.vladzuev.locationreceiver.crud.entity.UserEntity;
import lombok.Value;

@Value
public class UserView {
    Long id;
    String email;
    UserEntity.Role role;

    //TODO: test
    public UserView(final User user) {
        id = user.getId();
        email = user.getEmail();
        role = user.getRole();
    }
}
