package by.bsu.wialontransport.config;

import by.bsu.wialontransport.base.AbstractSpringBootTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.junit.Assert.assertNotNull;

public final class PasswordEncoderConfigTest extends AbstractSpringBootTest {

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Test
    public void passwordEncoderShouldBeCreated() {
        assertNotNull(passwordEncoder);
    }
}
