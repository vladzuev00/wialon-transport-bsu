package by.vladzuev.locationreceiver.controller.user.view;

import by.vladzuev.locationreceiver.crud.dto.User;
import by.vladzuev.locationreceiver.crud.enumeration.UserRole;
import lombok.Value;

@Value
public class UserView {
    Long id;
    String email;
    UserRole role;

    //TODO: test
    public UserView(final User user) {
        id = user.getId();
        email = user.getEmail();
        role = user.getRole();
    }
}
