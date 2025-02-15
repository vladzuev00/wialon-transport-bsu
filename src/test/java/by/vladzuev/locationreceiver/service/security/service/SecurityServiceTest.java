package by.vladzuev.locationreceiver.service.security.service;

import by.vladzuev.locationreceiver.crud.dto.User;
import by.vladzuev.locationreceiver.crud.service.UserService;
import by.vladzuev.locationreceiver.service.security.exception.NoAuthorizedUserException;
import by.vladzuev.locationreceiver.service.security.mapper.SecurityUserMapper;
import by.vladzuev.locationreceiver.service.security.model.SecurityUser;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static by.vladzuev.locationreceiver.crud.entity.UserEntity.Role.USER;
import static java.util.Optional.empty;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.mockito.ArgumentMatchers.same;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public final class SecurityServiceTest {

    @Mock
    private UserService mockedUserService;

    @Mock
    private SecurityUserMapper mockedUserMapper;

    private SecurityService securityService;

    @Before
    public void initializeSecurityService() {
        securityService = new SecurityService(mockedUserService, mockedUserMapper);
    }

    @Test
    public void userShouldBeLoadedByUsername() {
        final String givenEmail = "vladzuev.00@mail.ru";

        final User givenUser = User.builder()
                .id(255L)
                .email(givenEmail)
                .password("password")
                .role(USER)
                .build();
        when(mockedUserService.findByEmail(givenEmail)).thenReturn(Optional.of(givenUser));

        final SecurityUser givenSecurityUser = SecurityUser.builder()
                .id(255L)
                .email(givenEmail)
                .password("password")
                .role(USER)
                .build();
        when(mockedUserMapper.map(givenUser)).thenReturn(givenSecurityUser);

        final UserDetails actual = securityService.loadUserByUsername(givenEmail);
        assertEquals(givenSecurityUser, actual);
    }

    @Test(expected = UsernameNotFoundException.class)
    public void userShouldNotBeLoadedByUsername() {
        final String givenEmail = "vladzuev.00@mail.ru";

        when(mockedUserService.findByEmail(givenEmail)).thenReturn(empty());

        securityService.loadUserByUsername(givenEmail);
    }

    @Test
    public void loggedOnUserShouldBeFound() {
        try (final MockedStatic<SecurityContextHolder> mockedStatic = mockStatic(SecurityContextHolder.class)) {
            final SecurityContext givenContext = mock(SecurityContext.class);
            mockedStatic.when(SecurityContextHolder::getContext).thenReturn(givenContext);

            final Authentication givenAuthentication = mock(Authentication.class);
            when(givenContext.getAuthentication()).thenReturn(givenAuthentication);

            final SecurityUser givenSecurityUser = SecurityUser.builder().build();
            when(givenAuthentication.getPrincipal()).thenReturn(givenSecurityUser);

            final User givenUser = User.builder().build();
            when(mockedUserMapper.map(same(givenSecurityUser))).thenReturn(givenUser);

            final User actual = securityService.findLoggedOnUser();
            assertSame(givenUser, actual);
        }
    }

    @Test(expected = NoAuthorizedUserException.class)
    public void loggedOnUserShouldNotBeFound() {
        try (final MockedStatic<SecurityContextHolder> mockedStatic = mockStatic(SecurityContextHolder.class)) {
            final SecurityContext givenContext = mock(SecurityContext.class);
            mockedStatic.when(SecurityContextHolder::getContext).thenReturn(givenContext);

            when(givenContext.getAuthentication()).thenReturn(null);

            securityService.findLoggedOnUser();
        }
    }
}
