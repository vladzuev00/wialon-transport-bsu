package by.bsu.wialontransport.crud.repository;

import by.bsu.wialontransport.base.AbstractContextTest;
import by.bsu.wialontransport.crud.entity.UserEntity;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

import static by.bsu.wialontransport.crud.entity.UserEntity.Role.USER;
import static by.bsu.wialontransport.util.entity.UserEntityUtil.checkEquals;
import static java.lang.Long.MAX_VALUE;
import static org.junit.Assert.*;

public final class UserRepositoryTest extends AbstractContextTest {

    @Autowired
    private UserRepository repository;

    @Test
    public void userShouldBeFoundById() {
        super.startQueryCount();
        final UserEntity actual = this.repository.findById(255L).orElseThrow();
        super.checkQueryCount(1);

        final UserEntity expected = UserEntity.builder()
                .id(255L)
                .email("vladzuev.00@mail.ru")
                .password("$2a$10$8y9hC00YePN.9uH.OLCQ6OWeaR8G9q/U9MEvizLx9zaBkwe0KItHG")
                .role(USER)
                .build();
        checkEquals(expected, actual);
    }

    @Test
    public void userShouldBeInserted() {
        final UserEntity givenUser = UserEntity.builder()
                .email("test@mail.ru")
                .password("password")
                .role(USER)
                .build();

        super.startQueryCount();
        this.repository.save(givenUser);
        super.checkQueryCount(1);
    }

    @Test
    public void userShouldBeFoundByEmail() {
        final String givenEmail = "vladzuev.00@mail.ru";

        super.startQueryCount();
        final UserEntity actual = this.repository.findByEmail(givenEmail).orElseThrow();
        super.checkQueryCount(1);

        final UserEntity expected = UserEntity.builder()
                .id(255L)
                .email(givenEmail)
                .password("$2a$10$8y9hC00YePN.9uH.OLCQ6OWeaR8G9q/U9MEvizLx9zaBkwe0KItHG")
                .role(USER)
                .build();
        checkEquals(expected, actual);
    }

    @Test
    public void userShouldNotBeFoundByEmail() {
        super.startQueryCount();
        final Optional<UserEntity> optionalEntity = this.repository.findByEmail("email@mail.ru");
        super.checkQueryCount(1);

        assertTrue(optionalEntity.isEmpty());
    }

    @Test
    public void userShouldExistsByEmail() {
        super.startQueryCount();
        final boolean exists = this.repository.existsByEmail("vladzuev.00@mail.ru");
        super.checkQueryCount(1);

        assertTrue(exists);
    }

    @Test
    public void userShouldNotExistsByEmail() {
        super.startQueryCount();
        final boolean exists = this.repository.existsByEmail("notexist@mail.ru");
        super.checkQueryCount(1);

        assertFalse(exists);
    }

    @Test
    public void userEmailShouldBeUpdated() {
        final Long givenUserId = 255L;
        final String givenNewEmail = "newEmail@mail.ru";

        super.startQueryCount();
        final int actualCountUpdatedRows = this.repository.updateEmail(givenUserId, givenNewEmail);
        super.checkQueryCount(1);

        final int expectedCountUpdatedRows = 1;
        assertEquals(expectedCountUpdatedRows, actualCountUpdatedRows);

        final UserEntity actual = this.repository.findById(givenUserId).orElseThrow();
        final UserEntity expected = UserEntity.builder()
                .id(givenUserId)
                .email(givenNewEmail)
                .password("$2a$10$8y9hC00YePN.9uH.OLCQ6OWeaR8G9q/U9MEvizLx9zaBkwe0KItHG")
                .role(USER)
                .build();
        checkEquals(expected, actual);
    }

    @Test
    public void userEmailShouldNotBeUpdatedBecauseOfNotExistingId() {
        final Long givenUserId = MAX_VALUE;
        final String givenNewEmail = "newEmail@mail.ru";

        super.startQueryCount();
        final int actualCountUpdatedRows = this.repository.updateEmail(givenUserId, givenNewEmail);
        super.checkQueryCount(1);

        final int expectedCountUpdatedRows = 0;
        assertEquals(expectedCountUpdatedRows, actualCountUpdatedRows);
    }

    @Test
    public void userPasswordShouldBeUpdated() {
        super.startQueryCount();
        final int actualCountUpdatedRows = this.repository.updatePassword(255L, "new-password");
        super.checkQueryCount(1);

        final int expectedCountUpdatedRows = 1;
        assertEquals(expectedCountUpdatedRows, actualCountUpdatedRows);

        final UserEntity actual = this.repository.findById(255L).orElseThrow();
        final UserEntity expected = UserEntity.builder()
                .id(255L)
                .email("vladzuev.00@mail.ru")
                .password("new-password")
                .role(USER)
                .build();
        checkEquals(expected, actual);
    }

    @Test
    public void userPasswordShouldBeUpdatedBecauseOfNotExistingId() {
        super.startQueryCount();
        final int actualCountUpdatedRows = this.repository.updatePassword(MAX_VALUE, "new-password");
        super.checkQueryCount(1);

        final int expectedCountUpdatedRows = 0;
        assertEquals(expectedCountUpdatedRows, actualCountUpdatedRows);
    }
}
