package by.bsu.wialontransport.controller.registration.model;

import by.bsu.wialontransport.base.AbstractContextTest;
import by.bsu.wialontransport.model.form.UserForm;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public final class UserFormTest extends AbstractContextTest {

    @Autowired
    private Validator validator;

    @Test
    public void userFormShouldBeValid() {
        final UserForm givenUserForm = UserForm.builder()
                .email("vladzuev.00@mail.ru")
                .password("password")
                .confirmedPassword("password")
                .build();

        final Set<ConstraintViolation<UserForm>> constraintViolations = this.validator.validate(givenUserForm);
        assertTrue(constraintViolations.isEmpty());
    }

    @Test
    public void userFormShouldNotBeValidBecauseOfEmailIsNull() {
        final UserForm givenUserForm = UserForm.builder()
                .password("password")
                .confirmedPassword("password")
                .build();

        final Set<ConstraintViolation<UserForm>> constraintViolations = this.validator.validate(givenUserForm);
        assertEquals(1, constraintViolations.size());
        assertEquals("must not be null", constraintViolations.iterator().next().getMessage());
    }

    @Test
    public void userFormShouldNotBeValidBecauseOfEmailIsNotValid() {
        final UserForm givenUserForm = UserForm.builder()
                .email("vladzuev.00mail.ru")
                .password("password")
                .confirmedPassword("password")
                .build();

        final Set<ConstraintViolation<UserForm>> constraintViolations = this.validator.validate(givenUserForm);
        assertEquals(1, constraintViolations.size());
        assertEquals("not valid email", constraintViolations.iterator().next().getMessage());
    }

    @Test
    public void userFormShouldNotBeValidBecauseOfPasswordIsNull() {
        final UserForm givenUserForm = UserForm.builder()
                .email("vladzuev.00@mail.ru")
                .confirmedPassword("password")
                .build();

        final Set<ConstraintViolation<UserForm>> constraintViolations = this.validator.validate(givenUserForm);
        assertEquals(1, constraintViolations.size());
        assertEquals("must not be null", constraintViolations.iterator().next().getMessage());
    }

    @Test
    public void userFormShouldNotBeValidBecauseOfPasswordLengthIsLessThanMinimalAllowable() {
        final UserForm givenUserForm = UserForm.builder()
                .email("vladzuev.00@mail.ru")
                .password("pass")
                .confirmedPassword("password")
                .build();

        final Set<ConstraintViolation<UserForm>> constraintViolations = this.validator.validate(givenUserForm);
        assertEquals(1, constraintViolations.size());
        assertEquals("size must be between 5 and 30", constraintViolations.iterator().next().getMessage());
    }

    @Test
    public void userFormShouldNotBeValidBecauseOfPasswordLengthIsMoreThanMaximalAllowable() {
        final UserForm givenUserForm = UserForm.builder()
                .email("vladzuev.00@mail.ru")
                .password("passwordpasswordpasswordpasswordpasswordpasswordpassword")
                .confirmedPassword("password")
                .build();

        final Set<ConstraintViolation<UserForm>> constraintViolations = this.validator.validate(givenUserForm);
        assertEquals(1, constraintViolations.size());
        assertEquals("size must be between 5 and 30", constraintViolations.iterator().next().getMessage());
    }

    @Test
    public void userFormShouldNotBeValidBecauseOfConfirmedPasswordIsNull() {
        final UserForm givenUserForm = UserForm.builder()
                .email("vladzuev.00@mail.ru")
                .password("password")
                .build();

        final Set<ConstraintViolation<UserForm>> constraintViolations = this.validator.validate(givenUserForm);
        assertEquals(1, constraintViolations.size());
        assertEquals("must not be null", constraintViolations.iterator().next().getMessage());
    }

    @Test
    public void userFormShouldNotBeValidBecauseOfConfirmedPasswordIsLessThanMinimalAllowable() {
        final UserForm givenUserForm = UserForm.builder()
                .email("vladzuev.00@mail.ru")
                .password("password")
                .confirmedPassword("pass")
                .build();

        final Set<ConstraintViolation<UserForm>> constraintViolations = this.validator.validate(givenUserForm);
        assertEquals(1, constraintViolations.size());
        assertEquals("size must be between 5 and 30", constraintViolations.iterator().next().getMessage());
    }

    @Test
    public void userFormShouldNotBeValidBecauseOfConfirmedPasswordIsMoreThanMaximalAllowable() {
        final UserForm givenUserForm = UserForm.builder()
                .email("vladzuev.00@mail.ru")
                .password("password")
                .confirmedPassword("passwordpasswordpasswordpasswordpasswordpasswordpassword")
                .build();

        final Set<ConstraintViolation<UserForm>> constraintViolations = this.validator.validate(givenUserForm);
        assertEquals(1, constraintViolations.size());
        assertEquals("size must be between 5 and 30", constraintViolations.iterator().next().getMessage());
    }

}
