package by.vladzuev.locationreceiver.protocol.core.handler.packages.location.validator.property;

import by.vladzuev.locationreceiver.crud.dto.Location;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public final class LocationPropertyValidatorTest {
    private final TestLocationPropertyValidator validator = new TestLocationPropertyValidator();

    @Test
    public void locationShouldBeValid() {
        final Location givenLocation = Location.builder().speed(15).build();

        assertTrue(validator.isValid(givenLocation));
    }

    @Test
    public void locationShouldNotBeValidBecauseOfSpeedIsLessThanMinAllowable() {
        final Location givenLocation = Location.builder().speed(9.9999999).build();

        assertFalse(validator.isValid(givenLocation));
    }

    @Test
    public void locationShouldNotBeValidBecauseOfSpeedIsBiggerThanMaxAllowable() {
        final Location givenLocation = Location.builder().speed(20.000001).build();

        assertFalse(validator.isValid(givenLocation));
    }

    private static final class TestLocationPropertyValidator extends LocationPropertyValidator<Double> {
        private static final Double MIN = 10.;
        private static final Double MAX = 20.;

        @Override
        protected Double getValue(final Location location) {
            return location.getSpeed();
        }

        @Override
        protected Double getMin() {
            return MIN;
        }

        @Override
        protected Double getMax() {
            return MAX;
        }

        @Override
        protected boolean isLessOrEqual(final Double first, final Double second) {
            return first.compareTo(second) <= 0;
        }

        @Override
        protected boolean isBiggerOrEqual(final Double first, final Double second) {
            return first.compareTo(second) >= 0;
        }
    }
}
