package by.bsu.wialontransport.crud.service;

import by.bsu.wialontransport.base.AbstractContextTest;
import by.bsu.wialontransport.crud.dto.User;
import by.bsu.wialontransport.crud.entity.UserEntity.Role;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

import static by.bsu.wialontransport.crud.entity.UserEntity.Role.USER;
import static org.junit.Assert.*;

public final class UserServiceTest extends AbstractContextTest {

    @Autowired
    private UserService service;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Test
    public void userShouldBeFoundByEmail() {
        final String givenEmail = "vladzuev.00@mail.ru";

        final User actual = this.service.findByEmail(givenEmail).orElseThrow();
        final User expected = User.builder()
                .id(255L)
                .email(givenEmail)
                .password("password")
                .role(USER)
                .build();
        assertEquals(expected, actual);
    }

    @Test
    public void userShouldNotBeFoundByEmail() {
        final Optional<User> optionalUser = this.service.findByEmail("email@mail.ru");
        assertTrue(optionalUser.isEmpty());
    }

    @Test
    public void userShouldExistsByEmail() {
        final boolean exists = this.service.isExistByEmail("vladzuev.00@mail.ru");
        assertTrue(exists);
    }

    @Test
    public void userShouldNotExistByEmail() {
        final boolean exists = this.service.isExistByEmail("notexist@mail.ru");
        assertFalse(exists);
    }

    @Test
    public void userShouldBeSavedWithEncryptedPassword() {
        final String givenEmail = "vladzuev.01@mail.ru";
        final String givenPassword = "password";
        final Role givenRole = USER;
        final User givenUser = User.builder()
                .email("vladzuev.01@mail.ru")
                .password(givenPassword)
                .role(givenRole)
                .build();

        final User actualSavedUser = this.service.saveWithEncryptingPassword(givenUser);
        final User actualSavedUserFromDataBase = this.service.findById(actualSavedUser.getId()).orElseThrow();
        final User expected = User.builder()
                .email(givenEmail)
                .password(givenPassword)
                .role(givenRole)
                .build();

        checkEqualsExceptId(expected, actualSavedUser);
        checkEqualsExceptId(expected, actualSavedUserFromDataBase);
        assertEquals(actualSavedUser, actualSavedUserFromDataBase);
    }

    private void checkEqualsExceptId(final User expected, final User actual) {
        assertEquals(expected.getEmail(), actual.getEmail());
        assertTrue(this.passwordEncoder.matches(expected.getPassword(), actual.getPassword()));
        assertSame(expected.getRole(), actual.getRole());
    }

}
