package by.vladzuev.locationreceiver.crud.repository;

import by.vladzuev.locationreceiver.base.AbstractSpringBootTest;
import by.vladzuev.locationreceiver.crud.entity.UserEntity;
import by.vladzuev.locationreceiver.util.entity.UserEntityUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

import static by.vladzuev.locationreceiver.crud.enumeration.UserRole.USER;
import static java.lang.Long.MAX_VALUE;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public final class UserRepositoryTest extends AbstractSpringBootTest {

    @Autowired
    private UserRepository repository;

    @Test
    public void userShouldBeFoundById() {
        final Long givenId = 255L;

        final Optional<UserEntity> optionalActual = repository.findById(givenId);
        assertTrue(optionalActual.isPresent());
        final UserEntity actual = optionalActual.get();
        final UserEntity expected = new UserEntity(
                givenId,
                "vladzuev.00@mail.ru",
                "$2a$10$8y9hC00YePN.9uH.OLCQ6OWeaR8G9q/U9MEvizLx9zaBkwe0KItHG",
                USER
        );
        UserEntityUtil.assertEquals(expected, actual);
    }

    @Test
    public void userShouldBeSaved() {
        final UserEntity givenUser = UserEntity.builder()
                .email("test@mail.ru")
                .password("password")
                .role(USER)
                .build();

        final UserEntity actual = repository.save(givenUser);
        final UserEntity expected = new UserEntity(1L, "test@mail.ru", "password", USER);
        UserEntityUtil.assertEquals(expected, actual);
    }

    @Test
    public void userShouldBeFoundByEmail() {
        final String givenEmail = "vladzuev.00@mail.ru";

        final Optional<UserEntity> optionalActual = repository.findByEmail(givenEmail);
        assertTrue(optionalActual.isPresent());
        final UserEntity actual = optionalActual.get();
        final UserEntity expected = UserEntity.builder().id(255L).build();
        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void userShouldNotBeFoundByEmail() {
        final String givenEmail = "email@mail.ru";

        final Optional<UserEntity> optionalEntity = repository.findByEmail(givenEmail);
        assertTrue(optionalEntity.isEmpty());
    }

    @Test
    public void userShouldExistByEmail() {
        final String givenEmail = "vladzuev.00@mail.ru";

        assertTrue(repository.existsByEmail(givenEmail));
    }

    @Test
    public void userShouldNotExistByEmail() {
        final String givenEmail = "email@mail.ru";

        assertFalse(repository.existsByEmail(givenEmail));
    }

    @Test
    public void userEmailShouldBeUpdated() {
        final Long givenUserId = 255L;
        final String givenNewEmail = "newEmail@mail.ru";

        final int actual = repository.updateEmail(givenUserId, givenNewEmail);
        final int expected = 1;
        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void userEmailShouldNotBeUpdatedBecauseOfNoSuchUser() {
        final Long givenUserId = MAX_VALUE;
        final String givenNewEmail = "newEmail@mail.ru";

        final int actual = repository.updateEmail(givenUserId, givenNewEmail);
        final int expected = 0;
        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void userPasswordShouldBeUpdated() {
        final Long givenId = 255L;
        final String givenNewEncryptedPassword = "new-password";

        final int actual = repository.updatePassword(givenId, givenNewEncryptedPassword);
        final int expected = 1;
        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void userPasswordShouldBeUpdatedBecauseOfNoSuchUser() {
        final Long givenId = MAX_VALUE;
        final String givenNewEncryptedPassword = "new-password";

        final int actual = repository.updatePassword(givenId, givenNewEncryptedPassword);
        final int expected = 0;
        Assertions.assertEquals(expected, actual);
    }
}
