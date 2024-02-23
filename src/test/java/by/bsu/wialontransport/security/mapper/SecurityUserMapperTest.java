package by.bsu.wialontransport.security.mapper;

import by.bsu.wialontransport.crud.dto.User;
import by.bsu.wialontransport.crud.entity.UserEntity.Role;
import by.bsu.wialontransport.service.security.mapper.SecurityUserMapper;
import by.bsu.wialontransport.service.security.model.SecurityUser;
import org.junit.Test;

import static by.bsu.wialontransport.crud.entity.UserEntity.Role.USER;
import static org.junit.Assert.assertEquals;

public final class SecurityUserMapperTest {
    private final SecurityUserMapper mapper = new SecurityUserMapper();

    @Test
    public void securityUserShouldBeMappedToUser() {
        final SecurityUser givenSecurityUser = SecurityUser.builder()
                .id(255L)
                .email("vladzuev.00@mail.ru")
                .password("password")
                .role(USER)
                .build();

        final User actual = this.mapper.map(givenSecurityUser);
        final User expected = User.builder()
                .id(255L)
                .email("vladzuev.00@mail.ru")
                .password("password")
                .role(USER)
                .build();
        assertEquals(expected, actual);
    }

    @Test
    public void userShouldBeMappedToSecurityUser() {
        final User givenUser = User.builder()
                .id(255L)
                .email("vladzuev.00@mail.ru")
                .password("password")
                .role(USER)
                .build();

        final SecurityUser actual = this.mapper.map(givenUser);
        final SecurityUser expected = SecurityUser.builder()
                .id(255L)
                .email("vladzuev.00@mail.ru")
                .password("password")
                .role(USER)
                .build();
        assertEquals(expected, actual);
    }

}
