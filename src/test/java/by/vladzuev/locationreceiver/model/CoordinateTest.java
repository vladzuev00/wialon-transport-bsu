//package by.vladzuev.locationreceiver.model;
//
//import by.vladzuev.locationreceiver.base.AbstractSpringBootTest;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import org.junit.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//
//import javax.validation.ConstraintViolation;
//import javax.validation.Validator;
//import java.util.Set;
//
//import static org.junit.Assert.assertEquals;
//import static org.junit.Assert.assertTrue;
//
//public final class CoordinateTest extends AbstractSpringBootTest {
//
//    @Autowired
//    private Validator validator;
//
//    @Autowired
//    private ObjectMapper objectMapper;
//
//    @Test
//    public void coordinateShouldBeValid() {
//        final CoordinateRequest givenCoordinate = new CoordinateRequest(45., 150.);
//
//        final Set<ConstraintViolation<CoordinateRequest>> constraintViolations = this.validator.validate(givenCoordinate);
//        assertTrue(constraintViolations.isEmpty());
//    }
//
//    @Test
//    public void coordinateShouldNotBeValidBecauseOfLatitudeIsNull() {
//        final CoordinateRequest givenCoordinate = new CoordinateRequest(null, 150.);
//
//        final Set<ConstraintViolation<CoordinateRequest>> constraintViolations = this.validator.validate(givenCoordinate);
//        assertEquals(1, constraintViolations.size());
//        assertEquals("must not be null", constraintViolations.iterator().next().getMessage());
//    }
//
//    @Test
//    public void coordinateShouldNotBeValidBecauseOfLatitudeIsLessThanMinimalAllowable() {
//        final CoordinateRequest givenCoordinate = new CoordinateRequest(-90.1, 150.);
//
//        final Set<ConstraintViolation<CoordinateRequest>> constraintViolations = this.validator.validate(givenCoordinate);
//        assertEquals(1, constraintViolations.size());
//        assertEquals(
//                "must be greater than or equal to -90",
//                constraintViolations.iterator().next().getMessage()
//        );
//    }
//
//    @Test
//    public void coordinateShouldNotBeValidBecauseOfLatitudeIsMoreThanMaximalAllowable() {
//        final CoordinateRequest givenCoordinate = new CoordinateRequest(90.1, 150.);
//
//        final Set<ConstraintViolation<CoordinateRequest>> constraintViolations = this.validator.validate(givenCoordinate);
//        assertEquals(1, constraintViolations.size());
//        assertEquals(
//                "must be less than or equal to 90",
//                constraintViolations.iterator().next().getMessage()
//        );
//    }
//
//    @Test
//    public void coordinateShouldNotBeValidBecauseOfLongitudeIsNull() {
//        final CoordinateRequest givenCoordinate = new CoordinateRequest(45., null);
//
//        final Set<ConstraintViolation<CoordinateRequest>> constraintViolations = this.validator.validate(givenCoordinate);
//        assertEquals(1, constraintViolations.size());
//        assertEquals(
//                "must not be null",
//                constraintViolations.iterator().next().getMessage()
//        );
//    }
//
//    @Test
//    public void coordinateShouldNotBeValidBecauseOfLongitudeIsLessThanMinimalAllowable() {
//        final CoordinateRequest givenCoordinate = new CoordinateRequest(45., -180.1);
//
//        final Set<ConstraintViolation<CoordinateRequest>> constraintViolations = this.validator.validate(givenCoordinate);
//        assertEquals(1, constraintViolations.size());
//        assertEquals(
//                "must be greater than or equal to -180",
//                constraintViolations.iterator().next().getMessage()
//        );
//    }
//
//    @Test
//    public void coordinateShouldNotBeValidBecauseOfLongitudeIsMoreThanMaximalAllowable() {
//        final CoordinateRequest givenCoordinate = new CoordinateRequest(45., 180.1);
//
//        final Set<ConstraintViolation<CoordinateRequest>> constraintViolations = this.validator.validate(givenCoordinate);
//        assertEquals(1, constraintViolations.size());
//        assertEquals(
//                "must be less than or equal to 180",
//                constraintViolations.iterator().next().getMessage()
//        );
//    }
//
//    @Test
//    public void coordinateShouldBeConvertedToJson()
//            throws Exception {
//        final CoordinateRequest givenCoordinate = new CoordinateRequest(45., 90.);
//
//        final String actual = this.objectMapper.writeValueAsString(givenCoordinate);
//        final String expected = "{\"latitude\":45.0,\"longitude\":90.0}";
//        assertEquals(expected, actual);
//    }
//
//    @Test
//    public void jsonShouldBeConvertedToCoordinate()
//            throws Exception {
//        final String givenJson = "{\"latitude\":45.0,\"longitude\":90.0}";
//
//        final CoordinateRequest actual = this.objectMapper.readValue(givenJson, CoordinateRequest.class);
//        final CoordinateRequest expected = new CoordinateRequest(45., 90.);
//        assertEquals(expected, actual);
//    }
//}
