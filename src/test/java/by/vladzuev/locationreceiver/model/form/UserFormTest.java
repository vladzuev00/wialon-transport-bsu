//package by.vladzuev.locationreceiver.model.form;
//
//import by.vladzuev.locationreceiver.base.AbstractSpringBootTest;
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
//public final class UserFormTest extends AbstractSpringBootTest {
//
//    @Autowired
//    private Validator validator;
//
//    @Test
//    public void userFormShouldBeValid() {
//        final UserForm givenUserForm = UserForm.builder()
//                .email("vladzuev.00@mail.ru")
//                .password("password")
//                .confirmedPassword("password")
//                .build();
//
//        final Set<ConstraintViolation<UserForm>> constraintViolations = this.validator.validate(givenUserForm);
//        assertTrue(constraintViolations.isEmpty());
//    }
//
//    @Test
//    public void userFormShouldNotBeValidBecauseOfEmailIsNull() {
//        final UserForm givenUserForm = UserForm.builder()
//                .password("password")
//                .confirmedPassword("password")
//                .build();
//
//        final Set<ConstraintViolation<UserForm>> constraintViolations = this.validator.validate(givenUserForm);
//        assertEquals(1, constraintViolations.size());
//        assertEquals("must not be null", constraintViolations.iterator().next().getMessage());
//    }
//
//    @Test
//    public void userFormShouldNotBeValidBecauseOfEmailIsNotValid() {
//        final UserForm givenUserForm = UserForm.builder()
//                .email("vladzuev.00mail.ru")
//                .password("password")
//                .confirmedPassword("password")
//                .build();
//
//        final Set<ConstraintViolation<UserForm>> constraintViolations = this.validator.validate(givenUserForm);
//        assertEquals(1, constraintViolations.size());
//        assertEquals("not valid email", constraintViolations.iterator().next().getMessage());
//    }
//
//    @Test
//    public void userFormShouldNotBeValidBecauseOfPasswordIsNull() {
//        final UserForm givenUserForm = UserForm.builder()
//                .email("vladzuev.00@mail.ru")
//                .confirmedPassword("password")
//                .build();
//
//        final Set<ConstraintViolation<UserForm>> constraintViolations = this.validator.validate(givenUserForm);
//        assertEquals(1, constraintViolations.size());
//        assertEquals("not valid password", constraintViolations.iterator().next().getMessage());
//    }
//
//    @Test
//    public void userFormShouldNotBeValidBecauseOfPasswordLengthIsLessThanMinimalAllowable() {
//        final UserForm givenUserForm = UserForm.builder()
//                .email("vladzuev.00@mail.ru")
//                .password("pa")
//                .confirmedPassword("password")
//                .build();
//
//        final Set<ConstraintViolation<UserForm>> constraintViolations = this.validator.validate(givenUserForm);
//        assertEquals(1, constraintViolations.size());
//        assertEquals("not valid password", constraintViolations.iterator().next().getMessage());
//    }
//
//    @Test
//    public void userFormShouldNotBeValidBecauseOfPasswordLengthIsMoreThanMaximalAllowable() {
//        final UserForm givenUserForm = UserForm.builder()
//                .email("vladzuev.00@mail.ru")
//                .password("passwordpasswordpasswordpasswordpasswordpasswordpasswordpasswordpasswordpasswordpasswordpas"
//                        + "swordpasswordpasswordpasswordpasswordpasswordpasswordpasswordpasswordpasswordpassword"
//                        + "passwordpasswordpasswordpasswordpasswordpasswordpasswordpasswordpasswordpasswordpassword"
//                        + "passwordpasswordpasswordpasswordpasswordpasswordpasswordpasswordpassword")
//                .confirmedPassword("password")
//                .build();
//
//        final Set<ConstraintViolation<UserForm>> constraintViolations = this.validator.validate(givenUserForm);
//        assertEquals(1, constraintViolations.size());
//        assertEquals("not valid password", constraintViolations.iterator().next().getMessage());
//    }
//
//    @Test
//    public void userFormShouldNotBeValidBecauseOfConfirmedPasswordIsNull() {
//        final UserForm givenUserForm = UserForm.builder()
//                .email("vladzuev.00@mail.ru")
//                .password("password")
//                .build();
//
//        final Set<ConstraintViolation<UserForm>> constraintViolations = this.validator.validate(givenUserForm);
//        assertEquals(1, constraintViolations.size());
//        assertEquals("not valid password", constraintViolations.iterator().next().getMessage());
//    }
//
//    @Test
//    public void userFormShouldNotBeValidBecauseOfConfirmedPasswordIsLessThanMinimalAllowable() {
//        final UserForm givenUserForm = UserForm.builder()
//                .email("vladzuev.00@mail.ru")
//                .password("password")
//                .confirmedPassword("pa")
//                .build();
//
//        final Set<ConstraintViolation<UserForm>> constraintViolations = this.validator.validate(givenUserForm);
//        assertEquals(1, constraintViolations.size());
//        assertEquals("not valid password", constraintViolations.iterator().next().getMessage());
//    }
//
//    @Test
//    public void userFormShouldNotBeValidBecauseOfConfirmedPasswordIsMoreThanMaximalAllowable() {
//        final UserForm givenUserForm = UserForm.builder()
//                .email("vladzuev.00@mail.ru")
//                .password("password")
//                .confirmedPassword("passwordpasswordpasswordpasswordpasswordpasswordpasswordpasswordpasswordpassword"
//                        + "passwordpasswordpasswordpasswordpasswordpasswordpasswordpasswordpasswordpasswordpasswordpassword"
//                        + "passwordpasswordpasswordpasswordpasswordpasswordpasswordpasswordpasswordpasswordpassword"
//                        + "passwordpasswordpasswordpasswordpasswordpasswordpasswordpasswordpassword")
//                .build();
//
//        final Set<ConstraintViolation<UserForm>> constraintViolations = this.validator.validate(givenUserForm);
//        assertEquals(1, constraintViolations.size());
//        assertEquals("not valid password", constraintViolations.iterator().next().getMessage());
//    }
//
//}
