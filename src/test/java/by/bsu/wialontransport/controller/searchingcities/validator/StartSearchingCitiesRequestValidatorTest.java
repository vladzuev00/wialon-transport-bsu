package by.bsu.wialontransport.controller.searchingcities.validator;

import by.bsu.wialontransport.controller.exception.CustomValidationException;
import by.bsu.wialontransport.controller.searchingcities.model.StartSearchingCitiesRequest;
import by.bsu.wialontransport.model.AreaCoordinate;
import by.bsu.wialontransport.model.RequestCoordinate;
import org.junit.Test;

public final class StartSearchingCitiesRequestValidatorTest {

    private final StartSearchingCitiesRequestValidator validator = new StartSearchingCitiesRequestValidator();

    @Test
    public void requestShouldBeValid() {
        final StartSearchingCitiesRequest givenRequest = new StartSearchingCitiesRequest(
                new AreaCoordinate(
                        new RequestCoordinate(1., 2.),
                        new RequestCoordinate(3., 4.)
                ),
                1.
        );

        this.validator.validate(givenRequest);
    }

    @Test(expected = CustomValidationException.class)
    public void requestShouldBeValidBecauseOfAreaCoordinate() {
        final StartSearchingCitiesRequest givenRequest = new StartSearchingCitiesRequest(
                new AreaCoordinate(
                        new RequestCoordinate(1., 2.),
                        new RequestCoordinate(0.1, 4.)
                ),
                1.
        );

        this.validator.validate(givenRequest);
    }

}
