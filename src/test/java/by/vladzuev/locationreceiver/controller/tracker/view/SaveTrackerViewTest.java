//package by.vladzuev.locationreceiver.controller.tracker.view;
//
//import by.vladzuev.locationreceiver.base.AbstractSpringBootTest;
//import by.vladzuev.locationreceiver.util.ConstraintViolationUtil;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import org.junit.Assert;
//import org.junit.Test;
//import org.skyscreamer.jsonassert.JSONAssert;
//import org.springframework.beans.factory.annotation.Autowired;
//
//import javax.validation.ConstraintViolation;
//import javax.validation.Validator;
//import java.util.Optional;
//import java.util.Set;
//
//import static java.lang.Long.MIN_VALUE;
//import static org.junit.Assert.assertEquals;
//import static org.junit.Assert.assertTrue;
//
//public final class SaveTrackerViewTest extends AbstractSpringBootTest {
//
//    @Autowired
//    private ObjectMapper objectMapper;
//
//    @Autowired
//    private Validator validator;
//
//    @Test
//    public void viewShouldBeMappedToJson()
//            throws Exception {
//        final SaveTrackerView givenView = SaveTrackerView.builder()
//                .imei("99994444333366665456")
//                .phoneNumber("448664385")
//                .password("password")
//                .userId(255L)
//                .build();
//
//        final String actual = objectMapper.writeValueAsString(givenView);
//        final String expected = """
//                {
//                   "imei": "99994444333366665456",
//                   "phoneNumber": "448664385",
//                   "password": "password",
//                   "userId": 255
//                }""";
//        JSONAssert.assertEquals(expected, actual, true);
//    }
//
//    @Test
//    public void jsonShouldBeMappedToView()
//            throws Exception {
//        final String givenJson = """
//                {
//                   "imei": "99994444333366665456",
//                   "phoneNumber": "448664385",
//                   "password": "password",
//                   "userId": 255
//                }""";
//
//        final SaveTrackerView actual = objectMapper.readValue(givenJson, SaveTrackerView.class);
//        final SaveTrackerView expected = SaveTrackerView.builder()
//                .imei("99994444333366665456")
//                .phoneNumber("448664385")
//                .password("password")
//                .userId(255L)
//                .build();
//        assertEquals(expected, actual);
//    }
//
//    @Test
//    public void viewShouldBeValid() {
//        final SaveTrackerView givenView = SaveTrackerView.builder()
//                .imei("99994444333366665456")
//                .phoneNumber("448664385")
//                .password("password")
//                .userId(255L)
//                .build();
//
//        final Set<ConstraintViolation<SaveTrackerView>> constraintViolations = validator.validate(givenView);
//        assertTrue(constraintViolations.isEmpty());
//    }
//
//    @Test
//    public void viewShouldNotBeValidBecauseOfPasswordIsNull() {
//        final SaveTrackerView givenView = SaveTrackerView.builder()
//                .imei("99994444333366665456")
//                .phoneNumber("448664385")
//                .userId(255L)
//                .build();
//
//        final Set<ConstraintViolation<SaveTrackerView>> constraintViolations = validator.validate(givenView);
//        assertEquals(1, constraintViolations.size());
//        Assert.assertEquals("Invalid password", ConstraintViolationUtil.findFirstMessage(constraintViolations));
//    }
//
//    @Test
//    public void viewShouldNotBeValidBecauseOfPasswordIsNotValid() {
//        final SaveTrackerView givenView = SaveTrackerView.builder()
//                .imei("99994444333366665456")
//                .phoneNumber("448664385")
//                .password("pa")
//                .userId(255L)
//                .build();
//
//        final Set<ConstraintViolation<SaveTrackerView>> constraintViolations = validator.validate(givenView);
//        assertEquals(1, constraintViolations.size());
//        Assert.assertEquals("Invalid password", ConstraintViolationUtil.findFirstMessage(constraintViolations));
//    }
//
//    @Test
//    public void viewShouldNotBeValidBecauseOfUserIdIsNull() {
//        final SaveTrackerView givenView = SaveTrackerView.builder()
//                .imei("99994444333366665456")
//                .phoneNumber("448664385")
//                .password("password")
//                .build();
//
//        final Set<ConstraintViolation<SaveTrackerView>> constraintViolations = validator.validate(givenView);
//        assertEquals(1, constraintViolations.size());
//        Assert.assertEquals("User with given id doesn't exist", ConstraintViolationUtil.findFirstMessage(constraintViolations));
//    }
//
//    @Test
//    public void viewShouldNotBeValidBecauseOfUserWithGivenIdNotExist() {
//        final SaveTrackerView givenView = SaveTrackerView.builder()
//                .imei("99994444333366665456")
//                .phoneNumber("448664385")
//                .password("password")
//                .userId(MIN_VALUE)
//                .build();
//
//        final Set<ConstraintViolation<SaveTrackerView>> constraintViolations = validator.validate(givenView);
//        assertEquals(1, constraintViolations.size());
//        Assert.assertEquals("User with given id doesn't exist", ConstraintViolationUtil.findFirstMessage(constraintViolations));
//    }
//
//    @Test
//    public void idShouldNotBeFound() {
//        final SaveTrackerView givenView = SaveTrackerView.builder().build();
//
//        final Optional<Long> optionalActual = givenView.findId();
//        assertTrue(optionalActual.isEmpty());
//    }
//}
