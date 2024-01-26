package by.bsu.wialontransport.protocol.wialon.model.coordinate;

import by.bsu.wialontransport.protocol.wialon.model.coordinate.Latitude.LatitudeHemisphere;
import org.junit.Test;

import static by.bsu.wialontransport.protocol.wialon.model.coordinate.Latitude.LatitudeHemisphere.NORTH;
import static by.bsu.wialontransport.protocol.wialon.model.coordinate.Latitude.LatitudeHemisphere.findByValue;
import static org.junit.Assert.assertSame;

public final class LatitudeTest {

    @Test
    public void hemisphereShouldBeFoundByValue() {
        final char givenValue = 'N';

        final LatitudeHemisphere actual = findByValue(givenValue);
        assertSame(NORTH, actual);
    }

    @Test(expected = IllegalArgumentException.class)
    public void hemisphereShouldNotBeFoundByValue() {
        final char givenValue = 'W';

        findByValue(givenValue);
    }
}
