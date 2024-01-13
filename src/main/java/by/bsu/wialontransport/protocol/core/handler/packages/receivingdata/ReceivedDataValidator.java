package by.bsu.wialontransport.protocol.core.handler.packages.receivingdata;

import by.bsu.wialontransport.configuration.property.DataValidationConfiguration;
import by.bsu.wialontransport.model.Coordinate;
import by.bsu.wialontransport.protocol.core.model.ReceivedData;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

import static java.lang.Double.compare;
import static java.time.LocalDateTime.now;

@Component
@RequiredArgsConstructor
public final class ReceivedDataValidator {
    private static final double MIN_ALLOWABLE_LATITUDE = -90;
    private static final double MAX_ALLOWABLE_LATITUDE = 90;
    private static final double MIN_ALLOWABLE_LONGITUDE = -180;
    private static final double MAX_ALLOWABLE_LONGITUDE = 180;

    private final DataValidationConfiguration validationProperty;

    public boolean isValid(final ReceivedData data) {
        return isValidCoordinate(data) && isValidAmountOfSatellites(data) && isValidDateTime(data);
    }

    private static boolean isValidCoordinate(final ReceivedData data) {
        final Coordinate research = data.getCoordinate();
        return isValidLatitude(research) && isValidLongitude(research);
    }

    private static boolean isValidLatitude(final Coordinate coordinate) {
        final double research = coordinate.getLatitude();
        return compare(research, MIN_ALLOWABLE_LATITUDE) >= 0 && compare(research, MAX_ALLOWABLE_LATITUDE) <= 0;
    }

    private static boolean isValidLongitude(final Coordinate coordinate) {
        final double research = coordinate.getLongitude();
        return compare(research, MIN_ALLOWABLE_LONGITUDE) >= 0 && compare(research, MAX_ALLOWABLE_LONGITUDE) <= 0;
    }

    private boolean isValidAmountOfSatellites(final ReceivedData data) {
        final int research = data.getAmountOfSatellites();
        return validationProperty.getMinValidAmountOfSatellites() <= research
                && research <= validationProperty.getMaxValidAmountSatellites();
    }

    private boolean isValidDateTime(final ReceivedData data) {
        final LocalDateTime research = data.getDateTime();
        final LocalDateTime minAllowableDateTime = validationProperty.getMinValidDateTime();
        final LocalDateTime maxAllowableDateTime = findMaxAllowableDateTime();
        return research.isAfter(minAllowableDateTime) && research.isBefore(maxAllowableDateTime);
    }

    private LocalDateTime findMaxAllowableDateTime() {
        return now().plusSeconds(validationProperty.getDeltaSecondsFromNowMaxAllowableValidDateTime());
    }
}
