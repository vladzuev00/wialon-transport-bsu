package by.bsu.wialontransport.it;

import by.bsu.wialontransport.base.AbstractSpringBootTest;
import by.bsu.wialontransport.service.registration.model.RegisteredUserRequest;
import org.junit.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;

import static by.bsu.wialontransport.util.HttpUtil.JSON_COMPARATOR_IGNORING_DATE_TIME;
import static by.bsu.wialontransport.util.HttpUtil.createHttpEntity;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.test.context.jdbc.Sql.ExecutionPhase.AFTER_TEST_METHOD;
import static org.springframework.transaction.annotation.Propagation.NOT_SUPPORTED;

@Transactional(propagation = NOT_SUPPORTED)
@SpringBootTest(webEnvironment = RANDOM_PORT)
@Sql(statements = "DELETE FROM users WHERE id = 1")
@Sql(statements = "ALTER SEQUENCE users_id_seq RESTART WITH 1", executionPhase = AFTER_TEST_METHOD)
public class RegistrationIT extends AbstractSpringBootTest {
    private static final String URL = "/registration";

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void registrationShouldBeSuccess()
            throws Exception {
        final RegisteredUserRequest givenRequest = RegisteredUserRequest.builder()
                .email("dimasockol@gmail.com")
                .password("dimasockol")
                .confirmedPassword("dimasockol")
                .build();
        final HttpEntity<RegisteredUserRequest> givenHttpEntity = createHttpEntity(givenRequest);

        final String actual = restTemplate.postForObject(URL, givenHttpEntity, String.class);
        final String expected = """
                {
                  "id": 1,
                  "email": "dimasockol@gmail.com",
                  "role": "USER"
                }""";
        JSONAssert.assertEquals(expected, actual, true);
    }

    @Test
    public void registrationShouldBeFailedBecauseOfPasswordConfirming()
            throws Exception {
        final RegisteredUserRequest givenRequest = RegisteredUserRequest.builder()
                .email("dimasockol@gmail.com")
                .password("dimasockol")
                .confirmedPassword("dimasocko")
                .build();
        final HttpEntity<RegisteredUserRequest> givenHttpEntity = createHttpEntity(givenRequest);

        final String actual = restTemplate.postForObject(URL, givenHttpEntity, String.class);
        final String expected = """
                {
                  "httpStatus": "NOT_ACCEPTABLE",
                  "message": "Password and confirmed password aren't equal",
                  "dateTime": "2024-03-08 13-03-35"
                }""";
        JSONAssert.assertEquals(expected, actual, JSON_COMPARATOR_IGNORING_DATE_TIME);
    }

    @Test
    public void registrationShouldBeFailedBecauseOfEmailAlreadyExist()
            throws Exception {
        final RegisteredUserRequest givenRequest = RegisteredUserRequest.builder()
                .email("vladzuev.00@mail.ru")
                .password("dimasockol")
                .confirmedPassword("dimasockol")
                .build();
        final HttpEntity<RegisteredUserRequest> givenHttpEntity = createHttpEntity(givenRequest);

        final String actual = restTemplate.postForObject(URL, givenHttpEntity, String.class);
        final String expected = """
                {
                   "httpStatus": "NOT_ACCEPTABLE",
                   "message": "Email 'vladzuev.00@mail.ru' already exists",
                   "dateTime": "2024-03-08 15-22-26"
                }""";
        JSONAssert.assertEquals(expected, actual, JSON_COMPARATOR_IGNORING_DATE_TIME);
    }

    @Test
    public void registrationShouldBeFailedBecauseOfEmailIsNull()
            throws Exception {
        final RegisteredUserRequest givenRequest = RegisteredUserRequest.builder()
                .password("dimasockol")
                .confirmedPassword("dimasockol")
                .build();
        final HttpEntity<RegisteredUserRequest> givenHttpEntity = createHttpEntity(givenRequest);

        final String actual = restTemplate.postForObject(URL, givenHttpEntity, String.class);
        final String expected = """
                {
                   "httpStatus": "NOT_ACCEPTABLE",
                   "message": "email : Invalid email",
                   "dateTime": "2024-03-08 15-25-22"
                }""";
        JSONAssert.assertEquals(expected, actual, JSON_COMPARATOR_IGNORING_DATE_TIME);
    }

    @Test
    public void registrationShouldBeFailedBecauseOfEmailIsNotValid()
            throws Exception {
        final RegisteredUserRequest givenRequest = RegisteredUserRequest.builder()
                .email("dimasockolgmail.com")
                .password("dimasockol")
                .confirmedPassword("dimasockol")
                .build();
        final HttpEntity<RegisteredUserRequest> givenHttpEntity = createHttpEntity(givenRequest);

        final String actual = restTemplate.postForObject(URL, givenHttpEntity, String.class);
        final String expected = """
                {
                   "httpStatus": "NOT_ACCEPTABLE",
                   "message": "email : Invalid email",
                   "dateTime": "2024-03-08 15-25-22"
                }""";
        JSONAssert.assertEquals(expected, actual, JSON_COMPARATOR_IGNORING_DATE_TIME);
    }

    @Test
    public void registrationShouldBeFailedBecauseOfPasswordIsNull()
            throws Exception {
        final RegisteredUserRequest givenRequest = RegisteredUserRequest.builder()
                .email("dimasockol@gmail.com")
                .confirmedPassword("dimasockol")
                .build();
        final HttpEntity<RegisteredUserRequest> givenHttpEntity = createHttpEntity(givenRequest);

        final String actual = restTemplate.postForObject(URL, givenHttpEntity, String.class);
        final String expected = """
                {
                   "httpStatus": "NOT_ACCEPTABLE",
                   "message": "password : Invalid password",
                   "dateTime": "2024-03-08 15-25-22"
                }""";
        JSONAssert.assertEquals(expected, actual, JSON_COMPARATOR_IGNORING_DATE_TIME);
    }

    @Test
    public void registrationShouldBeFailedBecauseOfPasswordIsNotValid()
            throws Exception {
        final RegisteredUserRequest givenRequest = RegisteredUserRequest.builder()
                .email("dimasockol@gmail.com")
                .password("11")
                .confirmedPassword("dimasockol")
                .build();
        final HttpEntity<RegisteredUserRequest> givenHttpEntity = createHttpEntity(givenRequest);

        final String actual = restTemplate.postForObject(URL, givenHttpEntity, String.class);
        final String expected = """
                {
                   "httpStatus": "NOT_ACCEPTABLE",
                   "message": "password : Invalid password",
                   "dateTime": "2024-03-08 15-25-22"
                }""";
        JSONAssert.assertEquals(expected, actual, JSON_COMPARATOR_IGNORING_DATE_TIME);
    }

    @Test
    public void registrationShouldBeFailedBecauseOfConfirmedPasswordIsNull()
            throws Exception {
        final RegisteredUserRequest givenRequest = RegisteredUserRequest.builder()
                .email("dimasockol@gmail.com")
                .password("dimasockol")
                .build();
        final HttpEntity<RegisteredUserRequest> givenHttpEntity = createHttpEntity(givenRequest);

        final String actual = restTemplate.postForObject(URL, givenHttpEntity, String.class);
        final String expected = """
                {
                   "httpStatus": "NOT_ACCEPTABLE",
                   "message": "confirmedPassword : Invalid password",
                   "dateTime": "2024-03-08 15-25-22"
                }""";
        JSONAssert.assertEquals(expected, actual, JSON_COMPARATOR_IGNORING_DATE_TIME);
    }

    @Test
    public void registrationShouldBeFailedBecauseOfConfirmedPasswordIsNotValid()
            throws Exception {
        final RegisteredUserRequest givenRequest = RegisteredUserRequest.builder()
                .email("dimasockol@gmail.com")
                .password("dimasockol")
                .confirmedPassword("11")
                .build();
        final HttpEntity<RegisteredUserRequest> givenHttpEntity = createHttpEntity(givenRequest);

        final String actual = restTemplate.postForObject(URL, givenHttpEntity, String.class);
        final String expected = """
                {
                   "httpStatus": "NOT_ACCEPTABLE",
                   "message": "confirmedPassword : Invalid password",
                   "dateTime": "2024-03-08 15-25-22"
                }""";
        JSONAssert.assertEquals(expected, actual, JSON_COMPARATOR_IGNORING_DATE_TIME);
    }
}
