package by.vladzuev.locationreceiver.protocol.core.handler.packages.location.validator.property;

import by.vladzuev.locationreceiver.protocol.core.property.LocationValidationProperty;
import by.vladzuev.locationreceiver.crud.dto.Location;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public final class SatelliteCountValidatorTest {

    @Mock
    private LocationValidationProperty mockedProperty;

    private SatelliteCountValidator validator;

    @BeforeEach
    public void initializeValidator() {
        validator = new SatelliteCountValidator(mockedProperty);
    }

    @Test
    public void satelliteCountShouldBeGot() {
        final int givenSatelliteCount = 10;
        final Location givenLocation = Location.builder().satelliteCount(givenSatelliteCount).build();

        final Integer actual = validator.getValue(givenLocation);
        assertEquals(givenSatelliteCount, actual);
    }

    @Test
    public void minAllowableShouldBeGot() {
        final int givenMinSatelliteCount = 10;
        when(mockedProperty.getMinSatelliteCount()).thenReturn(givenMinSatelliteCount);

        final Integer actual = validator.getMin();
        assertEquals(givenMinSatelliteCount, actual);
    }

    @Test
    public void maxAllowableShouldBeGot() {
        final int givenMaxSatelliteCount = 10;
        when(mockedProperty.getMaxSatelliteCount()).thenReturn(givenMaxSatelliteCount);

        final Integer actual = validator.getMax();
        assertEquals(givenMaxSatelliteCount, actual);
    }

    @Test
    public void firstShouldBeLessOrEqualThanSecond() {
        final Integer first = 10;
        final Integer second = 11;

        assertTrue(validator.isLessOrEqual(first, second));
    }

    @Test
    public void firstShouldNotBeLessOrEqualThanSecond() {
        final Integer first = 11;
        final Integer second = 10;

        assertFalse(validator.isLessOrEqual(first, second));
    }

    @Test
    public void firstShouldBeBiggerOrEqualThanSecond() {
        final Integer first = 11;
        final Integer second = 10;

        assertTrue(validator.isBiggerOrEqual(first, second));
    }

    @Test
    public void firstShouldNotBeBiggerOrEqualThanSecond() {
        final Integer first = 10;
        final Integer second = 11;

        assertFalse(validator.isBiggerOrEqual(first, second));
    }
}
