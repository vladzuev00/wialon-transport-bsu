package by.bsu.wialontransport.config.property;

import by.bsu.wialontransport.base.AbstractSpringBootTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

import static by.bsu.wialontransport.util.ConstraintViolationUtil.findFirstMessage;
import static by.bsu.wialontransport.util.ConstraintViolationUtil.findMessages;
import static java.time.LocalDateTime.now;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public final class LocationValidationPropertyTest extends AbstractSpringBootTest {

    @Autowired
    private LocationValidationProperty config;

    @Autowired
    private Validator validator;

    @Test
    public void configShouldBeCreated() {
        final LocationValidationProperty expected = LocationValidationProperty.builder()
                .minAmountOfSatellites(3)
                .maxAmountOfSatellites(999)
                .minDateTime(LocalDateTime.of(2010, 1, 1, 0, 0, 0))
                .maxDateTimeDeltaSecondsFromNow(15)
                .build();
        assertEquals(expected, config);
    }

    @Test(expected = IllegalArgumentException.class)
    public void configShouldNotBeCreatedBecauseOfMinAmountOfSatellitesIsNull() {
        LocationValidationProperty.builder()
                .maxAmountOfSatellites(999)
                .minDateTime(LocalDateTime.of(2010, 1, 1, 0, 0, 0))
                .maxDateTimeDeltaSecondsFromNow(15)
                .build();
    }

    @Test(expected = IllegalArgumentException.class)
    public void configShouldNotBeCreatedBecauseOfMaxAmountOfSatellitesIsNull() {
        LocationValidationProperty.builder()
                .minAmountOfSatellites(3)
                .minDateTime(LocalDateTime.of(2010, 1, 1, 0, 0, 0))
                .maxDateTimeDeltaSecondsFromNow(15)
                .build();
    }

    @Test(expected = IllegalArgumentException.class)
    public void configShouldNotBeCreatedBecauseOfMinAmountOfSatellitesIsBiggerThanMax() {
        LocationValidationProperty.builder()
                .minAmountOfSatellites(999)
                .maxAmountOfSatellites(3)
                .minDateTime(LocalDateTime.of(2010, 1, 1, 0, 0, 0))
                .maxDateTimeDeltaSecondsFromNow(15)
                .build();
    }

    @Test
    public void configShouldBeValid() {
        final Set<ConstraintViolation<LocationValidationProperty>> violations = validator.validate(config);
        assertTrue(violations.isEmpty());
    }

    @Test
    public void configShouldNotBeValidBecauseOfMinAndMaxAmountOfSatellitesAreNotPositive() {
        final LocationValidationProperty givenConfig = LocationValidationProperty.builder()
                .minAmountOfSatellites(0)
                .maxAmountOfSatellites(0)
                .minDateTime(LocalDateTime.of(2010, 1, 1, 0, 0, 0))
                .maxDateTimeDeltaSecondsFromNow(15)
                .build();

        final Set<ConstraintViolation<LocationValidationProperty>> violations = validator.validate(givenConfig);
        final List<String> actualMessages = findMessages(violations);
        final List<String> expectedMessages = List.of("must be greater than 0", "must be greater than 0");
        assertEquals(expectedMessages, actualMessages);
    }

    @Test
    public void configShouldNotBeValidBecauseOfMinDateTimeIsNull() {
        final LocationValidationProperty givenConfig = LocationValidationProperty.builder()
                .minAmountOfSatellites(3)
                .maxAmountOfSatellites(999)
                .maxDateTimeDeltaSecondsFromNow(15)
                .build();

        final Set<ConstraintViolation<LocationValidationProperty>> violations = validator.validate(givenConfig);
        assertEquals(1, violations.size());
        assertEquals("must not be null", findFirstMessage(violations));
    }

    @Test
    public void configShouldNotBeValidBecauseOfMinDateTimeIsNotPast() {
        final LocationValidationProperty givenConfig = LocationValidationProperty.builder()
                .minAmountOfSatellites(3)
                .maxAmountOfSatellites(999)
                .minDateTime(now().plusDays(1))
                .maxDateTimeDeltaSecondsFromNow(15)
                .build();

        final Set<ConstraintViolation<LocationValidationProperty>> violations = validator.validate(givenConfig);
        assertEquals(1, violations.size());
        assertEquals("must be a past date", findFirstMessage(violations));
    }

    @Test
    public void configShouldNotBeValidBecauseOfMaxDateTimeDeltaSecondsFromNowIsNull() {
        final LocationValidationProperty givenConfig = LocationValidationProperty.builder()
                .minAmountOfSatellites(3)
                .maxAmountOfSatellites(999)
                .minDateTime(LocalDateTime.of(2010, 1, 1, 0, 0, 0))
                .build();

        final Set<ConstraintViolation<LocationValidationProperty>> violations = validator.validate(givenConfig);
        assertEquals(1, violations.size());
        assertEquals("must not be null", findFirstMessage(violations));
    }

    @Test
    public void configShouldNotBeValidBecauseOfMaxDateTimeDeltaSecondsFromNowIsNotPositive() {
        final LocationValidationProperty givenConfig = LocationValidationProperty.builder()
                .minAmountOfSatellites(3)
                .maxAmountOfSatellites(999)
                .minDateTime(LocalDateTime.of(2010, 1, 1, 0, 0, 0))
                .maxDateTimeDeltaSecondsFromNow(0)
                .build();

        final Set<ConstraintViolation<LocationValidationProperty>> violations = validator.validate(givenConfig);
        assertEquals(1, violations.size());
        assertEquals("must be greater than 0", findFirstMessage(violations));
    }
}
