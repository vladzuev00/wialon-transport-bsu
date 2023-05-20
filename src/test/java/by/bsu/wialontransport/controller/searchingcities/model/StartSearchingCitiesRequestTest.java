package by.bsu.wialontransport.controller.searchingcities.model;

import by.bsu.wialontransport.base.AbstractContextTest;
import by.bsu.wialontransport.model.AreaCoordinate;
import by.bsu.wialontransport.model.Coordinate;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public final class StartSearchingCitiesRequestTest extends AbstractContextTest {

    @Autowired
    private Validator validator;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void requestShouldBeValid() {
        final AreaCoordinate givenAreaCoordinate = new AreaCoordinate(
                new Coordinate(1.1, 2.2),
                new Coordinate(4.4, 5.5)
        );
        final StartSearchingCitiesRequest givenRequest = new StartSearchingCitiesRequest(
                givenAreaCoordinate, 1.
        );

        final Set<ConstraintViolation<StartSearchingCitiesRequest>> constraintViolations = this.validator.validate(
                givenRequest
        );
        assertTrue(constraintViolations.isEmpty());
    }

    @Test
    public void requestShouldNotBeValidBecauseOfAreaCoordinateIsNull() {
        final StartSearchingCitiesRequest givenRequest = new StartSearchingCitiesRequest(
                null, 1.
        );

        final Set<ConstraintViolation<StartSearchingCitiesRequest>> constraintViolations = this.validator.validate(
                givenRequest
        );
        assertEquals(1, constraintViolations.size());
        assertEquals("must not be null", constraintViolations.iterator().next().getMessage());
    }

    @Test
    public void requestShouldNotBeValidBecauseOfAreaCoordinateIsNotValid() {
        final AreaCoordinate givenAreaCoordinate = new AreaCoordinate(
                new Coordinate(null, 2.2),
                new Coordinate(4.4, 5.5)
        );
        final StartSearchingCitiesRequest givenRequest = new StartSearchingCitiesRequest(
                givenAreaCoordinate, 1.
        );

        final Set<ConstraintViolation<StartSearchingCitiesRequest>> constraintViolations = this.validator.validate(
                givenRequest
        );
        assertEquals(1, constraintViolations.size());
        assertEquals("must not be null", constraintViolations.iterator().next().getMessage());
    }

    @Test
    public void requestShouldNotBeValidBecauseOfSearchStepIsNull() {
        final AreaCoordinate givenAreaCoordinate = new AreaCoordinate(
                new Coordinate(1.1, 2.2),
                new Coordinate(4.4, 5.5)
        );
        final StartSearchingCitiesRequest givenRequest = new StartSearchingCitiesRequest(
                givenAreaCoordinate, null
        );

        final Set<ConstraintViolation<StartSearchingCitiesRequest>> constraintViolations = this.validator.validate(
                givenRequest
        );
        assertEquals(1, constraintViolations.size());
        assertEquals("must not be null", constraintViolations.iterator().next().getMessage());
    }

    @Test
    public void requestShouldNotBeValidBecauseOfSearchStepIsLessThanMinimalAllowable() {
        final AreaCoordinate givenAreaCoordinate = new AreaCoordinate(
                new Coordinate(1.1, 2.2),
                new Coordinate(4.4, 5.5)
        );
        final StartSearchingCitiesRequest givenRequest = new StartSearchingCitiesRequest(
                givenAreaCoordinate, 0.009
        );

        final Set<ConstraintViolation<StartSearchingCitiesRequest>> constraintViolations = this.validator.validate(
                givenRequest
        );
        assertEquals(1, constraintViolations.size());
        assertEquals(
                "must be greater than or equal to 0.01",
                constraintViolations.iterator().next().getMessage()
        );
    }

    @Test
    public void requestShouldNotBeValidBecauseOfSearchStepIsMoreThanMaximalAllowable() {
        final AreaCoordinate givenAreaCoordinate = new AreaCoordinate(
                new Coordinate(1.1, 2.2),
                new Coordinate(4.4, 5.5)
        );
        final StartSearchingCitiesRequest givenRequest = new StartSearchingCitiesRequest(
                givenAreaCoordinate, 5.0001
        );

        final Set<ConstraintViolation<StartSearchingCitiesRequest>> constraintViolations = this.validator.validate(
                givenRequest
        );
        assertEquals(1, constraintViolations.size());
        assertEquals(
                "must be less than or equal to 5",
                constraintViolations.iterator().next().getMessage()
        );
    }

    @Test
    public void requestShouldBeMappedToJson()
            throws Exception {
        final AreaCoordinate givenAreaCoordinate = new AreaCoordinate(
                new Coordinate(1.1, 2.2),
                new Coordinate(4.4, 5.5)
        );
        final StartSearchingCitiesRequest givenRequest = new StartSearchingCitiesRequest(
                givenAreaCoordinate, 1.
        );

        final String actual = this.objectMapper.writeValueAsString(givenRequest);
        final String expected = "{\"areaCoordinate\":{\"leftBottom\":{\"latitude\":1.1,\"longitude\":2.2},"
                + "\"rightUpper\":{\"latitude\":4.4,\"longitude\":5.5}},\"searchStep\":1.0}";
        assertEquals(expected, actual);
    }

    @Test
    public void jsonShouldBeConvertedToRequest()
            throws Exception {
        final String givenJson = "{\"areaCoordinate\":{\"leftBottom\":{\"latitude\":1.1,\"longitude\":2.2},"
                + "\"rightUpper\":{\"latitude\":4.4,\"longitude\":5.5}},\"searchStep\":1.0}";

        final StartSearchingCitiesRequest actual = this.objectMapper.readValue(
                givenJson, StartSearchingCitiesRequest.class
        );
        final StartSearchingCitiesRequest expected = new StartSearchingCitiesRequest(
                new AreaCoordinate(
                        new Coordinate(1.1, 2.2),
                        new Coordinate(4.4, 5.5)
                ),
                1.
        );
        assertEquals(expected, actual);
    }

}
