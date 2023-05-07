package by.bsu.wialontransport.model;

import by.bsu.wialontransport.base.AbstractContextTest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public final class AreaCoordinateTest extends AbstractContextTest {

    @Autowired
    private Validator validator;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void areaCoordinateShouldBeValid() {
        final AreaCoordinate givenAreaCoordinate = new AreaCoordinate(
                new Coordinate(45., 46.),
                new Coordinate(47., 48.)
        );

        final Set<ConstraintViolation<AreaCoordinate>> constraintViolations = this.validator.validate(
                givenAreaCoordinate
        );
        assertTrue(constraintViolations.isEmpty());
    }

    @Test
    public void areaCoordinateShouldNotBeValidBecauseOfLeftBottomIsNull() {
        final AreaCoordinate givenAreaCoordinate = new AreaCoordinate(
                null,
                new Coordinate(47., 48.)
        );

        final Set<ConstraintViolation<AreaCoordinate>> constraintViolations = this.validator.validate(
                givenAreaCoordinate
        );
        assertEquals(1, constraintViolations.size());
        assertEquals("must not be null", constraintViolations.iterator().next().getMessage());
    }

    @Test
    public void areaCoordinateShouldNotBeValidBecauseOfLeftBottomLatitudeIsNull() {
        final AreaCoordinate givenAreaCoordinate = new AreaCoordinate(
                new Coordinate(null, 46.),
                new Coordinate(47., 48.)
        );

        final Set<ConstraintViolation<AreaCoordinate>> constraintViolations = this.validator.validate(
                givenAreaCoordinate
        );
        assertEquals(1, constraintViolations.size());
        assertEquals("must not be null", constraintViolations.iterator().next().getMessage());
    }

    @Test
    public void areaCoordinateShouldNotBeValidBecauseOfLeftBottomLatitudeIsLessThanMinimalAllowable() {
        final AreaCoordinate givenAreaCoordinate = new AreaCoordinate(
                new Coordinate(-90.1, 46.),
                new Coordinate(47., 48.)
        );

        final Set<ConstraintViolation<AreaCoordinate>> constraintViolations = this.validator.validate(
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
        final AreaCoordinate givenAreaCoordinate = new AreaCoordinate(
                new Coordinate(90.1, 46.),
                new Coordinate(47., 48.)
        );

        final Set<ConstraintViolation<AreaCoordinate>> constraintViolations = this.validator.validate(
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
        final AreaCoordinate givenAreaCoordinate = new AreaCoordinate(
                new Coordinate(45., null),
                new Coordinate(47., 48.)
        );

        final Set<ConstraintViolation<AreaCoordinate>> constraintViolations = this.validator.validate(
                givenAreaCoordinate
        );
        assertEquals(1, constraintViolations.size());
        assertEquals("must not be null", constraintViolations.iterator().next().getMessage());
    }

    @Test
    public void areaCoordinateShouldNotBeValidBecauseOfLeftBottomLongitudeIsLessThanMinimalAllowable() {
        final AreaCoordinate givenAreaCoordinate = new AreaCoordinate(
                new Coordinate(45., -180.1),
                new Coordinate(47., 48.)
        );

        final Set<ConstraintViolation<AreaCoordinate>> constraintViolations = this.validator.validate(
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
        final AreaCoordinate givenAreaCoordinate = new AreaCoordinate(
                new Coordinate(45., 180.1),
                new Coordinate(47., 48.)
        );

        final Set<ConstraintViolation<AreaCoordinate>> constraintViolations = this.validator.validate(
                givenAreaCoordinate
        );
        assertEquals(1, constraintViolations.size());
        assertEquals("must be less than or equal to 180", constraintViolations.iterator().next().getMessage());
    }

    @Test
    public void areaCoordinateShouldNotBeValidBecauseOfRightUpperIsNull() {
        final AreaCoordinate givenAreaCoordinate = new AreaCoordinate(
                new Coordinate(45., 46.),
                null
        );

        final Set<ConstraintViolation<AreaCoordinate>> constraintViolations = this.validator.validate(
                givenAreaCoordinate
        );
        assertEquals(1, constraintViolations.size());
        assertEquals("must not be null", constraintViolations.iterator().next().getMessage());
    }

    @Test
    public void areaCoordinateShouldNotBeValidBecauseOfRightUpperLatitudeIsNull() {
        final AreaCoordinate givenAreaCoordinate = new AreaCoordinate(
                new Coordinate(45., 46.),
                new Coordinate(null, 48.)
        );

        final Set<ConstraintViolation<AreaCoordinate>> constraintViolations = this.validator.validate(
                givenAreaCoordinate
        );
        assertEquals(1, constraintViolations.size());
        assertEquals("must not be null", constraintViolations.iterator().next().getMessage());
    }

    @Test
    public void areaCoordinateShouldNotBeValidBecauseOfRightUpperLatitudeIsLessThanMinimalAllowable() {
        final AreaCoordinate givenAreaCoordinate = new AreaCoordinate(
                new Coordinate(45., 46.),
                new Coordinate(-90.1, 48.)
        );

        final Set<ConstraintViolation<AreaCoordinate>> constraintViolations = this.validator.validate(
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
        final AreaCoordinate givenAreaCoordinate = new AreaCoordinate(
                new Coordinate(45., 46.),
                new Coordinate(90.1, 48.)
        );

        final Set<ConstraintViolation<AreaCoordinate>> constraintViolations = this.validator.validate(
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
        final AreaCoordinate givenAreaCoordinate = new AreaCoordinate(
                new Coordinate(45., 46.),
                new Coordinate(47., null)
        );

        final Set<ConstraintViolation<AreaCoordinate>> constraintViolations = this.validator.validate(
                givenAreaCoordinate
        );
        assertEquals(1, constraintViolations.size());
        assertEquals("must not be null", constraintViolations.iterator().next().getMessage());
    }

    @Test
    public void areaCoordinateShouldNotBeValidBecauseOfRightUpperLongitudeIsLessThanMinimalAllowable() {
        final AreaCoordinate givenAreaCoordinate = new AreaCoordinate(
                new Coordinate(45., 46.),
                new Coordinate(47., -180.1)
        );

        final Set<ConstraintViolation<AreaCoordinate>> constraintViolations = this.validator.validate(
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
        final AreaCoordinate givenAreaCoordinate = new AreaCoordinate(
                new Coordinate(45., 46.),
                new Coordinate(47., 180.1)
        );

        final Set<ConstraintViolation<AreaCoordinate>> constraintViolations = this.validator.validate(
                givenAreaCoordinate
        );
        assertEquals(1, constraintViolations.size());
        assertEquals("must be less than or equal to 180", constraintViolations.iterator().next().getMessage());
    }

    @Test
    public void areaCoordinateShouldBeConvertedToJson()
            throws Exception {
        final AreaCoordinate givenAreaCoordinate = new AreaCoordinate(
                new Coordinate(45., 46.),
                new Coordinate(47., 48.1)
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

        final AreaCoordinate actual = this.objectMapper.readValue(givenJson, AreaCoordinate.class);
        final AreaCoordinate expected = new AreaCoordinate(
                new Coordinate(45., 46.),
                new Coordinate(47., 48.1)
        );
        assertEquals(expected, actual);
    }
}
