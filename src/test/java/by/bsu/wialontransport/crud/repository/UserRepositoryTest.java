package by.bsu.wialontransport.crud.repository;

import by.bsu.wialontransport.base.AbstractContextTest;
import by.bsu.wialontransport.crud.entity.UserEntity;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static by.bsu.wialontransport.crud.entity.UserEntity.Role.USER;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

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
                .password("password")
                .role(USER)
                .build();
        checkEquals(expected, actual);
    }

    @Test
    public void userShouldBeInserted() {
        final UserEntity givenUser = UserEntity.builder()
                .id(255L)
                .email("vladzuev.00@mail.ru")
                .password("password")
                .role(USER)
                .build();

        super.startQueryCount();
        this.repository.save(givenUser);
        super.checkQueryCount(1);
    }

    private static void checkEquals(final UserEntity expected, final UserEntity actual) {
        assertEquals(expected.getId(), actual.getId());
        assertEquals(expected.getEmail(), actual.getEmail());
        assertEquals(expected.getPassword(), actual.getPassword());
        assertSame(expected.getRole(), actual.getRole());
    }
}
