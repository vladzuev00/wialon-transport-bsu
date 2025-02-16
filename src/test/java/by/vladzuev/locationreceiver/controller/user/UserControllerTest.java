//package by.vladzuev.locationreceiver.controller.user;
//
//import by.vladzuev.locationreceiver.base.AbstractSpringBootTest;
//import by.vladzuev.locationreceiver.crud.dto.User;
//import by.vladzuev.locationreceiver.service.security.service.SecurityService;
//import by.vladzuev.locationreceiver.crud.entity.UserEntity;
//import org.junit.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
//import org.springframework.boot.test.web.client.TestRestTemplate;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//
//import static org.junit.Assert.assertSame;
//import static org.mockito.Mockito.when;
//import static org.skyscreamer.jsonassert.JSONAssert.assertEquals;
//import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
//import static org.springframework.http.HttpStatus.OK;
//
//@SpringBootTest(webEnvironment = RANDOM_PORT)
//public final class UserControllerTest extends AbstractSpringBootTest {
//    private static final String CONTROLLER_URL = "/user";
//    private static final String URL_TO_GET_AUTHORIZED_USER = CONTROLLER_URL + "/authorized";
//
//    @MockBean
//    private SecurityService mockedSecurityService;
//
//    @Autowired
//    private TestRestTemplate restTemplate;
//
//    @Test
//    public void authorizedUserShouldBeGot()
//            throws Exception {
//        final User givenUser = new User(255L, "vladzuev.00@mail.ru", "password", UserEntity.Role.USER);
//        when(mockedSecurityService.findLoggedOnUser()).thenReturn(givenUser);
//
//        final ResponseEntity<String> actual = restTemplate.getForEntity(URL_TO_GET_AUTHORIZED_USER, String.class);
//
//        final HttpStatus actualStatus = actual.getStatusCode();
//        assertSame(OK, actualStatus);
//
//        final String actualBody = actual.getBody();
//        final String expectedBody = """
//                {
//                  "id": 255,
//                  "email": "vladzuev.00@mail.ru",
//                  "role": "USER"
//                }""";
//        assertEquals(expectedBody, actualBody, true);
//    }
//}
