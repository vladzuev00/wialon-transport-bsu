package by.bsu.wialontransport.service.security.model;

import org.junit.Test;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;
import java.util.Set;

import static by.bsu.wialontransport.crud.entity.UserEntity.Role.USER;
import static org.junit.Assert.*;

public final class SecurityUserTest {

    @Test
    public void authoritiesShouldBeGot() {
        final SecurityUser givenUser = SecurityUser.builder()
                .role(USER)
                .build();

        final Collection<? extends GrantedAuthority> actual = givenUser.getAuthorities();
        final Collection<? extends GrantedAuthority> expected = Set.of(USER);
        assertEquals(expected, actual);
    }

    @Test
    public void passwordShouldBeGot() {
        final String givenPassword = "password";
        final SecurityUser givenUser = SecurityUser.builder()
                .password(givenPassword)
                .build();

        final String actual = givenUser.getPassword();
        assertSame(givenPassword, actual);
    }

    @Test
    public void userNameShouldBeGot() {
        final String givenEmail = "email";
        final SecurityUser givenUser = SecurityUser.builder()
                .email(givenEmail)
                .build();

        final String actual = givenUser.getUsername();
        assertSame(givenEmail, actual);
    }

    @Test
    public void accountShouldBeNonExpired() {
        final SecurityUser givenUser = SecurityUser.builder().build();

        assertTrue(givenUser.isAccountNonExpired());
    }

    @Test
    public void accountShouldBeNonLocked() {
        final SecurityUser givenUser = SecurityUser.builder().build();

        assertTrue(givenUser.isAccountNonLocked());
    }

    @Test
    public void credentialsShouldBeNonExpired() {
        final SecurityUser givenUser = SecurityUser.builder().build();

        assertTrue(givenUser.isCredentialsNonExpired());
    }

    @Test
    public void userShouldBeEnabled() {
        final SecurityUser givenUser = SecurityUser.builder().build();

        assertTrue(givenUser.isEnabled());
    }
}
