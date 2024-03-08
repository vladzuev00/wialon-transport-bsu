package by.bsu.wialontransport.controller.registration;

import by.bsu.wialontransport.base.AbstractSpringBootTest;
import by.bsu.wialontransport.service.registration.RegistrationService;
import by.bsu.wialontransport.service.registration.model.RegisteredUserRequest;
import by.bsu.wialontransport.service.registration.model.RegisteredUserResponse;
import org.junit.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;

import static by.bsu.wialontransport.crud.entity.UserEntity.Role.USER;
import static by.bsu.wialontransport.util.HttpUtil.JSON_COMPARATOR_IGNORING_DATE_TIME;
import static by.bsu.wialontransport.util.HttpUtil.createHttpEntity;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@SpringBootTest(webEnvironment = RANDOM_PORT)
public final class RegistrationControllerTest extends AbstractSpringBootTest {
    private static final String CONTROLLER_URL = "/registration";

    @MockBean
    private RegistrationService mockedRegistrationService;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void registrationShouldBeSuccess()
            throws Exception {
        final String givenEmail = "vladzuev00@mail.ru";
        final RegisteredUserRequest givenBody = RegisteredUserRequest.builder()
                .email(givenEmail)
                .password("password")
                .confirmedPassword("password")
                .build();
        final HttpEntity<RegisteredUserRequest> givenHttpEntity = createHttpEntity(givenBody);

        final RegisteredUserResponse givenResponse = RegisteredUserResponse.builder()
                .id(255L)
                .email(givenEmail)
                .role(USER)
                .build();
        when(mockedRegistrationService.checkIn(eq(givenBody))).thenReturn(givenResponse);

        final String actual = restTemplate.postForObject(CONTROLLER_URL, givenHttpEntity, String.class);
        final String expected = """
                {
                  "id": 255,
                  "email": "vladzuev00@mail.ru",
                  "role": "USER"
                }""";
        JSONAssert.assertEquals(expected, actual, JSON_COMPARATOR_IGNORING_DATE_TIME);
    }

    @Test
    public void registrationShouldBeFailedBecauseOfNotValidBody()
            throws Exception {
        final String givenEmail = "vladzuev00@mail.ru";
        final RegisteredUserRequest givenBody = RegisteredUserRequest.builder()
                .email(givenEmail)
                .password("password")
                .build();
        final HttpEntity<RegisteredUserRequest> givenHttpEntity = createHttpEntity(givenBody);

        final RegisteredUserResponse givenResponse = RegisteredUserResponse.builder()
                .id(255L)
                .email(givenEmail)
                .role(USER)
                .build();
        when(mockedRegistrationService.checkIn(eq(givenBody))).thenReturn(givenResponse);

        final String actual = restTemplate.postForObject(CONTROLLER_URL, givenHttpEntity, String.class);
        final String expected = """
                {
                   "httpStatus": "NOT_ACCEPTABLE",
                   "message": "confirmedPassword : Invalid password",
                   "dateTime": "2024-03-08 12-28-28"
                }""";
        JSONAssert.assertEquals(expected, actual, JSON_COMPARATOR_IGNORING_DATE_TIME);
    }
}
