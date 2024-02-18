package by.bsu.wialontransport.model.form;

import by.bsu.wialontransport.base.AbstractSpringBootTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public final class TrackerFormTest extends AbstractSpringBootTest {

    @Autowired
    private Validator validator;

    @Test
    public void trackerFormShouldBeValid() {
        final TrackerForm givenForm = TrackerForm.builder()
                .imei("11112222333344445555")
                .password("password")
                .phoneNumber("447336934")
                .build();

        final Set<ConstraintViolation<TrackerForm>> constraintViolations = this.validator.validate(givenForm);
        assertTrue(constraintViolations.isEmpty());
    }

    @Test
    public void trackerFormShouldNotBeValidBecauseOfImeiIsNull() {
        final TrackerForm givenForm = TrackerForm.builder()
                .password("password")
                .phoneNumber("447336934")
                .build();

        final Set<ConstraintViolation<TrackerForm>> constraintViolations = this.validator.validate(givenForm);
        assertEquals(1, constraintViolations.size());
        assertEquals("must not be null", constraintViolations.iterator().next().getMessage());
    }

    @Test
    public void trackerFormShouldNotBeValidBecauseOfImeiDoesNotMatchRegex() {
        final TrackerForm givenForm = TrackerForm.builder()
                .imei("1111222233334444555")
                .password("password")
                .phoneNumber("447336934")
                .build();

        final Set<ConstraintViolation<TrackerForm>> constraintViolations = this.validator.validate(givenForm);
        assertEquals(1, constraintViolations.size());
        assertEquals("should contain only 20 digits", constraintViolations.iterator().next().getMessage());
    }

    @Test
    public void trackerFormShouldNotBeValidBecauseOfPasswordIsNull() {
        final TrackerForm givenForm = TrackerForm.builder()
                .imei("11112222333344445555")
                .phoneNumber("447336934")
                .build();

        final Set<ConstraintViolation<TrackerForm>> constraintViolations = this.validator.validate(givenForm);
        assertEquals(1, constraintViolations.size());
        assertEquals("not valid password", constraintViolations.iterator().next().getMessage());
    }

    @Test
    public void trackerFormShouldNotBeValidBecauseOfPasswordLengthIsLessThanMinimalAllowable() {
        final TrackerForm givenForm = TrackerForm.builder()
                .imei("11112222333344445555")
                .password("pa")
                .phoneNumber("447336934")
                .build();

        final Set<ConstraintViolation<TrackerForm>> constraintViolations = this.validator.validate(givenForm);
        assertEquals(1, constraintViolations.size());
        assertEquals("not valid password", constraintViolations.iterator().next().getMessage());
    }

    @Test
    public void trackerFormShouldNotBeValidBecauseOfPasswordLengthIsMoreThanMaximalAllowable() {
        final TrackerForm givenForm = TrackerForm.builder()
                .imei("11112222333344445555")
                .password("passwordpasswordvpasswordpasswordpasswordpasswordpasswordpasswordpasswordpasswordpass"
                        + "wordpasswordpasswordpasswordpasswordpasswordpasswordpasswordpasswordpasswordpasswordpassword"
                        + "passwordpasswordpasswordpasswordpasswordpasswordpasswordpasswordpasswordpasswordpassword"
                        + "passwordpasswordpasswordpasswordpasswordpasswordpasswordpasswordpasswordpasswordpassword"
                        + "passwordpassword")
                .phoneNumber("447336934")
                .build();

        final Set<ConstraintViolation<TrackerForm>> constraintViolations = this.validator.validate(givenForm);
        assertEquals(1, constraintViolations.size());
        assertEquals("not valid password", constraintViolations.iterator().next().getMessage());
    }

    @Test
    public void trackerFormShouldNotBeValidBecauseOfPhoneNumberIsNull() {
        final TrackerForm givenForm = TrackerForm.builder()
                .imei("11112222333344445555")
                .password("password")
                .build();

        final Set<ConstraintViolation<TrackerForm>> constraintViolations = this.validator.validate(givenForm);
        assertEquals(1, constraintViolations.size());
        assertEquals("must not be null", constraintViolations.iterator().next().getMessage());
    }

    @Test
    public void trackerFormShouldNotBeValidBecauseOfPhoneNumberDoesNotMatchRegex() {
        final TrackerForm givenForm = TrackerForm.builder()
                .imei("11112222333344445555")
                .password("password")
                .phoneNumber("44733693")
                .build();

        final Set<ConstraintViolation<TrackerForm>> constraintViolations = this.validator.validate(givenForm);
        assertEquals(1, constraintViolations.size());
        assertEquals("should contain only 9 digits", constraintViolations.iterator().next().getMessage());
    }

}
