package by.bsu.wialontransport.protocol.wialon.model.coordinate;

import by.bsu.wialontransport.protocol.wialon.model.coordinate.Longitude.LongitudeHemisphere;
import org.junit.Test;

import static by.bsu.wialontransport.protocol.wialon.model.coordinate.Longitude.LongitudeHemisphere.EAST;
import static by.bsu.wialontransport.protocol.wialon.model.coordinate.Longitude.LongitudeHemisphere.findByValue;
import static org.junit.Assert.assertSame;

public final class LongitudeTest {

    @Test
    public void hemisphereShouldBeFoundByValue() {
        final char givenValue = 'E';

        final LongitudeHemisphere actual = findByValue(givenValue);
        assertSame(EAST, actual);
    }

    @Test(expected = IllegalArgumentException.class)
    public void hemisphereShouldNotBeFoundByValue() {
        final char givenValue = 'N';

        findByValue(givenValue);
    }
}
