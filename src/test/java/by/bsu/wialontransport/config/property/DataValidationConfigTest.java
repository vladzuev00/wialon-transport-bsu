package by.bsu.wialontransport.config.property;

import by.bsu.wialontransport.base.AbstractSpringBootTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.time.LocalDateTime;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public final class DataValidationConfigTest extends AbstractSpringBootTest {

    @Autowired
    private DataValidationConfig config;

    @Autowired
    private Validator validator;

    @Test
    public void configShouldBeCreated() {
        final DataValidationConfig expected = DataValidationConfig.builder()
                .minValidAmountOfSatellites(3)
                .maxValidAmountOfSatellites(999)
                .minValidDateTime(LocalDateTime.of(2010, 1, 1, 0, 0, 0))
                .maxValidDateTimeDeltaSecondsFromNow(15)
                .build();
        assertEquals(expected, config);
    }

    @Test(expected = IllegalArgumentException.class)
    public void configShouldNotBeCreatedBecauseOfMinValidAmountOfSatellitesIsNull() {
        DataValidationConfig.builder()
                .maxValidAmountOfSatellites(999)
                .minValidDateTime(LocalDateTime.of(2010, 1, 1, 0, 0, 0))
                .maxValidDateTimeDeltaSecondsFromNow(15)
                .build();
    }

    @Test(expected = IllegalArgumentException.class)
    public void configShouldNotBeCreatedBecauseOfMaxValidAmountOfSatellitesIsNull() {
        DataValidationConfig.builder()
                .minValidAmountOfSatellites(3)
                .minValidDateTime(LocalDateTime.of(2010, 1, 1, 0, 0, 0))
                .maxValidDateTimeDeltaSecondsFromNow(15)
                .build();
    }

    @Test(expected = IllegalArgumentException.class)
    public void configShouldNotBeCreatedBecauseOfMinValidAmountOfSatellitesIsBiggerThanMax() {
        DataValidationConfig.builder()
                .minValidAmountOfSatellites(999)
                .maxValidAmountOfSatellites(3)
                .minValidDateTime(LocalDateTime.of(2010, 1, 1, 0, 0, 0))
                .maxValidDateTimeDeltaSecondsFromNow(15)
                .build();
    }

    @Test
    public void configShouldBeValid() {
        final Set<ConstraintViolation<DataValidationConfig>> violations = validator.validate(config);
        assertTrue(violations.isEmpty());
    }

    @Test
    public void configShouldNotBeValidBecauseOfMinValidAmountOfSatellitesIsLessThanMinimalAllowable() {
        final DataValidationConfig givenConfig = DataValidationConfig.builder()
                .minValidAmountOfSatellites(0)
                .maxValidAmountOfSatellites(999)
                .minValidDateTime(LocalDateTime.of(2010, 1, 1, 0, 0, 0))
                .maxValidDateTimeDeltaSecondsFromNow(15)
                .build();

        final Set<ConstraintViolation<DataValidationConfig>> violations = validator.validate(givenConfig);
        assertEquals(1, violations.size());
        assertEquals("must be greater than or equal to 1", findFirstMessage(violations));
    }

    @Test
    public void configShouldNotBeValidBecauseOfMinValidAmountOfSatellitesIsGreaterThanMaximalAllowable() {
        final DataValidationConfig givenConfig = DataValidationConfig.builder()
                .minValidAmountOfSatellites(10000)
                .maxValidAmountOfSatellites(10000)
                .minValidDateTime(LocalDateTime.of(2010, 1, 1, 0, 0, 0))
                .maxValidDateTimeDeltaSecondsFromNow(15)
                .build();

        final Set<ConstraintViolation<DataValidationConfig>> violations = validator.validate(givenConfig);
        assertEquals(1, violations.size());
        assertEquals("must be less than or equal to 9999", findFirstMessage(violations));
    }

    @Test
    public void configShouldNotBeValidBecauseOfMaxValidAmountOfSatellitesIsLessThanMinimalAllowable() {
        final DataValidationConfig givenConfig = DataValidationConfig.builder()
                .minValidAmountOfSatellites(1)
                .maxValidAmountOfSatellites(1)
                .minValidDateTime(LocalDateTime.of(2010, 1, 1, 0, 0, 0))
                .maxValidDateTimeDeltaSecondsFromNow(15)
                .build();

        final Set<ConstraintViolation<DataValidationConfig>> violations = validator.validate(givenConfig);
        assertEquals(1, violations.size());
        assertEquals("must be greater than or equal to 2", findFirstMessage(violations));
    }

    @Test
    public void configShouldNotBeValidBecauseOfMaxValidAmountOfSatellitesIsGreaterThanMaximalAllowable() {
        final DataValidationConfig givenConfig = DataValidationConfig.builder()
                .minValidAmountOfSatellites(3)
                .maxValidAmountOfSatellites(10001)
                .minValidDateTime(LocalDateTime.of(2010, 1, 1, 0, 0, 0))
                .maxValidDateTimeDeltaSecondsFromNow(15)
                .build();

        final Set<ConstraintViolation<DataValidationConfig>> violations = validator.validate(givenConfig);
        assertEquals(1, violations.size());
        assertEquals("must be less than or equal to 10000", findFirstMessage(violations));
    }

    @Test
    public void configShouldNotBeValidBecauseOfMinValidDateTimeIsNull() {
        final DataValidationConfig givenConfig = DataValidationConfig.builder()
                .minValidAmountOfSatellites(3)
                .maxValidAmountOfSatellites(999)
                .maxValidDateTimeDeltaSecondsFromNow(15)
                .build();

        final Set<ConstraintViolation<DataValidationConfig>> violations = validator.validate(givenConfig);
        assertEquals(1, violations.size());
        assertEquals("must not be null", findFirstMessage(violations));
    }

    @Test
    public void configShouldNotBeValidBecauseOfMinValidDateTimeIsNotPast() {
        final DataValidationConfig givenConfig = DataValidationConfig.builder()
                .minValidAmountOfSatellites(3)
                .maxValidAmountOfSatellites(999)
                .minValidDateTime(LocalDateTime.now().plusDays(1))
                .maxValidDateTimeDeltaSecondsFromNow(15)
                .build();

        final Set<ConstraintViolation<DataValidationConfig>> violations = validator.validate(givenConfig);
        assertEquals(1, violations.size());
        assertEquals("must be a past date", findFirstMessage(violations));
    }

    @Test
    public void configShouldNotBeValidBecauseOfMaxValidDateTimeDeltaSecondsFromNowIsNull() {
        final DataValidationConfig givenConfig = DataValidationConfig.builder()
                .minValidAmountOfSatellites(3)
                .maxValidAmountOfSatellites(999)
                .minValidDateTime(LocalDateTime.of(2010, 1, 1, 0, 0, 0))
                .build();

        final Set<ConstraintViolation<DataValidationConfig>> violations = validator.validate(givenConfig);
        assertEquals(1, violations.size());
        assertEquals("must not be null", findFirstMessage(violations));
    }

    @Test
    public void configShouldNotBeValidBecauseOfMaxValidDateTimeDeltaSecondsFromNowIsNotPositive() {
        final DataValidationConfig givenConfig = DataValidationConfig.builder()
                .minValidAmountOfSatellites(3)
                .maxValidAmountOfSatellites(999)
                .minValidDateTime(LocalDateTime.of(2010, 1, 1, 0, 0, 0))
                .maxValidDateTimeDeltaSecondsFromNow(0)
                .build();

        final Set<ConstraintViolation<DataValidationConfig>> violations = validator.validate(givenConfig);
        assertEquals(1, violations.size());
        assertEquals("must be greater than 0", findFirstMessage(violations));
    }

    private static String findFirstMessage(final Set<ConstraintViolation<DataValidationConfig>> violations) {
        return violations.iterator().next().getMessage();
    }
}
