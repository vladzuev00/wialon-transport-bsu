package by.vladzuev.locationreceiver.protocol.core.handler.packages.location.validator.property.coordinate;

import by.vladzuev.locationreceiver.crud.dto.Location;
import org.junit.jupiter.api.Test;

import static java.lang.Double.MAX_VALUE;
import static java.lang.Double.MIN_VALUE;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public final class GpsCoordinateValidatorTest {
    private final TestGpsCoordinateValidator validator = new TestGpsCoordinateValidator();

    @Test
    public void firstShouldBeLessOrEqualThanSecond() {
        final Double givenFirst = 50.;
        final Double givenSecond = 60.;

        assertTrue(validator.isLessOrEqual(givenFirst, givenSecond));
    }

    @Test
    public void firstShouldNotBeLessOrEqualThanSecond() {
        final Double givenFirst = 60.;
        final Double givenSecond = 50.;

        assertFalse(validator.isLessOrEqual(givenFirst, givenSecond));
    }

    @Test
    public void firstShouldBeBiggerOrEqualThanSecond() {
        final Double givenFirst = 60.;
        final Double givenSecond = 50.;

        assertTrue(validator.isBiggerOrEqual(givenFirst, givenSecond));
    }

    @Test
    public void firstShouldNotBeBiggerOrEqualThanSecond() {
        final Double givenFirst = 50.;
        final Double givenSecond = 60.;

        assertFalse(validator.isBiggerOrEqual(givenFirst, givenSecond));
    }

    private static final class TestGpsCoordinateValidator extends GpsCoordinateValidator {

        public TestGpsCoordinateValidator() {
            super(MIN_VALUE, MAX_VALUE);
        }

        @Override
        protected Double getValue(final Location location) {
            throw new UnsupportedOperationException();
        }
    }
}
