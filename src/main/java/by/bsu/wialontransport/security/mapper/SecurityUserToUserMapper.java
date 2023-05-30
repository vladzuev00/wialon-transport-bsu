package by.bsu.wialontransport.security.mapper;

import by.bsu.wialontransport.crud.dto.User;
import by.bsu.wialontransport.security.model.SecurityUser;
import org.springframework.stereotype.Component;

@Component
public final class SecurityUserToUserMapper {

    public User map(final SecurityUser securityUser) {
        return new User(
                securityUser.getId(),
                securityUser.getEmail(),
                securityUser.getPassword(),
                securityUser.getRole().getUserRole()
        );
    }

}
