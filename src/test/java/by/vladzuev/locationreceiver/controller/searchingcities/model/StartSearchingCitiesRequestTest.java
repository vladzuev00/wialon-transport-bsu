package by.vladzuev.locationreceiver.controller.searchingcities.model;

import by.vladzuev.locationreceiver.base.AbstractSpringBootTest;
import by.vladzuev.locationreceiver.model.AreaCoordinateRequest;
import by.vladzuev.locationreceiver.model.CoordinateRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.skyscreamer.jsonassert.JSONAssert.assertEquals;

public final class StartSearchingCitiesRequestTest extends AbstractSpringBootTest {

    @Autowired
    private Validator validator;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void requestShouldBeValid() {
        final AreaCoordinateRequest givenAreaCoordinate = new AreaCoordinateRequest(
                new CoordinateRequest(1.1, 2.2),
                new CoordinateRequest(4.4, 5.5)
        );
        final StartSearchingCitiesRequest givenRequest = new StartSearchingCitiesRequest(
                givenAreaCoordinate,
                1.
        );

        final Set<ConstraintViolation<StartSearchingCitiesRequest>> constraintViolations = validator.validate(
                givenRequest
        );
        assertTrue(constraintViolations.isEmpty());
    }

    @Test
    public void requestShouldNotBeValidBecauseOfAreaCoordinateIsNull() {
        final StartSearchingCitiesRequest givenRequest = new StartSearchingCitiesRequest(null, 1.);

        final Set<ConstraintViolation<StartSearchingCitiesRequest>> constraintViolations = validator.validate(
                givenRequest
        );
        assertEquals(1, constraintViolations.size());
        assertEquals("must not be null", findFirstMessage(constraintViolations));
    }

    @Test
    public void requestShouldNotBeValidBecauseOfAreaCoordinateIsNotValid() {
        final AreaCoordinateRequest givenAreaCoordinate = new AreaCoordinateRequest(
                new CoordinateRequest(null, 2.2),
                new CoordinateRequest(4.4, 5.5)
        );
        final StartSearchingCitiesRequest givenRequest = new StartSearchingCitiesRequest(
                givenAreaCoordinate,
                1.
        );

        final Set<ConstraintViolation<StartSearchingCitiesRequest>> constraintViolations = validator.validate(
                givenRequest
        );
        assertEquals(1, constraintViolations.size());
        assertEquals("must not be null", findFirstMessage(constraintViolations));
    }

    @Test
    public void requestShouldNotBeValidBecauseOfSearchStepIsNull() {
        final AreaCoordinateRequest givenAreaCoordinate = new AreaCoordinateRequest(
                new CoordinateRequest(1.1, 2.2),
                new CoordinateRequest(4.4, 5.5)
        );
        final StartSearchingCitiesRequest givenRequest = new StartSearchingCitiesRequest(
                givenAreaCoordinate,
                null
        );

        final Set<ConstraintViolation<StartSearchingCitiesRequest>> constraintViolations = validator.validate(
                givenRequest
        );
        assertEquals(1, constraintViolations.size());
        assertEquals("must not be null", findFirstMessage(constraintViolations));
    }

    @Test
    public void requestShouldNotBeValidBecauseOfSearchStepIsLessThanMinimalAllowable() {
        final AreaCoordinateRequest givenAreaCoordinate = new AreaCoordinateRequest(
                new CoordinateRequest(1.1, 2.2),
                new CoordinateRequest(4.4, 5.5)
        );
        final StartSearchingCitiesRequest givenRequest = new StartSearchingCitiesRequest(
                givenAreaCoordinate,
                0.009
        );

        final Set<ConstraintViolation<StartSearchingCitiesRequest>> constraintViolations = validator.validate(
                givenRequest
        );
        assertEquals(1, constraintViolations.size());
        assertEquals("must be greater than or equal to 0.01", findFirstMessage(constraintViolations));
    }

    @Test
    public void requestShouldNotBeValidBecauseOfSearchStepIsMoreThanMaximalAllowable() {
        final AreaCoordinateRequest givenAreaCoordinate = new AreaCoordinateRequest(
                new CoordinateRequest(1.1, 2.2),
                new CoordinateRequest(4.4, 5.5)
        );
        final StartSearchingCitiesRequest givenRequest = new StartSearchingCitiesRequest(
                givenAreaCoordinate,
                5.0001
        );

        final Set<ConstraintViolation<StartSearchingCitiesRequest>> constraintViolations = validator.validate(
                givenRequest
        );
        assertEquals(1, constraintViolations.size());
        assertEquals("must be less than or equal to 5", findFirstMessage(constraintViolations));
    }

    @Test
    public void requestShouldBeMappedToJson()
            throws Exception {
        final AreaCoordinateRequest givenAreaCoordinate = new AreaCoordinateRequest(
                new CoordinateRequest(1.1, 2.2),
                new CoordinateRequest(4.4, 5.5)
        );
        final StartSearchingCitiesRequest givenRequest = new StartSearchingCitiesRequest(
                givenAreaCoordinate,
                1.
        );

        final String actual = objectMapper.writeValueAsString(givenRequest);
        final String expected = """
                {
                  "areaCoordinate": {
                    "leftBottom": {
                      "latitude": 1.1,
                      "longitude": 2.2
                    },
                    "rightUpper": {
                      "latitude": 4.4,
                      "longitude": 5.5
                    }
                  },
                  "searchStep": 1
                }""";
        assertEquals(expected, actual, true);
    }

    @Test
    public void jsonShouldBeConvertedToRequest()
            throws Exception {
        final String givenJson = """
                {
                  "areaCoordinate": {
                    "leftBottom": {
                      "latitude": 1.1,
                      "longitude": 2.2
                    },
                    "rightUpper": {
                      "latitude": 4.4,
                      "longitude": 5.5
                    }
                  },
                  "searchStep": 1
                }""";

        final StartSearchingCitiesRequest actual = objectMapper.readValue(
                givenJson,
                StartSearchingCitiesRequest.class
        );
        final StartSearchingCitiesRequest expected = new StartSearchingCitiesRequest(
                new AreaCoordinateRequest(
                        new CoordinateRequest(1.1, 2.2),
                        new CoordinateRequest(4.4, 5.5)
                ),
                1.
        );
        assertEquals(expected, actual);
    }

    private static String findFirstMessage(final Set<ConstraintViolation<StartSearchingCitiesRequest>> violations) {
        return violations.iterator().next().getMessage();
    }
}
