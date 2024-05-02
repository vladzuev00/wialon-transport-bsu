package by.bsu.wialontransport.config.property;

import by.bsu.wialontransport.base.AbstractSpringBootTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public final class DataDefaultPropertyConfigTest extends AbstractSpringBootTest {

    @Autowired
    private DataDefaultPropertyConfig config;

    @Autowired
    private Validator validator;

    @Test
    public void configShouldBeCreated() {
        final DataDefaultPropertyConfig expected = DataDefaultPropertyConfig.builder()
                .course(0)
                .altitude(0)
                .speed(0.)
                .amountOfSatellites(3)
                .hdop(5.)
                .inputs(0)
                .outputs(0)
                .driverKeyCode("not defined")
                .build();
        assertEquals(expected, config);
    }

    @Test
    public void configShouldBeValid() {
        final DataDefaultPropertyConfig givenConfig = DataDefaultPropertyConfig.builder()
                .course(0)
                .altitude(0)
                .speed(0.)
                .amountOfSatellites(3)
                .hdop(5.)
                .inputs(0)
                .outputs(0)
                .driverKeyCode("not defined")
                .build();

        final Set<ConstraintViolation<DataDefaultPropertyConfig>> violations = validator.validate(givenConfig);
        assertTrue(violations.isEmpty());
    }

    @Test
    public void configShouldNotBeValidBecauseOfCourseIsNull() {
        final DataDefaultPropertyConfig givenConfig = DataDefaultPropertyConfig.builder()
                .altitude(0)
                .speed(0.)
                .amountOfSatellites(3)
                .hdop(5.)
                .inputs(0)
                .outputs(0)
                .driverKeyCode("not defined")
                .build();

        final Set<ConstraintViolation<DataDefaultPropertyConfig>> violations = validator.validate(givenConfig);
        assertEquals(1, violations.size());
        assertEquals("must not be null", findFirstMessage(violations));
    }

    @Test
    public void configShouldNotBeValidBecauseOfAltitudeIsNull() {
        final DataDefaultPropertyConfig givenConfig = DataDefaultPropertyConfig.builder()
                .course(0)
                .speed(0.)
                .amountOfSatellites(3)
                .hdop(5.)
                .inputs(0)
                .outputs(0)
                .driverKeyCode("not defined")
                .build();

        final Set<ConstraintViolation<DataDefaultPropertyConfig>> violations = validator.validate(givenConfig);
        assertEquals(1, violations.size());
        assertEquals("must not be null", findFirstMessage(violations));
    }

    @Test
    public void configShouldNotBeValidBecauseOfSpeedIsNull() {
        final DataDefaultPropertyConfig givenConfig = DataDefaultPropertyConfig.builder()
                .course(0)
                .altitude(0)
                .amountOfSatellites(3)
                .hdop(5.)
                .inputs(0)
                .outputs(0)
                .driverKeyCode("not defined")
                .build();

        final Set<ConstraintViolation<DataDefaultPropertyConfig>> violations = validator.validate(givenConfig);
        assertEquals(1, violations.size());
        assertEquals("must not be null", findFirstMessage(violations));
    }

    @Test
    public void configShouldNotBeValidBecauseOfSpeedIsNegative() {
        final DataDefaultPropertyConfig givenConfig = DataDefaultPropertyConfig.builder()
                .course(0)
                .altitude(0)
                .speed(-0.0000001)
                .amountOfSatellites(3)
                .hdop(5.)
                .inputs(0)
                .outputs(0)
                .driverKeyCode("not defined")
                .build();

        final Set<ConstraintViolation<DataDefaultPropertyConfig>> violations = validator.validate(givenConfig);
        assertEquals(1, violations.size());
        assertEquals("must be greater than or equal to 0", findFirstMessage(violations));
    }

    @Test
    public void configShouldNotBeValidBecauseOfAmountOfSatellitesIsNull() {
        final DataDefaultPropertyConfig givenConfig = DataDefaultPropertyConfig.builder()
                .course(0)
                .altitude(0)
                .speed(0.)
                .hdop(5.)
                .inputs(0)
                .outputs(0)
                .driverKeyCode("not defined")
                .build();

        final Set<ConstraintViolation<DataDefaultPropertyConfig>> violations = validator.validate(givenConfig);
        assertEquals(1, violations.size());
        assertEquals("must not be null", findFirstMessage(violations));
    }

    @Test
    public void configShouldNotBeValidBecauseOfAmountOfSatellitesIsNotPositive() {
        final DataDefaultPropertyConfig givenConfig = DataDefaultPropertyConfig.builder()
                .course(0)
                .altitude(0)
                .speed(0.)
                .amountOfSatellites(0)
                .hdop(5.)
                .inputs(0)
                .outputs(0)
                .driverKeyCode("not defined")
                .build();

        final Set<ConstraintViolation<DataDefaultPropertyConfig>> violations = validator.validate(givenConfig);
        assertEquals(1, violations.size());
        assertEquals("must be greater than 0", findFirstMessage(violations));
    }

    @Test
    public void configShouldNotBeValidBecauseOfHdopIsNull() {
        final DataDefaultPropertyConfig givenConfig = DataDefaultPropertyConfig.builder()
                .course(0)
                .altitude(0)
                .speed(0.)
                .amountOfSatellites(3)
                .inputs(0)
                .outputs(0)
                .driverKeyCode("not defined")
                .build();

        final Set<ConstraintViolation<DataDefaultPropertyConfig>> violations = validator.validate(givenConfig);
        assertEquals(1, violations.size());
        assertEquals("must not be null", findFirstMessage(violations));
    }

    @Test
    public void configShouldNotBeValidBecauseOfHdopIsLessThanMinimalAllowable() {
        final DataDefaultPropertyConfig givenConfig = DataDefaultPropertyConfig.builder()
                .course(0)
                .altitude(0)
                .speed(0.)
                .amountOfSatellites(3)
                .hdop(0.9)
                .inputs(0)
                .outputs(0)
                .driverKeyCode("not defined")
                .build();

        final Set<ConstraintViolation<DataDefaultPropertyConfig>> violations = validator.validate(givenConfig);
        assertEquals(1, violations.size());
        assertEquals("must be greater than or equal to 1", findFirstMessage(violations));
    }

    @Test
    public void configShouldNotBeValidBecauseOfHdopIsBiggerThanMaximalAllowable() {
        final DataDefaultPropertyConfig givenConfig = DataDefaultPropertyConfig.builder()
                .course(0)
                .altitude(0)
                .speed(0.)
                .amountOfSatellites(3)
                .hdop(10.1)
                .inputs(0)
                .outputs(0)
                .driverKeyCode("not defined")
                .build();

        final Set<ConstraintViolation<DataDefaultPropertyConfig>> violations = validator.validate(givenConfig);
        assertEquals(1, violations.size());
        assertEquals("must be less than or equal to 10", findFirstMessage(violations));
    }

    @Test
    public void configShouldNotBeValidBecauseOfInputsIsNull() {
        final DataDefaultPropertyConfig givenConfig = DataDefaultPropertyConfig.builder()
                .course(0)
                .altitude(0)
                .speed(0.)
                .amountOfSatellites(3)
                .hdop(5.)
                .outputs(0)
                .driverKeyCode("not defined")
                .build();

        final Set<ConstraintViolation<DataDefaultPropertyConfig>> violations = validator.validate(givenConfig);
        assertEquals(1, violations.size());
        assertEquals("must not be null", findFirstMessage(violations));
    }

    @Test
    public void configShouldNotBeValidBecauseOfInputsIsNegative() {
        final DataDefaultPropertyConfig givenConfig = DataDefaultPropertyConfig.builder()
                .course(0)
                .altitude(0)
                .speed(0.)
                .amountOfSatellites(3)
                .hdop(5.)
                .inputs(-1)
                .outputs(0)
                .driverKeyCode("not defined")
                .build();

        final Set<ConstraintViolation<DataDefaultPropertyConfig>> violations = validator.validate(givenConfig);
        assertEquals(1, violations.size());
        assertEquals("must be greater than or equal to 0", findFirstMessage(violations));
    }

    @Test
    public void configShouldNotBeValidBecauseOfOutputsIsNull() {
        final DataDefaultPropertyConfig givenConfig = DataDefaultPropertyConfig.builder()
                .course(0)
                .altitude(0)
                .speed(0.)
                .amountOfSatellites(3)
                .hdop(5.)
                .inputs(0)
                .driverKeyCode("not defined")
                .build();

        final Set<ConstraintViolation<DataDefaultPropertyConfig>> violations = validator.validate(givenConfig);
        assertEquals(1, violations.size());
        assertEquals("must not be null", findFirstMessage(violations));
    }

    @Test
    public void configShouldNotBeValidBecauseOfOutputsIsNegative() {
        final DataDefaultPropertyConfig givenConfig = DataDefaultPropertyConfig.builder()
                .course(0)
                .altitude(0)
                .speed(0.)
                .amountOfSatellites(3)
                .hdop(5.)
                .inputs(0)
                .outputs(-1)
                .driverKeyCode("not defined")
                .build();

        final Set<ConstraintViolation<DataDefaultPropertyConfig>> violations = validator.validate(givenConfig);
        assertEquals(1, violations.size());
        assertEquals("must be greater than or equal to 0", findFirstMessage(violations));
    }

    @Test
    public void configShouldNotBeValidBecauseOfDriverKeyCodeIsNull() {
        final DataDefaultPropertyConfig givenConfig = DataDefaultPropertyConfig.builder()
                .course(0)
                .altitude(0)
                .speed(0.)
                .amountOfSatellites(3)
                .hdop(5.)
                .inputs(0)
                .outputs(0)
                .build();

        final Set<ConstraintViolation<DataDefaultPropertyConfig>> violations = validator.validate(givenConfig);
        assertEquals(1, violations.size());
        assertEquals("must not be blank", findFirstMessage(violations));
    }

    @Test
    public void configShouldNotBeValidBecauseOfDriverKeyCodeIsBlank() {
        final DataDefaultPropertyConfig givenConfig = DataDefaultPropertyConfig.builder()
                .course(0)
                .altitude(0)
                .speed(0.)
                .amountOfSatellites(3)
                .hdop(5.)
                .inputs(0)
                .outputs(0)
                .driverKeyCode("      \n\t")
                .build();

        final Set<ConstraintViolation<DataDefaultPropertyConfig>> violations = validator.validate(givenConfig);
        assertEquals(1, violations.size());
        assertEquals("must not be blank", findFirstMessage(violations));
    }

    private static String findFirstMessage(final Set<ConstraintViolation<DataDefaultPropertyConfig>> violations) {
        return violations.iterator().next().getMessage();
    }
}
