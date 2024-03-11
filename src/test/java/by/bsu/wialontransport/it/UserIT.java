package by.bsu.wialontransport.it;

import by.bsu.wialontransport.base.AbstractSpringBootTest;
import by.bsu.wialontransport.service.security.exception.NoAuthorizedUserException;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.Assert.assertSame;
import static org.skyscreamer.jsonassert.JSONAssert.assertEquals;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.http.HttpStatus.OK;

@SpringBootTest(webEnvironment = RANDOM_PORT)
public final class UserIT extends AbstractSpringBootTest {
    private static final String CONTROLLER_URL = "/registration";
    private static final String URL_TO_GET_AUTHORIZED_USER = CONTROLLER_URL + "/authorized";

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void authorizedUserShouldBeGot()
            throws Exception {
        final ResponseEntity<String> actual = restTemplate
                .withBasicAuth("vladzuev.00@mail.ru", "password")
                .getForEntity(URL_TO_GET_AUTHORIZED_USER, String.class);

        final HttpStatus actualStatus = actual.getStatusCode();
        assertSame(OK, actualStatus);

        final String actualBody = actual.getBody();
        final String expectedBody = """
                {
                  "id": 255,
                  "email": "vladzuev.00@mail.ru",
                  "role": "USER"
                }""";
        assertEquals(expectedBody, actualBody, true);
    }

    @Test(expected = NoAuthorizedUserException.class)
    public void authorizedUserShouldNotBeGot() {

    }
}
