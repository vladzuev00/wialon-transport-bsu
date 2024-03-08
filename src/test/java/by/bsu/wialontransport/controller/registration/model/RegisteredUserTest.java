package by.bsu.wialontransport.controller.registration.model;

import by.bsu.wialontransport.base.AbstractSpringBootTest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Assert;
import org.junit.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public final class RegisteredUserTest extends AbstractSpringBootTest {

    @Autowired
    private Validator validator;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void registeredUserShouldBeValid() {
        final RegisteredUser givenUser = RegisteredUser.builder()
                .email("vladzuev.00@mail.ru")
                .password("password")
                .confirmedPassword("password")
                .build();

        final Set<ConstraintViolation<RegisteredUser>> violations = validator.validate(givenUser);
        assertTrue(violations.isEmpty());
    }

    @Test
    public void registeredUserShouldNotBeValidBecauseOfEmailIsNull() {
        final RegisteredUser givenUser = RegisteredUser.builder()
                .password("password")
                .confirmedPassword("password")
                .build();

        final Set<ConstraintViolation<RegisteredUser>> violations = validator.validate(givenUser);
        assertEquals(1, violations.size());
        assertEquals("Invalid email", findFirstMessage(violations));
    }

    @Test
    public void registeredUserShouldNotBeValidBecauseOfEmailIsNotValid() {
        final RegisteredUser givenUser = RegisteredUser.builder()
                .email("vladzuev.00mail.ru")
                .password("password")
                .confirmedPassword("password")
                .build();

        final Set<ConstraintViolation<RegisteredUser>> violations = validator.validate(givenUser);
        assertEquals(1, violations.size());
        assertEquals("Invalid email", findFirstMessage(violations));
    }

    @Test
    public void registeredUserShouldNotBeValidBecauseOfPasswordIsNull() {
        final RegisteredUser givenUser = RegisteredUser.builder()
                .email("vladzuev.00@mail.ru")
                .confirmedPassword("password")
                .build();

        final Set<ConstraintViolation<RegisteredUser>> violations = validator.validate(givenUser);
        assertEquals(1, violations.size());
        assertEquals("Invalid password", findFirstMessage(violations));
    }

    @Test
    public void registeredUserShouldNotBeValidBecauseOfPasswordIsNotValid() {
        final RegisteredUser givenUser = RegisteredUser.builder()
                .email("vladzuev.00@mail.ru")
                .password("11")
                .confirmedPassword("password")
                .build();

        final Set<ConstraintViolation<RegisteredUser>> violations = validator.validate(givenUser);
        assertEquals(1, violations.size());
        assertEquals("Invalid password", findFirstMessage(violations));
    }

    @Test
    public void registeredUserShouldNotBeValidBecauseOfConfirmedPasswordIsNull() {
        final RegisteredUser givenUser = RegisteredUser.builder()
                .email("vladzuev.00@mail.ru")
                .password("password")
                .build();

        final Set<ConstraintViolation<RegisteredUser>> violations = validator.validate(givenUser);
        assertEquals(1, violations.size());
        assertEquals("Invalid password", findFirstMessage(violations));
    }

    @Test
    public void registeredUserShouldNotBeValidBecauseOfConfirmedPasswordIsNotValid() {
        final RegisteredUser givenUser = RegisteredUser.builder()
                .email("vladzuev.00@mail.ru")
                .password("password")
                .confirmedPassword("11")
                .build();

        final Set<ConstraintViolation<RegisteredUser>> violations = validator.validate(givenUser);
        assertEquals(1, violations.size());
        assertEquals("Invalid password", findFirstMessage(violations));
    }

    @Test
    public void registeredUserShouldBeMappedToJson()
            throws Exception {
        final RegisteredUser givenUser = RegisteredUser.builder()
                .email("vladzuev.00@mail.ru")
                .password("password")
                .confirmedPassword("password")
                .build();

        final String actual = objectMapper.writeValueAsString(givenUser);
        final String expected = """
                {
                  "email": "vladzuev.00@mail.ru",
                  "password": "password",
                  "confirmedPassword": "password"
                }""";
        JSONAssert.assertEquals(expected, actual, true);
    }

    @Test
    public void jsonShouldBeMappedToRegisteredUser()
            throws Exception {
        final String givenJson = """
                {
                  "email": "vladzuev.00@mail.ru",
                  "password": "password",
                  "confirmedPassword": "password"
                }""";

        final RegisteredUser actual = objectMapper.readValue(givenJson, RegisteredUser.class);
        final RegisteredUser expected = RegisteredUser.builder()
                .email("vladzuev.00@mail.ru")
                .password("password")
                .confirmedPassword("password")
                .build();
        Assert.assertEquals(expected, actual);
    }

    private static String findFirstMessage(final Set<ConstraintViolation<RegisteredUser>> violations) {
        return violations.iterator().next().getMessage();
    }
}
