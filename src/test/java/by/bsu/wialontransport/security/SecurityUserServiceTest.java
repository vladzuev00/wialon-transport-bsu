package by.bsu.wialontransport.security;

import by.bsu.wialontransport.crud.dto.User;
import by.bsu.wialontransport.crud.entity.UserEntity.Role;
import by.bsu.wialontransport.crud.service.UserService;
import by.bsu.wialontransport.security.mapper.UserToSecurityUserMapper;
import by.bsu.wialontransport.security.model.SecurityUser;
import by.bsu.wialontransport.security.model.SecurityUser.SecurityRole;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static java.util.Optional.empty;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public final class SecurityUserServiceTest {

    @Mock
    private UserService mockedUserService;

    @Mock
    private UserToSecurityUserMapper mockedMapper;

    private SecurityUserService securityUserService;

    @Before
    public void initializeSecurityUserService() {
        this.securityUserService = new SecurityUserService(this.mockedUserService, this.mockedMapper);
    }

    @Test
    public void userShouldBeLoadedByUsername() {
        final String givenEmail = "vladzuev.00@mail.ru";

        final User givenUser = User.builder()
                .id(255L)
                .email(givenEmail)
                .password("password")
                .role(Role.USER)
                .build();
        when(this.mockedUserService.findByEmail(givenEmail)).thenReturn(Optional.of(givenUser));

        final SecurityUser givenSecurityUser = SecurityUser.builder()
                .id(255L)
                .email(givenEmail)
                .password("password")
                .role(SecurityRole.USER)
                .build();
        when(this.mockedMapper.map(givenUser)).thenReturn(givenSecurityUser);

        final UserDetails actual = this.securityUserService.loadUserByUsername(givenEmail);
        assertEquals(givenSecurityUser, actual);
    }

    @Test(expected = UsernameNotFoundException.class)
    public void userShouldNotBeLoadedByUsername() {
        final String givenEmail = "vladzuev.00@mail.ru";

        when(this.mockedUserService.findByEmail(givenEmail)).thenReturn(empty());

        this.securityUserService.loadUserByUsername(givenEmail);
    }

}