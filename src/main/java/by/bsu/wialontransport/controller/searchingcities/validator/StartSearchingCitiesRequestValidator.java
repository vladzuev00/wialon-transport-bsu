package by.bsu.wialontransport.controller.searchingcities.validator;

import by.bsu.wialontransport.controller.exception.CustomValidationException;
import by.bsu.wialontransport.controller.searchingcities.model.StartSearchingCitiesRequest;
import by.bsu.wialontransport.model.AreaCoordinate;
import by.bsu.wialontransport.model.Coordinate;
import org.springframework.stereotype.Component;

import static java.lang.Double.compare;

@Component
public final class StartSearchingCitiesRequestValidator {
    private static final String EXCEPTION_MESSAGE_NOT_VALID_AREA_COORDINATE
            = "Left bottom point's coordinates should be less than right upper point's coordinates.";

    public void validate(final StartSearchingCitiesRequest request) {
        if (!isValidAreaCoordinate(request.getAreaCoordinate())) {
            throw new CustomValidationException(EXCEPTION_MESSAGE_NOT_VALID_AREA_COORDINATE);
        }
    }

    private static boolean isValidAreaCoordinate(final AreaCoordinate research) {
        final Coordinate leftBottom = research.getLeftBottom();
        final Coordinate rightUpper = research.getRightUpper();
        return compare(leftBottom.getLatitude(), rightUpper.getLatitude()) <= 0
                && compare(leftBottom.getLongitude(), rightUpper.getLongitude()) <= 0;
    }
}
