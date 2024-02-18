package by.bsu.wialontransport.model.form.mapper;

import by.bsu.wialontransport.base.AbstractSpringBootTest;
import by.bsu.wialontransport.crud.dto.User;
import by.bsu.wialontransport.model.form.UserForm;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static by.bsu.wialontransport.crud.entity.UserEntity.Role.USER;
import static org.junit.Assert.assertEquals;

public final class UserFormMapperTest extends AbstractSpringBootTest {

    @Autowired
    private UserFormMapper mapper;

    @Test
    public void formShouldBeMappedToUser() {
        final UserForm givenForm = UserForm.builder()
                .email("vladzuev.00@mail.ru")
                .password("password")
                .confirmedPassword("password")
                .build();

        final User actual = this.mapper.map(givenForm);
        final User expected = User.builder()
                .email("vladzuev.00@mail.ru")
                .password("password")
                .role(USER)
                .build();
        assertEquals(expected, actual);
    }

}
