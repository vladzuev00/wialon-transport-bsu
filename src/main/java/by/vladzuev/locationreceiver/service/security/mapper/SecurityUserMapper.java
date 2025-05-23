package by.vladzuev.locationreceiver.service.security.mapper;

import by.vladzuev.locationreceiver.crud.dto.User;
import by.vladzuev.locationreceiver.service.security.model.SecurityUser;
import org.springframework.stereotype.Component;

@Component
public final class SecurityUserMapper {

    public User map(final SecurityUser securityUser) {
        return new User(
                securityUser.getId(),
                securityUser.getEmail(),
                securityUser.getPassword(),
                securityUser.getRole()
        );
    }

    public SecurityUser map(final User user) {
        return new SecurityUser(
                user.getId(),
                user.getEmail(),
                user.getPassword(),
                user.getRole()
        );
    }

}
