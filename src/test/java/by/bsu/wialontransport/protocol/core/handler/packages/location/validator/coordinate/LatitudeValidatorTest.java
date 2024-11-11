package by.bsu.wialontransport.protocol.core.handler.packages.location.validator.coordinate;

import by.bsu.wialontransport.crud.dto.Location;
import by.bsu.wialontransport.model.GpsCoordinate;
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
