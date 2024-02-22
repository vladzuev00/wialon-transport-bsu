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
        assertEquals("", findFirstMessage(violations));
    }

    private static String findFirstMessage(final Set<ConstraintViolation<DataValidationConfig>> violations) {
        return violations.iterator().next().getMessage();
    }
}
