package by.bsu.wialontransport.config;

import by.bsu.wialontransport.base.AbstractSpringBootTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;

import static org.junit.Assert.assertSame;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.http.HttpStatus.OK;

@SpringBootTest(webEnvironment = RANDOM_PORT)
public final class WebSecurityConfigTest extends AbstractSpringBootTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void registrationShouldBeAllowedForNotAuthorizedUser() {
        final ResponseEntity<String> actual = restTemplate.getForEntity("/registration", String.class);
        assertSame(OK, actual.getStatusCode());
    }
}
