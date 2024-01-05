package by.bsu.wialontransport.controller.searchingcities.validator;

import by.bsu.wialontransport.controller.exception.CustomValidationException;
import by.bsu.wialontransport.controller.searchingcities.model.StartSearchingCitiesRequest;
import by.bsu.wialontransport.model.AreaCoordinateRequest;
import by.bsu.wialontransport.model.RequestCoordinate;
import org.springframework.stereotype.Component;

import static java.lang.Double.compare;

@Component
public final class StartSearchingCitiesRequestValidator {

    public void validate(final StartSearchingCitiesRequest request) {
        if (!isValidAreaCoordinate(request.getAreaCoordinate())) {
            throw new CustomValidationException(
                    "Left bottom point's coordinates should be less than right upper point's coordinates."
            );
        }
    }

    private static boolean isValidAreaCoordinate(final AreaCoordinateRequest areaCoordinate) {
        final RequestCoordinate leftBottom = areaCoordinate.getLeftBottom();
        final RequestCoordinate rightUpper = areaCoordinate.getRightUpper();
        return compare(leftBottom.getLatitude(), rightUpper.getLatitude()) <= 0
                && compare(leftBottom.getLongitude(), rightUpper.getLongitude()) <= 0;
    }
}
