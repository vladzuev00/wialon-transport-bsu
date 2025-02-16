//package by.vladzuev.locationreceiver.config.property;
//
//import by.vladzuev.locationreceiver.base.AbstractSpringBootTest;
//import by.vladzuev.locationreceiver.protocol.core.property.LocationDefaultProperty;
//import by.vladzuev.locationreceiver.util.ConstraintViolationUtil;
//import org.junit.Assert;
//import org.junit.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//
//import javax.validation.ConstraintViolation;
//import javax.validation.Validator;
//import java.util.Set;
//
//import static org.junit.Assert.assertEquals;
//import static org.junit.Assert.assertTrue;
//
//public final class DataDefaultPropertyConfigTest extends AbstractSpringBootTest {
//
//    @Autowired
//    private LocationDefaultProperty config;
//
//    @Autowired
//    private Validator validator;
//
//    @Test
//    public void configShouldBeCreated() {
//        final LocationDefaultProperty expected = LocationDefaultProperty.builder()
//                .course(0)
//                .altitude(0)
//                .speed(0.)
//                .satelliteCount(3)
//                .hdop(5.)
//                .inputs(0)
//                .outputs(0)
//                .driverKeyCode("not defined")
//                .build();
//        assertEquals(expected, config);
//    }
//
//    @Test
//    public void configShouldBeValid() {
//        final LocationDefaultProperty givenConfig = LocationDefaultProperty.builder()
//                .course(0)
//                .altitude(0)
//                .speed(0.)
//                .satelliteCount(3)
//                .hdop(5.)
//                .inputs(0)
//                .outputs(0)
//                .driverKeyCode("not defined")
//                .build();
//
//        final Set<ConstraintViolation<LocationDefaultProperty>> violations = validator.validate(givenConfig);
//        assertTrue(violations.isEmpty());
//    }
//
//    @Test
//    public void configShouldNotBeValidBecauseOfCourseIsNull() {
//        final LocationDefaultProperty givenConfig = LocationDefaultProperty.builder()
//                .altitude(0)
//                .speed(0.)
//                .satelliteCount(3)
//                .hdop(5.)
//                .inputs(0)
//                .outputs(0)
//                .driverKeyCode("not defined")
//                .build();
//
//        final Set<ConstraintViolation<LocationDefaultProperty>> violations = validator.validate(givenConfig);
//        assertEquals(1, violations.size());
//        Assert.assertEquals("must not be null", ConstraintViolationUtil.findFirstMessage(violations));
//    }
//
//    @Test
//    public void configShouldNotBeValidBecauseOfAltitudeIsNull() {
//        final LocationDefaultProperty givenConfig = LocationDefaultProperty.builder()
//                .course(0)
//                .speed(0.)
//                .satelliteCount(3)
//                .hdop(5.)
//                .inputs(0)
//                .outputs(0)
//                .driverKeyCode("not defined")
//                .build();
//
//        final Set<ConstraintViolation<LocationDefaultProperty>> violations = validator.validate(givenConfig);
//        assertEquals(1, violations.size());
//        Assert.assertEquals("must not be null", ConstraintViolationUtil.findFirstMessage(violations));
//    }
//
//    @Test
//    public void configShouldNotBeValidBecauseOfSpeedIsNull() {
//        final LocationDefaultProperty givenConfig = LocationDefaultProperty.builder()
//                .course(0)
//                .altitude(0)
//                .satelliteCount(3)
//                .hdop(5.)
//                .inputs(0)
//                .outputs(0)
//                .driverKeyCode("not defined")
//                .build();
//
//        final Set<ConstraintViolation<LocationDefaultProperty>> violations = validator.validate(givenConfig);
//        assertEquals(1, violations.size());
//        Assert.assertEquals("must not be null", ConstraintViolationUtil.findFirstMessage(violations));
//    }
//
//    @Test
//    public void configShouldNotBeValidBecauseOfSpeedIsNegative() {
//        final LocationDefaultProperty givenConfig = LocationDefaultProperty.builder()
//                .course(0)
//                .altitude(0)
//                .speed(-0.0000001)
//                .satelliteCount(3)
//                .hdop(5.)
//                .inputs(0)
//                .outputs(0)
//                .driverKeyCode("not defined")
//                .build();
//
//        final Set<ConstraintViolation<LocationDefaultProperty>> violations = validator.validate(givenConfig);
//        assertEquals(1, violations.size());
//        Assert.assertEquals("must be greater than or equal to 0", ConstraintViolationUtil.findFirstMessage(violations));
//    }
//
//    @Test
//    public void configShouldNotBeValidBecauseOfAmountOfSatellitesIsNull() {
//        final LocationDefaultProperty givenConfig = LocationDefaultProperty.builder()
//                .course(0)
//                .altitude(0)
//                .speed(0.)
//                .hdop(5.)
//                .inputs(0)
//                .outputs(0)
//                .driverKeyCode("not defined")
//                .build();
//
//        final Set<ConstraintViolation<LocationDefaultProperty>> violations = validator.validate(givenConfig);
//        assertEquals(1, violations.size());
//        Assert.assertEquals("must not be null", ConstraintViolationUtil.findFirstMessage(violations));
//    }
//
//    @Test
//    public void configShouldNotBeValidBecauseOfAmountOfSatellitesIsNotPositive() {
//        final LocationDefaultProperty givenConfig = LocationDefaultProperty.builder()
//                .course(0)
//                .altitude(0)
//                .speed(0.)
//                .satelliteCount(0)
//                .hdop(5.)
//                .inputs(0)
//                .outputs(0)
//                .driverKeyCode("not defined")
//                .build();
//
//        final Set<ConstraintViolation<LocationDefaultProperty>> violations = validator.validate(givenConfig);
//        assertEquals(1, violations.size());
//        Assert.assertEquals("must be greater than 0", ConstraintViolationUtil.findFirstMessage(violations));
//    }
//
//    @Test
//    public void configShouldNotBeValidBecauseOfHdopIsNull() {
//        final LocationDefaultProperty givenConfig = LocationDefaultProperty.builder()
//                .course(0)
//                .altitude(0)
//                .speed(0.)
//                .satelliteCount(3)
//                .inputs(0)
//                .outputs(0)
//                .driverKeyCode("not defined")
//                .build();
//
//        final Set<ConstraintViolation<LocationDefaultProperty>> violations = validator.validate(givenConfig);
//        assertEquals(1, violations.size());
//        Assert.assertEquals("Not valid double", ConstraintViolationUtil.findFirstMessage(violations));
//    }
//
//    @Test
//    public void configShouldNotBeValidBecauseOfHdopIsLessThanMinimalAllowable() {
//        final LocationDefaultProperty givenConfig = LocationDefaultProperty.builder()
//                .course(0)
//                .altitude(0)
//                .speed(0.)
//                .satelliteCount(3)
//                .hdop(0.99999999)
//                .inputs(0)
//                .outputs(0)
//                .driverKeyCode("not defined")
//                .build();
//
//        final Set<ConstraintViolation<LocationDefaultProperty>> violations = validator.validate(givenConfig);
//        assertEquals(1, violations.size());
//        Assert.assertEquals("Not valid double", ConstraintViolationUtil.findFirstMessage(violations));
//    }
//
//    @Test
//    public void configShouldNotBeValidBecauseOfHdopIsBiggerThanMaximalAllowable() {
//        final LocationDefaultProperty givenConfig = LocationDefaultProperty.builder()
//                .course(0)
//                .altitude(0)
//                .speed(0.)
//                .satelliteCount(3)
//                .hdop(10.00000001)
//                .inputs(0)
//                .outputs(0)
//                .driverKeyCode("not defined")
//                .build();
//
//        final Set<ConstraintViolation<LocationDefaultProperty>> violations = validator.validate(givenConfig);
//        assertEquals(1, violations.size());
//        Assert.assertEquals("Not valid double", ConstraintViolationUtil.findFirstMessage(violations));
//    }
//
//    @Test
//    public void configShouldNotBeValidBecauseOfInputsIsNull() {
//        final LocationDefaultProperty givenConfig = LocationDefaultProperty.builder()
//                .course(0)
//                .altitude(0)
//                .speed(0.)
//                .satelliteCount(3)
//                .hdop(5.)
//                .outputs(0)
//                .driverKeyCode("not defined")
//                .build();
//
//        final Set<ConstraintViolation<LocationDefaultProperty>> violations = validator.validate(givenConfig);
//        assertEquals(1, violations.size());
//        Assert.assertEquals("must not be null", ConstraintViolationUtil.findFirstMessage(violations));
//    }
//
//    @Test
//    public void configShouldNotBeValidBecauseOfInputsIsNegative() {
//        final LocationDefaultProperty givenConfig = LocationDefaultProperty.builder()
//                .course(0)
//                .altitude(0)
//                .speed(0.)
//                .satelliteCount(3)
//                .hdop(5.)
//                .inputs(-1)
//                .outputs(0)
//                .driverKeyCode("not defined")
//                .build();
//
//        final Set<ConstraintViolation<LocationDefaultProperty>> violations = validator.validate(givenConfig);
//        assertEquals(1, violations.size());
//        Assert.assertEquals("must be greater than or equal to 0", ConstraintViolationUtil.findFirstMessage(violations));
//    }
//
//    @Test
//    public void configShouldNotBeValidBecauseOfOutputsIsNull() {
//        final LocationDefaultProperty givenConfig = LocationDefaultProperty.builder()
//                .course(0)
//                .altitude(0)
//                .speed(0.)
//                .satelliteCount(3)
//                .hdop(5.)
//                .inputs(0)
//                .driverKeyCode("not defined")
//                .build();
//
//        final Set<ConstraintViolation<LocationDefaultProperty>> violations = validator.validate(givenConfig);
//        assertEquals(1, violations.size());
//        Assert.assertEquals("must not be null", ConstraintViolationUtil.findFirstMessage(violations));
//    }
//
//    @Test
//    public void configShouldNotBeValidBecauseOfOutputsIsNegative() {
//        final LocationDefaultProperty givenConfig = LocationDefaultProperty.builder()
//                .course(0)
//                .altitude(0)
//                .speed(0.)
//                .satelliteCount(3)
//                .hdop(5.)
//                .inputs(0)
//                .outputs(-1)
//                .driverKeyCode("not defined")
//                .build();
//
//        final Set<ConstraintViolation<LocationDefaultProperty>> violations = validator.validate(givenConfig);
//        assertEquals(1, violations.size());
//        Assert.assertEquals("must be greater than or equal to 0", ConstraintViolationUtil.findFirstMessage(violations));
//    }
//
//    @Test
//    public void configShouldNotBeValidBecauseOfDriverKeyCodeIsNull() {
//        final LocationDefaultProperty givenConfig = LocationDefaultProperty.builder()
//                .course(0)
//                .altitude(0)
//                .speed(0.)
//                .satelliteCount(3)
//                .hdop(5.)
//                .inputs(0)
//                .outputs(0)
//                .build();
//
//        final Set<ConstraintViolation<LocationDefaultProperty>> violations = validator.validate(givenConfig);
//        assertEquals(1, violations.size());
//        Assert.assertEquals("must not be blank", ConstraintViolationUtil.findFirstMessage(violations));
//    }
//
//    @Test
//    public void configShouldNotBeValidBecauseOfDriverKeyCodeIsBlank() {
//        final LocationDefaultProperty givenConfig = LocationDefaultProperty.builder()
//                .course(0)
//                .altitude(0)
//                .speed(0.)
//                .satelliteCount(3)
//                .hdop(5.)
//                .inputs(0)
//                .outputs(0)
//                .driverKeyCode("      \n\t")
//                .build();
//
//        final Set<ConstraintViolation<LocationDefaultProperty>> violations = validator.validate(givenConfig);
//        assertEquals(1, violations.size());
//        Assert.assertEquals("must not be blank", ConstraintViolationUtil.findFirstMessage(violations));
//    }
//}
