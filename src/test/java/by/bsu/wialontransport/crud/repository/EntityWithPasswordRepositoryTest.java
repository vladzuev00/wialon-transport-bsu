package by.bsu.wialontransport.crud.repository;

import by.bsu.wialontransport.base.AbstractContextTest;
import by.bsu.wialontransport.crud.entity.UserEntity;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static by.bsu.wialontransport.crud.entity.UserEntity.Role.USER;
import static by.bsu.wialontransport.util.entity.UserEntityUtil.checkEquals;
import static org.junit.Assert.assertEquals;

public final class EntityWithPasswordRepositoryTest extends AbstractContextTest {

    @Autowired
    private UserRepository repository;

    @Test
    public void passwordShouldBeUpdated() {
        final Long givenUserId = 255L;
        final String givenNewEncryptedPassword = "password";

        final int actualCountUpdatedRows = this.repository.updatePassword(givenUserId, givenNewEncryptedPassword);
        final int expectedCountUpdatedRows = 1;
        assertEquals(expectedCountUpdatedRows, actualCountUpdatedRows);

        final UserEntity actual = this.repository.findById(givenUserId).orElseThrow();
        final UserEntity expected = UserEntity.builder()
                .id(givenUserId)
                .email("vladzuev.00@mail.ru")
                .role(USER)
                .password(givenNewEncryptedPassword)
                .build();
        checkEquals(expected, actual);
    }
}
