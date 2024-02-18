package by.bsu.wialontransport.model.form;

import by.bsu.wialontransport.base.AbstractSpringBootTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.util.Set;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public final class ChangePasswordFormTest extends AbstractSpringBootTest {

    @Autowired
    private Validator validator;

    @Test
    public void changePasswordFormShouldBeValid() {
        final ChangePasswordForm givenForm = ChangePasswordForm.builder()
                .oldPassword("old-password")
                .newPassword("new-password")
                .confirmedNewPassword("new-password")
                .build();

        final Set<ConstraintViolation<ChangePasswordForm>> constraintViolations = this.validator.validate(givenForm);
        assertTrue(constraintViolations.isEmpty());
    }

    @Test
    public void changePasswordFormShouldNotBeValidBecauseOfOldPasswordIsNull() {
        final ChangePasswordForm givenForm = ChangePasswordForm.builder()
                .newPassword("new-password")
                .confirmedNewPassword("new-password")
                .build();

        final Set<ConstraintViolation<ChangePasswordForm>> constraintViolations = this.validator.validate(givenForm);
        assertEquals(1, constraintViolations.size());
        assertEquals("not valid password", constraintViolations.iterator().next().getMessage());
    }

    @Test
    public void changePasswordFormShouldNotBeValidBecauseOfOldPasswordLengthIsLessThanMinimalAllowable() {
        final ChangePasswordForm givenForm = ChangePasswordForm.builder()
                .oldPassword("ol")
                .newPassword("new-password")
                .confirmedNewPassword("new-password")
                .build();

        final Set<ConstraintViolation<ChangePasswordForm>> constraintViolations = this.validator.validate(givenForm);
        assertEquals(1, constraintViolations.size());
        assertEquals("not valid password", constraintViolations.iterator().next().getMessage());
    }

    @Test
    public void changePasswordFormShouldNotBeValidBecauseOfOldPasswordLengthIsMoreThanMaximalAllowable() {
        final ChangePasswordForm givenForm = ChangePasswordForm.builder()
                .oldPassword("old-passwordold-passwordold-passwordold-passwordold-passwordold-passwordold-passwordold-"
                        + "passwordold-passwordold-passwordold-passwordold-passwordold-passwordold-passwordold-"
                        + "passwordold-passwordold-passwordold-passwordold-passwordold-passwordold-passwordold-"
                        + "passwordold-passwordold-password")
                .newPassword("new-password")
                .confirmedNewPassword("new-password")
                .build();

        final Set<ConstraintViolation<ChangePasswordForm>> constraintViolations = this.validator.validate(givenForm);
        assertEquals(1, constraintViolations.size());
        assertEquals("not valid password", constraintViolations.iterator().next().getMessage());
    }

    @Test
    public void changePasswordFormShouldNotBeValidBecauseOfNewPasswordIsNull() {
        final ChangePasswordForm givenForm = ChangePasswordForm.builder()
                .oldPassword("old-password")
                .confirmedNewPassword("new-password")
                .build();

        final Set<ConstraintViolation<ChangePasswordForm>> constraintViolations = this.validator.validate(givenForm);
        assertEquals(1, constraintViolations.size());
        assertEquals("not valid password", constraintViolations.iterator().next().getMessage());
    }

    @Test
    public void changePasswordFormShouldNotBeValidBecauseOfNewPasswordLengthIsLessThanMinimalAllowable() {
        final ChangePasswordForm givenForm = ChangePasswordForm.builder()
                .oldPassword("old-password")
                .newPassword("ne")
                .confirmedNewPassword("new-password")
                .build();

        final Set<ConstraintViolation<ChangePasswordForm>> constraintViolations = this.validator.validate(givenForm);
        assertEquals(1, constraintViolations.size());
        assertEquals("not valid password", constraintViolations.iterator().next().getMessage());
    }

    @Test
    public void changePasswordFormShouldNotBeValidBecauseOfNewPasswordLengthIsMoreThanMaximalAllowable() {
        final ChangePasswordForm givenForm = ChangePasswordForm.builder()
                .oldPassword("old-password")
                .newPassword("new-passwordnew-passwordnew-passwordnew-passwordnew-passwordnew-passwordnew-passwordnew-"
                        + "passwordnew-passwordnew-passwordnew-passwordnew-passwordnew-passwordnew-passwordnew-"
                        + "passwordnew-passwordnew-passwordnew-passwordnew-passwordnew-passwordnew-passwordnew-password")
                .confirmedNewPassword("new-password")
                .build();

        final Set<ConstraintViolation<ChangePasswordForm>> constraintViolations = this.validator.validate(givenForm);
        assertEquals(1, constraintViolations.size());
        assertEquals("not valid password", constraintViolations.iterator().next().getMessage());
    }

    @Test
    public void changePasswordFormShouldNotBeValidBecauseOfConfirmedNewPasswordIsNull() {
        final ChangePasswordForm givenForm = ChangePasswordForm.builder()
                .oldPassword("old-password")
                .newPassword("new-password")
                .build();

        final Set<ConstraintViolation<ChangePasswordForm>> constraintViolations = this.validator.validate(givenForm);
        assertEquals(1, constraintViolations.size());
        assertEquals("not valid password", constraintViolations.iterator().next().getMessage());
    }

    @Test
    public void changePasswordFormShouldNotBeValidBecauseOfConfirmedNewPasswordLengthIsLessThanMinimalAllowable() {
        final ChangePasswordForm givenForm = ChangePasswordForm.builder()
                .oldPassword("old-password")
                .newPassword("new-password")
                .confirmedNewPassword("ne")
                .build();

        final Set<ConstraintViolation<ChangePasswordForm>> constraintViolations = this.validator.validate(givenForm);
        assertEquals(1, constraintViolations.size());
        assertEquals("not valid password", constraintViolations.iterator().next().getMessage());
    }

    @Test
    public void changePasswordFormShouldNotBeValidBecauseOfConfirmedNewPasswordLengthIsMoreThanMaximalAllowable() {
        final ChangePasswordForm givenForm = ChangePasswordForm.builder()
                .oldPassword("old-password")
                .newPassword("new-password")
                .confirmedNewPassword("new-passwordnew-passwordnew-passwordnew-passwordnew-passwordnew-passwordnew-"
                        + "passwordnew-passwordnew-passwordnew-passwordnew-passwordnew-passwordnew-passwordnew"
                        + "-passwordnew-passwordnew-passwordnew-passwordnew-passwordnew-password")
                .build();

        final Set<ConstraintViolation<ChangePasswordForm>> constraintViolations = this.validator.validate(givenForm);
        assertEquals(1, constraintViolations.size());
        assertEquals("not valid password", constraintViolations.iterator().next().getMessage());
    }

}
