package by.bsu.wialontransport.security.mapper;

import by.bsu.wialontransport.crud.dto.User;
import by.bsu.wialontransport.security.model.SecurityUser;
import org.springframework.stereotype.Component;

import static by.bsu.wialontransport.security.model.SecurityUser.SecurityRole.findByRole;

@Component
public final class UserToSecurityUserMapper {

    public SecurityUser map(final User user) {
        return new SecurityUser(
                user.getId(),
                user.getEmail(),
                user.getPassword(),
                findByRole(user.getRole())
        );
    }

}
