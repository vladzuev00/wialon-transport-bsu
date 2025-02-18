package by.vladzuev.locationreceiver.crud.repository;

import by.vladzuev.locationreceiver.base.AbstractSpringBootTest;
import by.vladzuev.locationreceiver.crud.entity.UserEntity;
import by.vladzuev.locationreceiver.util.entity.UserEntityUtil;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

import static java.lang.Long.MAX_VALUE;
import static org.junit.Assert.*;

public final class UserRepositoryTest extends AbstractSpringBootTest {

    @Autowired
    private UserRepository repository;

    @Test
    public void userShouldBeFoundById() {
        final Long givenId = 255L;

        startQueryCount();
        final Optional<UserEntity> optionalActual = repository.findById(givenId);
        checkQueryCount(1);

        assertTrue(optionalActual.isPresent());
        final UserEntity actual = optionalActual.get();
        final UserEntity expected = UserEntity.builder()
                .id(givenId)
                .email("vladzuev.00@mail.ru")
                .password("$2a$10$8y9hC00YePN.9uH.OLCQ6OWeaR8G9q/U9MEvizLx9zaBkwe0KItHG")
                .role(UserEntity.UserRole.USER)
                .build();
        UserEntityUtil.checkEquals(expected, actual);
    }

    @Test
    public void userShouldBeInserted() {
        final UserEntity givenUser = UserEntity.builder()
                .email("test@mail.ru")
                .password("password")
                .role(UserEntity.UserRole.USER)
                .build();

        startQueryCount();
        repository.save(givenUser);
        checkQueryCount(1);
    }

    @Test
    public void userShouldBeFoundByEmail() {
        final String givenEmail = "vladzuev.00@mail.ru";

        startQueryCount();
        final Optional<UserEntity> optionalActual = repository.findByEmail(givenEmail);
        checkQueryCount(1);

        assertTrue(optionalActual.isPresent());
        final UserEntity actual = optionalActual.get();
        final UserEntity expected = UserEntity.builder()
                .id(255L)
                .email(givenEmail)
                .password("$2a$10$8y9hC00YePN.9uH.OLCQ6OWeaR8G9q/U9MEvizLx9zaBkwe0KItHG")
                .role(UserEntity.UserRole.USER)
                .build();
        UserEntityUtil.checkEquals(expected, actual);
    }

    @Test
    public void userShouldNotBeFoundByEmail() {
        final String givenEmail = "email@mail.ru";

        startQueryCount();
        final Optional<UserEntity> optionalEntity = repository.findByEmail(givenEmail);
        checkQueryCount(1);

        assertTrue(optionalEntity.isEmpty());
    }

    @Test
    public void userShouldExistsByEmail() {
        final String givenEmail = "vladzuev.00@mail.ru";

        startQueryCount();
        final boolean exists = repository.existsByEmail(givenEmail);
        checkQueryCount(1);

        assertTrue(exists);
    }

    @Test
    public void userShouldNotExistsByEmail() {
        final String givenEmail = "notexist@mail.ru";

        startQueryCount();
        final boolean exists = repository.existsByEmail(givenEmail);
        checkQueryCount(1);

        assertFalse(exists);
    }

    @Test
    public void userEmailShouldBeUpdated() {
        final Long givenUserId = 255L;
        final String givenNewEmail = "newEmail@mail.ru";

        startQueryCount();
        final int actualCountUpdatedRows = repository.updateEmail(givenUserId, givenNewEmail);
        checkQueryCount(1);

        final int expectedCountUpdatedRows = 1;
        assertEquals(expectedCountUpdatedRows, actualCountUpdatedRows);

        final UserEntity actual = repository.findById(givenUserId).orElseThrow();
        final UserEntity expected = UserEntity.builder()
                .id(givenUserId)
                .email(givenNewEmail)
                .password("$2a$10$8y9hC00YePN.9uH.OLCQ6OWeaR8G9q/U9MEvizLx9zaBkwe0KItHG")
                .role(UserEntity.UserRole.USER)
                .build();
        UserEntityUtil.checkEquals(expected, actual);
    }

    @Test
    public void userEmailShouldNotBeUpdatedBecauseOfNotExistingId() {
        final Long givenUserId = MAX_VALUE;
        final String givenNewEmail = "newEmail@mail.ru";

        startQueryCount();
        final int actualCountUpdatedRows = repository.updateEmail(givenUserId, givenNewEmail);
        checkQueryCount(1);

        final int expectedCountUpdatedRows = 0;
        assertEquals(expectedCountUpdatedRows, actualCountUpdatedRows);
    }

    @Test
    public void userPasswordShouldBeUpdated() {
        final Long givenId = 255L;
        final String givenNewEncryptedPassword = "new-password";

        startQueryCount();
        final int actualCountUpdatedRows = repository.updatePassword(givenId, givenNewEncryptedPassword);
        checkQueryCount(1);

        final int expectedCountUpdatedRows = 1;
        assertEquals(expectedCountUpdatedRows, actualCountUpdatedRows);

        final UserEntity actual = repository.findById(givenId).orElseThrow();
        final UserEntity expected = UserEntity.builder()
                .id(givenId)
                .email("vladzuev.00@mail.ru")
                .password(givenNewEncryptedPassword)
                .role(UserEntity.UserRole.USER)
                .build();
        UserEntityUtil.checkEquals(expected, actual);
    }

    @Test
    public void userPasswordShouldBeUpdatedBecauseOfNotExistingId() {
        final Long givenId = MAX_VALUE;
        final String givenNewEncryptedPassword = "new-password";

        startQueryCount();
        final int actualCountUpdatedRows = repository.updatePassword(givenId, givenNewEncryptedPassword);
        checkQueryCount(1);

        final int expectedCountUpdatedRows = 0;
        assertEquals(expectedCountUpdatedRows, actualCountUpdatedRows);
    }
}
