package by.bsu.wialontransport.security.mapper;

import by.bsu.wialontransport.crud.dto.User;
import by.bsu.wialontransport.crud.entity.UserEntity.Role;
import by.bsu.wialontransport.security.model.SecurityUser;
import by.bsu.wialontransport.security.model.SecurityUser.SecurityRole;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public final class UserToSecurityUserMapperTest {
    private final UserToSecurityUserMapper mapper = new UserToSecurityUserMapper();

    @Test
    public void userShouldBeMappedToSecurityUser() {
        final User givenUser = User.builder()
                .id(255L)
                .email("vladzuev.00@mail.ru")
                .password("password")
                .role(Role.USER)
                .build();

        final SecurityUser actual = this.mapper.map(givenUser);
        final SecurityUser expected = SecurityUser.builder()
                .id(255L)
                .email("vladzuev.00@mail.ru")
                .password("password")
                .role(SecurityRole.USER)
                .build();
        assertEquals(expected, actual);
    }

}
