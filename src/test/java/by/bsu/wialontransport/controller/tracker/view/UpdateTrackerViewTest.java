package by.bsu.wialontransport.controller.tracker.view;

import by.bsu.wialontransport.base.AbstractSpringBootTest;
import by.bsu.wialontransport.crud.dto.Tracker;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Assert;
import org.junit.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.util.Set;

import static by.bsu.wialontransport.util.ConstraintViolationUtil.findFirstMessage;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public final class UpdateTrackerViewTest extends AbstractSpringBootTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private Validator validator;

    @Test
    public void viewShouldBeMappedToJson()
            throws Exception {
        final UpdateTrackerView givenView = UpdateTrackerView.builder()
                .id(255L)
                .imei("11112222333344445555")
                .password("password")
                .phoneNumber("447336934")
                .build();

        final String actual = objectMapper.writeValueAsString(givenView);
        final String expected = """
                {
                   "id": 255,
                   "imei": "11112222333344445555",
                   "password": "password",
                   "phoneNumber": "447336934"
                }""";
        JSONAssert.assertEquals(expected, actual, true);
    }

    @Test
    public void jsonShouldBeMappedToView()
            throws Exception {
        final String givenJson = """
                {
                   "id": 255,
                   "imei": "11112222333344445555",
                   "password": "password",
                   "phoneNumber": "447336934"
                }""";

        final UpdateTrackerView actual = objectMapper.readValue(givenJson, UpdateTrackerView.class);
        final UpdateTrackerView expected = UpdateTrackerView.builder()
                .id(255L)
                .imei("11112222333344445555")
                .password("password")
                .phoneNumber("447336934")
                .build();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void viewShouldBeValid() {
        final UpdateTrackerView givenView = UpdateTrackerView.builder()
                .id(255L)
                .imei("11112222333344445555")
                .password("password")
                .phoneNumber("447336934")
                .build();

        final Set<ConstraintViolation<UpdateTrackerView>> constraintViolations = validator.validate(givenView);
        assertTrue(constraintViolations.isEmpty());
    }

    @Test
    public void viewShouldNotBeValidBecauseOfIdIsNull() {
        final UpdateTrackerView givenView = UpdateTrackerView.builder()
                .imei("11112222333344445555")
                .password("password")
                .phoneNumber("447336934")
                .build();

        final Set<ConstraintViolation<UpdateTrackerView>> constraintViolations = validator.validate(givenView);
        assertEquals(1, constraintViolations.size());
        assertEquals("must not be null", findFirstMessage(constraintViolations));
    }

    @Test
    public void viewShouldNotBeValidBecauseOfImeiIsNull() {
        final UpdateTrackerView givenView = UpdateTrackerView.builder()
                .id(255L)
                .password("password")
                .phoneNumber("447336934")
                .build();

        final Set<ConstraintViolation<UpdateTrackerView>> constraintViolations = validator.validate(givenView);
        assertEquals(1, constraintViolations.size());
        assertEquals("Invalid imei", findFirstMessage(constraintViolations));
    }

    @Test
    public void viewShouldNotBeValidBecauseOfImeiIsNotValid() {
        final UpdateTrackerView givenView = UpdateTrackerView.builder()
                .id(255L)
                .imei("1111222233334444555")
                .password("password")
                .phoneNumber("447336934")
                .build();

        final Set<ConstraintViolation<UpdateTrackerView>> constraintViolations = validator.validate(givenView);
        assertEquals(1, constraintViolations.size());
        assertEquals("Invalid imei", findFirstMessage(constraintViolations));
    }

    @Test
    public void viewShouldNotBeValidBecauseOfPasswordIsNull() {
        final UpdateTrackerView givenView = UpdateTrackerView.builder()
                .id(255L)
                .imei("11112222333344445555")
                .phoneNumber("447336934")
                .build();

        final Set<ConstraintViolation<UpdateTrackerView>> constraintViolations = validator.validate(givenView);
        assertEquals(1, constraintViolations.size());
        assertEquals("Invalid password", findFirstMessage(constraintViolations));
    }

    @Test
    public void viewShouldNotBeValidBecauseOfPasswordIsNotValid() {
        final UpdateTrackerView givenView = UpdateTrackerView.builder()
                .id(255L)
                .imei("11112222333344445555")
                .password("pa")
                .phoneNumber("447336934")
                .build();

        final Set<ConstraintViolation<UpdateTrackerView>> constraintViolations = validator.validate(givenView);
        assertEquals(1, constraintViolations.size());
        assertEquals("Invalid password", findFirstMessage(constraintViolations));
    }

    @Test
    public void viewShouldNotBeValidBecauseOfPhoneNumberIsNull() {
        final UpdateTrackerView givenView = UpdateTrackerView.builder()
                .id(255L)
                .imei("11112222333344445555")
                .password("password")
                .build();

        final Set<ConstraintViolation<UpdateTrackerView>> constraintViolations = validator.validate(givenView);
        assertEquals(1, constraintViolations.size());
        assertEquals("Invalid phone number", findFirstMessage(constraintViolations));
    }

    @Test
    public void viewShouldNotBeValidBecauseOfPhoneNumberIsNotValid() {
        final UpdateTrackerView givenView = UpdateTrackerView.builder()
                .id(255L)
                .imei("11112222333344445555")
                .password("password")
                .phoneNumber("44733693")
                .build();

        final Set<ConstraintViolation<UpdateTrackerView>> constraintViolations = validator.validate(givenView);
        assertEquals(1, constraintViolations.size());
        assertEquals("Invalid phone number", findFirstMessage(constraintViolations));
    }

    @Test
    public void dtoShouldBeCreated() {
        final Long givenId = 255L;
        final String givenImei = "11112222333344445555";
        final String givenPassword = "password";
        final String givenPhoneNumber = "44733693";
        final UpdateTrackerView givenView = UpdateTrackerView.builder()
                .id(givenId)
                .imei(givenImei)
                .password(givenPassword)
                .phoneNumber(givenPhoneNumber)
                .build();

        final Tracker actual = givenView.createDto();
        final Tracker expected = Tracker.builder()
                .id(givenId)
                .imei(givenImei)
                .password(givenPassword)
                .phoneNumber(givenPhoneNumber)
                .build();
        assertEquals(expected, actual);
    }
}