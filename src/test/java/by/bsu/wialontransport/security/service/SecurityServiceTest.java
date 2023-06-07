package by.bsu.wialontransport.security.service;

import by.bsu.wialontransport.crud.dto.User;
import by.bsu.wialontransport.security.mapper.SecurityUserMapper;
import by.bsu.wialontransport.security.model.SecurityUser;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public final class SecurityServiceTest {

    @Mock
    private SecurityUserMapper mockedMapper;

    private SecurityService securityService;

    @Before
    public void initializeSecurityService() {
        this.securityService = new SecurityService(this.mockedMapper);
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
            when(this.mockedMapper.map(same(givenSecurityUser))).thenReturn(givenUser);

            final User actual = this.securityService.findLoggedOnUser();
            assertSame(givenUser, actual);
        }
    }

}
