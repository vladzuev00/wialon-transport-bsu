package by.vladzuev.locationreceiver.protocol.core.handler.packages.location.validator.property.coordinate;

import by.vladzuev.locationreceiver.crud.dto.Location;
import by.vladzuev.locationreceiver.model.GpsCoordinate;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public final class LatitudeValidatorTest {
    private final LatitudeValidator validator = new LatitudeValidator();

    @Test
    public void valueShouldBeGot() {
        final double givenLatitude = 5.5;
        final Location givenLocation = Location.builder()
                .coordinate(GpsCoordinate.builder().latitude(givenLatitude).build())
                .build();

        final Double actual = validator.getValue(givenLocation);
        assertEquals(givenLatitude, actual);
    }
}
