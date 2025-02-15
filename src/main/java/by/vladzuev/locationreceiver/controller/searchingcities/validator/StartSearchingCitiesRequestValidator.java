package by.vladzuev.locationreceiver.controller.searchingcities.validator;

import by.vladzuev.locationreceiver.controller.exception.CustomValidationException;
import by.vladzuev.locationreceiver.controller.searchingcities.model.StartSearchingCitiesRequest;
import by.vladzuev.locationreceiver.model.AreaCoordinateRequest;
import by.vladzuev.locationreceiver.model.CoordinateRequest;
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
        final CoordinateRequest leftBottom = areaCoordinate.getLeftBottom();
        final CoordinateRequest rightUpper = areaCoordinate.getRightUpper();
        return compare(leftBottom.getLatitude(), rightUpper.getLatitude()) <= 0
                && compare(leftBottom.getLongitude(), rightUpper.getLongitude()) <= 0;
    }
}
