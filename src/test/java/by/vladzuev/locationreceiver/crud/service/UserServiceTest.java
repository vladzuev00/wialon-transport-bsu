package by.vladzuev.locationreceiver.crud.service;

import by.vladzuev.locationreceiver.base.AbstractSpringBootTest;
import by.vladzuev.locationreceiver.crud.dto.User;
import by.vladzuev.locationreceiver.crud.entity.UserEntity;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

import static java.lang.Long.MAX_VALUE;
import static org.junit.Assert.*;

public final class UserServiceTest extends AbstractSpringBootTest {

    @Autowired
    private UserService service;

    @Test
    public void userShouldBeFoundByEmail() {
        final String givenEmail = "vladzuev.00@mail.ru";

        final Optional<User> optionalActual = service.findByEmail(givenEmail);
        assertTrue(optionalActual.isPresent());

        final User actual = optionalActual.get();
        final User expected = User.builder()
                .id(255L)
                .email(givenEmail)
                .password("$2a$10$8y9hC00YePN.9uH.OLCQ6OWeaR8G9q/U9MEvizLx9zaBkwe0KItHG")
                .role(UserEntity.Role.USER)
                .build();
        assertEquals(expected, actual);
    }

    @Test
    public void userShouldNotBeFoundByEmail() {
        final String givenEmail = "email@mail.ru";

        final Optional<User> optionalActual = service.findByEmail(givenEmail);
        assertTrue(optionalActual.isEmpty());
    }

    @Test
    public void userShouldExistsByEmail() {
        final String givenEmail = "vladzuev.00@mail.ru";

        final boolean exists = service.isExistByEmail(givenEmail);
        assertTrue(exists);
    }

    @Test
    public void userShouldNotExistByEmail() {
        final String givenEmail = "notexist@mail.ru";

        final boolean exists = service.isExistByEmail(givenEmail);
        assertFalse(exists);
    }

    @Test
    public void emailShouldBeUpdated() {
        final Long givenUserId = 255L;
        final User givenUser = createUser(givenUserId);
        final String givenNewEmail = "newEmail@mail.ru";

        final int actualCountUpdatedRows = service.updateEmail(givenUser, givenNewEmail);
        final int expectedCountUpdatedRows = 1;
        assertEquals(expectedCountUpdatedRows, actualCountUpdatedRows);

        final User actual = service.findById(givenUserId).orElseThrow();
        final User expected = User.builder()
                .id(givenUserId)
                .email(givenNewEmail)
                .password("$2a$10$8y9hC00YePN.9uH.OLCQ6OWeaR8G9q/U9MEvizLx9zaBkwe0KItHG")
                .role(UserEntity.Role.USER)
                .build();
        assertEquals(expected, actual);
    }

    @Test
    public void emailShouldNotBeUpdatedBecauseOfNotExistingUserId() {
        final Long givenUserId = MAX_VALUE;
        final User givenUser = createUser(givenUserId);
        final String givenNewEmail = "newEmail@mail.ru";

        final int actualCountUpdatedRows = service.updateEmail(givenUser, givenNewEmail);
        final int expectedCountUpdatedRows = 0;
        assertEquals(expectedCountUpdatedRows, actualCountUpdatedRows);
    }

    private static User createUser(final Long id) {
        return User.builder()
                .id(id)
                .build();
    }
}
