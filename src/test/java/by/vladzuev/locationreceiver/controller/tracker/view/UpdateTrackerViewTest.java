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
//import static org.junit.Assert.*;
//
//public final class UpdateTrackerViewTest extends AbstractSpringBootTest {
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
//        final UpdateTrackerView givenView = UpdateTrackerView.builder()
//                .id(255L)
//                .imei("99994444333366665456")
//                .phoneNumber("448664385")
//                .password("password")
//                .build();
//
//        final String actual = objectMapper.writeValueAsString(givenView);
//        final String expected = """
//                {
//                   "imei": "99994444333366665456",
//                   "phoneNumber": "448664385",
//                   "id": 255,
//                   "password": "password"
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
//                   "id": 255,
//                   "password": "password"
//                }""";
//
//        final UpdateTrackerView actual = objectMapper.readValue(givenJson, UpdateTrackerView.class);
//        final UpdateTrackerView expected = UpdateTrackerView.builder()
//                .imei("99994444333366665456")
//                .phoneNumber("448664385")
//                .id(255L)
//                .password("password")
//                .build();
//        assertEquals(expected, actual);
//    }
//
//    @Test
//    public void viewShouldBeValid() {
//        final UpdateTrackerView givenView = UpdateTrackerView.builder()
//                .imei("99994444333366665456")
//                .phoneNumber("448664385")
//                .id(255L)
//                .password("password")
//                .build();
//
//        final Set<ConstraintViolation<UpdateTrackerView>> constraintViolations = validator.validate(givenView);
//        assertTrue(constraintViolations.isEmpty());
//    }
//
//    @Test
//    public void viewShouldNotBeValidBecauseOfIdIsNull() {
//        final UpdateTrackerView givenView = UpdateTrackerView.builder()
//                .imei("99994444333366665456")
//                .phoneNumber("448664385")
//                .password("password")
//                .build();
//
//        final Set<ConstraintViolation<UpdateTrackerView>> constraintViolations = validator.validate(givenView);
//        assertEquals(1, constraintViolations.size());
//        Assert.assertEquals("Tracker with given id doesn't exist", ConstraintViolationUtil.findFirstMessage(constraintViolations));
//    }
//
//    @Test
//    public void viewShouldNotBeValidBecauseOfIdNotExist() {
//        final UpdateTrackerView givenView = UpdateTrackerView.builder()
//                .imei("99994444333366665456")
//                .phoneNumber("448664385")
//                .id(MIN_VALUE)
//                .password("password")
//                .build();
//
//        final Set<ConstraintViolation<UpdateTrackerView>> constraintViolations = validator.validate(givenView);
//        assertEquals(1, constraintViolations.size());
//        Assert.assertEquals("Tracker with given id doesn't exist", ConstraintViolationUtil.findFirstMessage(constraintViolations));
//    }
//
//    @Test
//    public void viewShouldNotBeValidBecauseOfPasswordIsNull() {
//        final UpdateTrackerView givenView = UpdateTrackerView.builder()
//                .imei("11112222333344445555")
//                .phoneNumber("447336934")
//                .id(255L)
//                .build();
//
//        final Set<ConstraintViolation<UpdateTrackerView>> constraintViolations = validator.validate(givenView);
//        assertEquals(1, constraintViolations.size());
//        Assert.assertEquals("Invalid password", ConstraintViolationUtil.findFirstMessage(constraintViolations));
//    }
//
//    @Test
//    public void viewShouldNotBeValidBecauseOfPasswordIsNotValid() {
//        final UpdateTrackerView givenView = UpdateTrackerView.builder()
//                .imei("11112222333344445555")
//                .phoneNumber("447336934")
//                .id(255L)
//                .password("pa")
//                .build();
//
//        final Set<ConstraintViolation<UpdateTrackerView>> constraintViolations = validator.validate(givenView);
//        assertEquals(1, constraintViolations.size());
//        Assert.assertEquals("Invalid password", ConstraintViolationUtil.findFirstMessage(constraintViolations));
//    }
//
//    @Test
//    public void idShouldBeFound() {
//        final Long givenId = 255L;
//        final UpdateTrackerView givenView = UpdateTrackerView.builder()
//                .id(givenId)
//                .build();
//
//        final Optional<Long> optionalActual = givenView.findId();
//        assertTrue(optionalActual.isPresent());
//        final Long actual = optionalActual.get();
//        assertSame(givenId, actual);
//    }
//}
