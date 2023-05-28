package by.bsu.wialontransport.security.model;

import by.bsu.wialontransport.crud.entity.UserEntity;
import org.junit.Test;

import static by.bsu.wialontransport.security.model.SecurityUser.SecurityRole.findByRole;
import static org.junit.Assert.assertSame;

public final class SecurityUserTest {

    @Test
    public void securityRoleShouldBeFoundByUserRole() {
        final UserEntity.Role givenRole = UserEntity.Role.USER;

        final SecurityUser.SecurityRole actual = findByRole(givenRole);
        assertSame(SecurityUser.SecurityRole.USER, actual);
    }

    @Test(expected = IllegalStateException.class)
    public void securityRoleShouldNotBeFoundByUserRole() {
        findByRole(null);
    }

}
