package by.vladzuev.locationreceiver.protocol.core.handler.packages.location.validator.property.coordinate;

import by.vladzuev.locationreceiver.crud.dto.Location;
import by.vladzuev.locationreceiver.model.GpsCoordinate;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public final class LongitudeValidatorTest {
    private final LongitudeValidator validator = new LongitudeValidator();

    @Test
    public void valueShouldBeGot() {
        final double givenLongitude = 5.5;
        final Location givenLocation = Location.builder()
                .coordinate(GpsCoordinate.builder().longitude(givenLongitude).build())
                .build();

        final Double actual = validator.getValue(givenLocation);
        assertEquals(givenLongitude, actual);
    }
}
