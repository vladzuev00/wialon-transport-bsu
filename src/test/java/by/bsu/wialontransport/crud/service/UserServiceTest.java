package by.bsu.wialontransport.crud.service;

import by.bsu.wialontransport.base.AbstractContextTest;
import by.bsu.wialontransport.crud.dto.User;
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

}
