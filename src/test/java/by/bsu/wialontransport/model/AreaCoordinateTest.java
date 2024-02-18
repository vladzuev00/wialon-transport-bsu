package by.bsu.wialontransport.model;

import by.bsu.wialontransport.base.AbstractSpringBootTest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public final class AreaCoordinateTest extends AbstractSpringBootTest {

    @Autowired
    private Validator validator;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void areaCoordinateShouldBeValid() {
        final AreaCoordinateRequest givenAreaCoordinate = new AreaCoordinateRequest(
                new CoordinateRequest(45., 46.),
                new CoordinateRequest(47., 48.)
        );

        final Set<ConstraintViolation<AreaCoordinateRequest>> constraintViolations = this.validator.validate(
                givenAreaCoordinate
        );
        assertTrue(constraintViolations.isEmpty());
    }

    @Test
    public void areaCoordinateShouldNotBeValidBecauseOfLeftBottomIsNull() {
        final AreaCoordinateRequest givenAreaCoordinate = new AreaCoordinateRequest(
                null,
                new CoordinateRequest(47., 48.)
        );

        final Set<ConstraintViolation<AreaCoordinateRequest>> constraintViolations = this.validator.validate(
                givenAreaCoordinate
        );
        assertEquals(1, constraintViolations.size());
        assertEquals("must not be null", constraintViolations.iterator().next().getMessage());
    }

    @Test
    public void areaCoordinateShouldNotBeValidBecauseOfLeftBottomLatitudeIsNull() {
        final AreaCoordinateRequest givenAreaCoordinate = new AreaCoordinateRequest(
                new CoordinateRequest(null, 46.),
                new CoordinateRequest(47., 48.)
        );

        final Set<ConstraintViolation<AreaCoordinateRequest>> constraintViolations = this.validator.validate(
                givenAreaCoordinate
        );
        assertEquals(1, constraintViolations.size());
        assertEquals("must not be null", constraintViolations.iterator().next().getMessage());
    }

    @Test
    public void areaCoordinateShouldNotBeValidBecauseOfLeftBottomLatitudeIsLessThanMinimalAllowable() {
        final AreaCoordinateRequest givenAreaCoordinate = new AreaCoordinateRequest(
                new CoordinateRequest(-90.1, 46.),
                new CoordinateRequest(47., 48.)
        );

        final Set<ConstraintViolation<AreaCoordinateRequest>> constraintViolations = this.validator.validate(
                givenAreaCoordinate
        );
        assertEquals(1, constraintViolations.size());
        assertEquals(
                "must be greater than or equal to -90",
                constraintViolations.iterator().next().getMessage()
        );
    }

    @Test
    public void areaCoordinateShouldNotBeValidBecauseOfLeftBottomLatitudeIsMoreThanMaximalAllowable() {
        final AreaCoordinateRequest givenAreaCoordinate = new AreaCoordinateRequest(
                new CoordinateRequest(90.1, 46.),
                new CoordinateRequest(47., 48.)
        );

        final Set<ConstraintViolation<AreaCoordinateRequest>> constraintViolations = this.validator.validate(
                givenAreaCoordinate
        );
        assertEquals(1, constraintViolations.size());
        assertEquals(
                "must be less than or equal to 90",
                constraintViolations.iterator().next().getMessage()
        );
    }

    @Test
    public void areaCoordinateShouldNotBeValidBecauseOfLeftBottomLongitudeIsNull() {
        final AreaCoordinateRequest givenAreaCoordinate = new AreaCoordinateRequest(
                new CoordinateRequest(45., null),
                new CoordinateRequest(47., 48.)
        );

        final Set<ConstraintViolation<AreaCoordinateRequest>> constraintViolations = this.validator.validate(
                givenAreaCoordinate
        );
        assertEquals(1, constraintViolations.size());
        assertEquals("must not be null", constraintViolations.iterator().next().getMessage());
    }

    @Test
    public void areaCoordinateShouldNotBeValidBecauseOfLeftBottomLongitudeIsLessThanMinimalAllowable() {
        final AreaCoordinateRequest givenAreaCoordinate = new AreaCoordinateRequest(
                new CoordinateRequest(45., -180.1),
                new CoordinateRequest(47., 48.)
        );

        final Set<ConstraintViolation<AreaCoordinateRequest>> constraintViolations = this.validator.validate(
                givenAreaCoordinate
        );
        assertEquals(1, constraintViolations.size());
        assertEquals(
                "must be greater than or equal to -180",
                constraintViolations.iterator().next().getMessage()
        );
    }

    @Test
    public void areaCoordinateShouldNotBeValidBecauseOfLeftBottomLongitudeIsMoreThanMaximalAllowable() {
        final AreaCoordinateRequest givenAreaCoordinate = new AreaCoordinateRequest(
                new CoordinateRequest(45., 180.1),
                new CoordinateRequest(47., 48.)
        );

        final Set<ConstraintViolation<AreaCoordinateRequest>> constraintViolations = this.validator.validate(
                givenAreaCoordinate
        );
        assertEquals(1, constraintViolations.size());
        assertEquals("must be less than or equal to 180", constraintViolations.iterator().next().getMessage());
    }

    @Test
    public void areaCoordinateShouldNotBeValidBecauseOfRightUpperIsNull() {
        final AreaCoordinateRequest givenAreaCoordinate = new AreaCoordinateRequest(
                new CoordinateRequest(45., 46.),
                null
        );

        final Set<ConstraintViolation<AreaCoordinateRequest>> constraintViolations = this.validator.validate(
                givenAreaCoordinate
        );
        assertEquals(1, constraintViolations.size());
        assertEquals("must not be null", constraintViolations.iterator().next().getMessage());
    }

    @Test
    public void areaCoordinateShouldNotBeValidBecauseOfRightUpperLatitudeIsNull() {
        final AreaCoordinateRequest givenAreaCoordinate = new AreaCoordinateRequest(
                new CoordinateRequest(45., 46.),
                new CoordinateRequest(null, 48.)
        );

        final Set<ConstraintViolation<AreaCoordinateRequest>> constraintViolations = this.validator.validate(
                givenAreaCoordinate
        );
        assertEquals(1, constraintViolations.size());
        assertEquals("must not be null", constraintViolations.iterator().next().getMessage());
    }

    @Test
    public void areaCoordinateShouldNotBeValidBecauseOfRightUpperLatitudeIsLessThanMinimalAllowable() {
        final AreaCoordinateRequest givenAreaCoordinate = new AreaCoordinateRequest(
                new CoordinateRequest(45., 46.),
                new CoordinateRequest(-90.1, 48.)
        );

        final Set<ConstraintViolation<AreaCoordinateRequest>> constraintViolations = this.validator.validate(
                givenAreaCoordinate
        );
        assertEquals(1, constraintViolations.size());
        assertEquals(
                "must be greater than or equal to -90",
                constraintViolations.iterator().next().getMessage()
        );
    }

    @Test
    public void areaCoordinateShouldNotBeValidBecauseOfRightUpperLatitudeIsMoreThanMaximalAllowable() {
        final AreaCoordinateRequest givenAreaCoordinate = new AreaCoordinateRequest(
                new CoordinateRequest(45., 46.),
                new CoordinateRequest(90.1, 48.)
        );

        final Set<ConstraintViolation<AreaCoordinateRequest>> constraintViolations = this.validator.validate(
                givenAreaCoordinate
        );
        assertEquals(1, constraintViolations.size());
        assertEquals(
                "must be less than or equal to 90",
                constraintViolations.iterator().next().getMessage()
        );
    }

    @Test
    public void areaCoordinateShouldNotBeValidBecauseOfRightUpperLongitudeIsNull() {
        final AreaCoordinateRequest givenAreaCoordinate = new AreaCoordinateRequest(
                new CoordinateRequest(45., 46.),
                new CoordinateRequest(47., null)
        );

        final Set<ConstraintViolation<AreaCoordinateRequest>> constraintViolations = this.validator.validate(
                givenAreaCoordinate
        );
        assertEquals(1, constraintViolations.size());
        assertEquals("must not be null", constraintViolations.iterator().next().getMessage());
    }

    @Test
    public void areaCoordinateShouldNotBeValidBecauseOfRightUpperLongitudeIsLessThanMinimalAllowable() {
        final AreaCoordinateRequest givenAreaCoordinate = new AreaCoordinateRequest(
                new CoordinateRequest(45., 46.),
                new CoordinateRequest(47., -180.1)
        );

        final Set<ConstraintViolation<AreaCoordinateRequest>> constraintViolations = this.validator.validate(
                givenAreaCoordinate
        );
        assertEquals(1, constraintViolations.size());
        assertEquals(
                "must be greater than or equal to -180",
                constraintViolations.iterator().next().getMessage()
        );
    }

    @Test
    public void areaCoordinateShouldNotBeValidBecauseOfRightUpperLongitudeIsMoreThanMaximalAllowable() {
        final AreaCoordinateRequest givenAreaCoordinate = new AreaCoordinateRequest(
                new CoordinateRequest(45., 46.),
                new CoordinateRequest(47., 180.1)
        );

        final Set<ConstraintViolation<AreaCoordinateRequest>> constraintViolations = this.validator.validate(
                givenAreaCoordinate
        );
        assertEquals(1, constraintViolations.size());
        assertEquals("must be less than or equal to 180", constraintViolations.iterator().next().getMessage());
    }

    @Test
    public void areaCoordinateShouldBeConvertedToJson()
            throws Exception {
        final AreaCoordinateRequest givenAreaCoordinate = new AreaCoordinateRequest(
                new CoordinateRequest(45., 46.),
                new CoordinateRequest(47., 48.1)
        );

        final String actual = this.objectMapper.writeValueAsString(givenAreaCoordinate);
        final String expected = "{\"leftBottom\":{\"latitude\":45.0,\"longitude\":46.0},"
                + "\"rightUpper\":{\"latitude\":47.0,\"longitude\":48.1}}";
        assertEquals(expected, actual);
    }

    @Test
    public void jsonShouldBeConvertedToAreaCoordinate()
            throws Exception {
        final String givenJson = "{\"leftBottom\":{\"latitude\":45.0,\"longitude\":46.0},"
                + "\"rightUpper\":{\"latitude\":47.0,\"longitude\":48.1}}";

        final AreaCoordinateRequest actual = this.objectMapper.readValue(givenJson, AreaCoordinateRequest.class);
        final AreaCoordinateRequest expected = new AreaCoordinateRequest(
                new CoordinateRequest(45., 46.),
                new CoordinateRequest(47., 48.1)
        );
        assertEquals(expected, actual);
    }
}
