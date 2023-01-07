package by.bsu.wialontransport.crud.mapper;

import by.bsu.wialontransport.base.AbstractContextTest;
import by.bsu.wialontransport.crud.dto.User;
import by.bsu.wialontransport.crud.entity.UserEntity;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static by.bsu.wialontransport.crud.entity.UserEntity.Role.USER;
import static org.junit.Assert.*;

public final class UserMapperTest extends AbstractContextTest {

    @Autowired
    private UserMapper mapper;

    @Test
    public void dtoShouldBeMappedToEntity() {
        final User givenDto = User.builder()
                .id(255L)
                .email("vladzuev.00@mail.ru")
                .password("password")
                .role(USER)
                .build();

        final UserEntity actual = this.mapper.mapToEntity(givenDto);
        final UserEntity expected = UserEntity.builder()
                .id(255L)
                .email("vladzuev.00@mail.ru")
                .password("password")
                .role(USER)
                .build();
        assertNotNull(actual);
        checkEquals(expected, actual);
    }

    @Test
    public void entityShouldBeMappedToDto() {
        final UserEntity givenEntity = UserEntity.builder()
                .id(255L)
                .email("vladzuev.00@mail.ru")
                .password("password")
                .role(USER)
                .build();

        final User actual = this.mapper.createDto(givenEntity);
        final User expected = User.builder()
                .id(255L)
                .email("vladzuev.00@mail.ru")
                .password("password")
                .role(USER)
                .build();
        assertEquals(expected, actual);
    }

    private static void checkEquals(final UserEntity expected, final UserEntity actual) {
        assertEquals(expected.getId(), actual.getId());
        assertEquals(expected.getEmail(), actual.getEmail());
        assertEquals(expected.getPassword(), actual.getPassword());
        assertSame(expected.getRole(), actual.getRole());
    }
}
