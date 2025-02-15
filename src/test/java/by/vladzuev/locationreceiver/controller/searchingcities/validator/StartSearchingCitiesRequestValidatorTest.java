package by.vladzuev.locationreceiver.controller.searchingcities.validator;

import by.vladzuev.locationreceiver.controller.exception.CustomValidationException;
import by.vladzuev.locationreceiver.controller.searchingcities.model.StartSearchingCitiesRequest;
import by.vladzuev.locationreceiver.model.AreaCoordinateRequest;
import by.vladzuev.locationreceiver.model.CoordinateRequest;
import org.junit.Test;

public final class StartSearchingCitiesRequestValidatorTest {

    private final StartSearchingCitiesRequestValidator validator = new StartSearchingCitiesRequestValidator();

    @Test
    public void requestShouldBeValid() {
        final StartSearchingCitiesRequest givenRequest = new StartSearchingCitiesRequest(
                new AreaCoordinateRequest(
                        new CoordinateRequest(1., 2.),
                        new CoordinateRequest(3., 4.)
                ),
                1.
        );

        validator.validate(givenRequest);
    }

    @Test(expected = CustomValidationException.class)
    public void requestShouldBeValidBecauseOfAreaCoordinate() {
        final StartSearchingCitiesRequest givenRequest = new StartSearchingCitiesRequest(
                new AreaCoordinateRequest(
                        new CoordinateRequest(1., 2.),
                        new CoordinateRequest(0.1, 4.)
                ),
                1.
        );

        validator.validate(givenRequest);
    }

}
