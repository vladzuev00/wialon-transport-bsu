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

public final class SaveTrackerViewTest extends AbstractSpringBootTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private Validator validator;

    @Test
    public void viewShouldBeMappedToJson()
            throws Exception {
        final SaveTrackerView givenView = SaveTrackerView.builder()
                .imei("11112222333344445555")
                .phoneNumber("447336934")
                .password("password")
                .userId(255L)
                .build();

        final String actual = objectMapper.writeValueAsString(givenView);
        final String expected = """
                {
                   "imei": "11112222333344445555",
                   "phoneNumber": "447336934",
                   "password": "password",
                   "userId": 255
                }""";
        JSONAssert.assertEquals(expected, actual, true);
    }

    @Test
    public void jsonShouldBeMappedToView()
            throws Exception {
        final String givenJson = """
                {
                   "imei": "11112222333344445555",
                   "phoneNumber": "447336934",
                   "password": "password",
                   "userId": 255
                }""";

        final SaveTrackerView actual = objectMapper.readValue(givenJson, SaveTrackerView.class);
        final SaveTrackerView expected = SaveTrackerView.builder()
                .imei("11112222333344445555")
                .phoneNumber("447336934")
                .password("password")
                .userId(255L)
                .build();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void viewShouldBeValid() {
        final SaveTrackerView givenView = SaveTrackerView.builder()
                .imei("11112222333344445555")
                .phoneNumber("447336934")
                .password("password")
                .userId(255L)
                .build();

        final Set<ConstraintViolation<SaveTrackerView>> constraintViolations = validator.validate(givenView);
        assertTrue(constraintViolations.isEmpty());
    }

    @Test
    public void viewShouldNotBeValidBecauseOfPasswordIsNull() {
        final SaveTrackerView givenView = SaveTrackerView.builder()
                .imei("11112222333344445555")
                .phoneNumber("447336934")
                .userId(255L)
                .build();

        final Set<ConstraintViolation<SaveTrackerView>> constraintViolations = validator.validate(givenView);
        assertEquals(1, constraintViolations.size());
        assertEquals("Invalid password", findFirstMessage(constraintViolations));
    }

    @Test
    public void viewShouldNotBeValidBecauseOfPasswordIsNotValid() {
        final SaveTrackerView givenView = SaveTrackerView.builder()
                .imei("11112222333344445555")
                .phoneNumber("447336934")
                .password("pa")
                .userId(255L)
                .build();

        final Set<ConstraintViolation<SaveTrackerView>> constraintViolations = validator.validate(givenView);
        assertEquals(1, constraintViolations.size());
        assertEquals("Invalid password", findFirstMessage(constraintViolations));
    }

    @Test
    public void viewShouldNotBeValidBecauseOfUserIdIsNull() {
        final SaveTrackerView givenView = SaveTrackerView.builder()
                .imei("11112222333344445555")
                .phoneNumber("447336934")
                .password("password")
                .build();

        final Set<ConstraintViolation<SaveTrackerView>> constraintViolations = validator.validate(givenView);
        assertEquals(1, constraintViolations.size());
        assertEquals("must not be null", findFirstMessage(constraintViolations));
    }
}
