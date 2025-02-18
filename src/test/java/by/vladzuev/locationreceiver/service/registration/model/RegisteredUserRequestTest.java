//package by.vladzuev.locationreceiver.service.registration.model;
//
//import by.vladzuev.locationreceiver.base.AbstractSpringBootTest;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import org.junit.Test;
//import org.skyscreamer.jsonassert.JSONAssert;
//import org.springframework.beans.factory.annotation.Autowired;
//
//import javax.validation.ConstraintViolation;
//import javax.validation.Validator;
//import java.util.Set;
//
//import static org.junit.Assert.assertEquals;
//import static org.junit.Assert.assertTrue;
//
//public final class RegisteredUserRequestTest extends AbstractSpringBootTest {
//
//    @Autowired
//    private Validator validator;
//
//    @Autowired
//    private ObjectMapper objectMapper;
//
//    @Test
//    public void registeredUserShouldBeValid() {
//        final RegisteredUserRequest givenUser = RegisteredUserRequest.builder()
//                .email("vladzuev.00@mail.ru")
//                .password("password")
//                .confirmedPassword("password")
//                .build();
//
//        final Set<ConstraintViolation<RegisteredUserRequest>> violations = validator.validate(givenUser);
//        assertTrue(violations.isEmpty());
//    }
//
//    @Test
//    public void registeredUserShouldNotBeValidBecauseOfEmailIsNull() {
//        final RegisteredUserRequest givenUser = RegisteredUserRequest.builder()
//                .password("password")
//                .confirmedPassword("password")
//                .build();
//
//        final Set<ConstraintViolation<RegisteredUserRequest>> violations = validator.validate(givenUser);
//        assertEquals(1, violations.size());
//        assertEquals("Invalid email", findFirstMessage(violations));
//    }
//
//    @Test
//    public void registeredUserShouldNotBeValidBecauseOfEmailIsNotValid() {
//        final RegisteredUserRequest givenUser = RegisteredUserRequest.builder()
//                .email("vladzuev.00mail.ru")
//                .password("password")
//                .confirmedPassword("password")
//                .build();
//
//        final Set<ConstraintViolation<RegisteredUserRequest>> violations = validator.validate(givenUser);
//        assertEquals(1, violations.size());
//        assertEquals("Invalid email", findFirstMessage(violations));
//    }
//
//    @Test
//    public void registeredUserShouldNotBeValidBecauseOfPasswordIsNull() {
//        final RegisteredUserRequest givenUser = RegisteredUserRequest.builder()
//                .email("vladzuev.00@mail.ru")
//                .confirmedPassword("password")
//                .build();
//
//        final Set<ConstraintViolation<RegisteredUserRequest>> violations = validator.validate(givenUser);
//        assertEquals(1, violations.size());
//        assertEquals("Invalid password", findFirstMessage(violations));
//    }
//
//    @Test
//    public void registeredUserShouldNotBeValidBecauseOfPasswordIsNotValid() {
//        final RegisteredUserRequest givenUser = RegisteredUserRequest.builder()
//                .email("vladzuev.00@mail.ru")
//                .password("11")
//                .confirmedPassword("password")
//                .build();
//
//        final Set<ConstraintViolation<RegisteredUserRequest>> violations = validator.validate(givenUser);
//        assertEquals(1, violations.size());
//        assertEquals("Invalid password", findFirstMessage(violations));
//    }
//
//    @Test
//    public void registeredUserShouldNotBeValidBecauseOfConfirmedPasswordIsNull() {
//        final RegisteredUserRequest givenUser = RegisteredUserRequest.builder()
//                .email("vladzuev.00@mail.ru")
//                .password("password")
//                .build();
//
//        final Set<ConstraintViolation<RegisteredUserRequest>> violations = validator.validate(givenUser);
//        assertEquals(1, violations.size());
//        assertEquals("Invalid password", findFirstMessage(violations));
//    }
//
//    @Test
//    public void registeredUserShouldNotBeValidBecauseOfConfirmedPasswordIsNotValid() {
//        final RegisteredUserRequest givenUser = RegisteredUserRequest.builder()
//                .email("vladzuev.00@mail.ru")
//                .password("password")
//                .confirmedPassword("11")
//                .build();
//
//        final Set<ConstraintViolation<RegisteredUserRequest>> violations = validator.validate(givenUser);
//        assertEquals(1, violations.size());
//        assertEquals("Invalid password", findFirstMessage(violations));
//    }
//
//    @Test
//    public void registeredUserShouldBeMappedToJson()
//            throws Exception {
//        final RegisteredUserRequest givenUser = RegisteredUserRequest.builder()
//                .email("vladzuev.00@mail.ru")
//                .password("password")
//                .confirmedPassword("password")
//                .build();
//
//        final String actual = objectMapper.writeValueAsString(givenUser);
//        final String expected = """
//                {
//                  "email": "vladzuev.00@mail.ru",
//                  "password": "password",
//                  "confirmedPassword": "password"
//                }""";
//        JSONAssert.assertEquals(expected, actual, true);
//    }
//
//    @Test
//    public void jsonShouldBeMappedToRegisteredUser()
//            throws Exception {
//        final String givenJson = """
//                {
//                  "email": "vladzuev.00@mail.ru",
//                  "password": "password",
//                  "confirmedPassword": "password"
//                }""";
//
//        final RegisteredUserRequest actual = objectMapper.readValue(givenJson, RegisteredUserRequest.class);
//        final RegisteredUserRequest expected = RegisteredUserRequest.builder()
//                .email("vladzuev.00@mail.ru")
//                .password("password")
//                .confirmedPassword("password")
//                .build();
//        assertEquals(expected, actual);
//    }
//
//    private static String findFirstMessage(final Set<ConstraintViolation<RegisteredUserRequest>> violations) {
//        return violations.iterator().next().getMessage();
//    }
//}
