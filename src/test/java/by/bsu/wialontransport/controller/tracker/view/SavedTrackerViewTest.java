package by.bsu.wialontransport.controller.tracker.view;

import by.bsu.wialontransport.base.AbstractSpringBootTest;
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

public final class SavedTrackerViewTest extends AbstractSpringBootTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private Validator validator;

    @Test
    public void viewShouldBeMappedToJson()
            throws Exception {
        final SavedTrackerView givenView = SavedTrackerView.builder()
                .imei("11112222333344445555")
                .password("password")
                .phoneNumber("447336934")
                .build();

        final String actual = objectMapper.writeValueAsString(givenView);
        final String expected = """
                {
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
                  "imei": "11112222333344445555",
                  "password": "password",
                  "phoneNumber": "447336934"
                }""";

        final SavedTrackerView actual = objectMapper.readValue(givenJson, SavedTrackerView.class);
        final SavedTrackerView expected = SavedTrackerView.builder()
                .imei("11112222333344445555")
                .password("password")
                .phoneNumber("447336934")
                .build();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void viewShouldBeValid() {
        final SavedTrackerView givenView = SavedTrackerView.builder()
                .imei("11112222333344445555")
                .password("password")
                .phoneNumber("447336934")
                .build();

        final Set<ConstraintViolation<SavedTrackerView>> constraintViolations = validator.validate(givenView);
        assertTrue(constraintViolations.isEmpty());
    }

    @Test
    public void viewShouldNotBeValidBecauseOfImeiIsNull() {
        final SavedTrackerView givenView = SavedTrackerView.builder()
                .password("password")
                .phoneNumber("447336934")
                .build();

        final Set<ConstraintViolation<SavedTrackerView>> constraintViolations = validator.validate(givenView);
        assertEquals(1, constraintViolations.size());
        assertEquals("Invalid imei", findFirstMessage(constraintViolations));
    }

    @Test
    public void viewShouldNotBeValidBecauseOfImeiIsNotValid() {
        final SavedTrackerView givenView = SavedTrackerView.builder()
                .imei("1111222233334444555")
                .password("password")
                .phoneNumber("447336934")
                .build();

        final Set<ConstraintViolation<SavedTrackerView>> constraintViolations = validator.validate(givenView);
        assertEquals(1, constraintViolations.size());
        assertEquals("Invalid imei", findFirstMessage(constraintViolations));
    }

    @Test
    public void viewShouldNotBeValidBecauseOfPasswordIsNull() {
        final SavedTrackerView givenView = SavedTrackerView.builder()
                .imei("11112222333344445555")
                .phoneNumber("447336934")
                .build();

        final Set<ConstraintViolation<SavedTrackerView>> constraintViolations = validator.validate(givenView);
        assertEquals(1, constraintViolations.size());
        assertEquals("Invalid password", findFirstMessage(constraintViolations));
    }

    @Test
    public void viewShouldNotBeValidBecauseOfPasswordIsNotValid() {
        final SavedTrackerView givenView = SavedTrackerView.builder()
                .imei("11112222333344445555")
                .password("pa")
                .phoneNumber("447336934")
                .build();

        final Set<ConstraintViolation<SavedTrackerView>> constraintViolations = validator.validate(givenView);
        assertEquals(1, constraintViolations.size());
        assertEquals("Invalid password", findFirstMessage(constraintViolations));
    }

    @Test
    public void viewShouldNotBeValidBecauseOfPhoneNumberIsNull() {
        final SavedTrackerView givenView = SavedTrackerView.builder()
                .imei("11112222333344445555")
                .password("password")
                .build();

        final Set<ConstraintViolation<SavedTrackerView>> constraintViolations = validator.validate(givenView);
        assertEquals(1, constraintViolations.size());
        assertEquals("Invalid phone number", findFirstMessage(constraintViolations));
    }

    @Test
    public void viewShouldNotBeValidBecauseOfPhoneNumberIsNotValid() {
        final SavedTrackerView givenView = SavedTrackerView.builder()
                .imei("11112222333344445555")
                .password("password")
                .phoneNumber("44733693")
                .build();

        final Set<ConstraintViolation<SavedTrackerView>> constraintViolations = validator.validate(givenView);
        assertEquals(1, constraintViolations.size());
        assertEquals("Invalid phone number", findFirstMessage(constraintViolations));
    }
}
