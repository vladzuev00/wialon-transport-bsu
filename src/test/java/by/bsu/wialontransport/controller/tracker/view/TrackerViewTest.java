package by.bsu.wialontransport.controller.tracker.view;

import by.bsu.wialontransport.base.AbstractSpringBootTest;
import lombok.Builder;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.util.Optional;
import java.util.Set;

import static by.bsu.wialontransport.util.ConstraintViolationUtil.findFirstMessage;
import static java.util.Optional.empty;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public final class TrackerViewTest extends AbstractSpringBootTest {

    @Autowired
    private Validator validator;

    @Test
    public void viewShouldBeValid() {
        final TrackerView givenView = new TestTrackerView("99994444333366665456", "448664385");

        final Set<ConstraintViolation<TrackerView>> constraintViolations = validator.validate(givenView);
        assertTrue(constraintViolations.isEmpty());
    }

    @Test
    public void viewShouldNotBeValidBecauseOfImeiIsNull() {
        final TrackerView givenView = TestTrackerView.builder()
                .phoneNumber("448664385")
                .build();

        final Set<ConstraintViolation<TrackerView>> constraintViolations = validator.validate(givenView);
        assertEquals(1, constraintViolations.size());
        assertEquals("Invalid imei", findFirstMessage(constraintViolations));
    }

    @Test
    public void viewShouldNotBeValidBecauseOfImeiIsNotValid() {
        final TrackerView givenView = new TestTrackerView("9999444433336666545", "448664385");

        final Set<ConstraintViolation<TrackerView>> constraintViolations = validator.validate(givenView);
        assertEquals(1, constraintViolations.size());
        assertEquals("Invalid imei", findFirstMessage(constraintViolations));
    }

    @Test
    public void viewShouldNotBeValidBecauseOfImeiShouldBeUnique() {
        final TrackerView givenView = new TestTrackerView("11112222333344445555", "448664385");

        final Set<ConstraintViolation<TrackerView>> constraintViolations = validator.validate(givenView);
        assertEquals(1, constraintViolations.size());
        assertEquals("Imei should be unique", findFirstMessage(constraintViolations));
    }

    @Test
    public void viewShouldNotBeValidBecauseOfPhoneNumberIsNull() {
        final TrackerView givenView = TestTrackerView.builder()
                .imei("99994444333366665456")
                .build();

        final Set<ConstraintViolation<TrackerView>> constraintViolations = validator.validate(givenView);
        assertEquals(1, constraintViolations.size());
        assertEquals("Invalid phone number", findFirstMessage(constraintViolations));
    }

    @Test
    public void viewShouldNotBeValidBecauseOfPhoneNumberIsNotValid() {
        final TrackerView givenView = new TestTrackerView("99994444333366665456", "44866438");

        final Set<ConstraintViolation<TrackerView>> constraintViolations = validator.validate(givenView);
        assertEquals(1, constraintViolations.size());
        assertEquals("Invalid phone number", findFirstMessage(constraintViolations));
    }

    @Test
    public void viewShouldNotBeValidBecauseOfPhoneNumberShouldBeUnique() {
        final TrackerView givenView = new TestTrackerView("99994444333366665456", "447336934");

        final Set<ConstraintViolation<TrackerView>> constraintViolations = validator.validate(givenView);
        assertEquals(1, constraintViolations.size());
        assertEquals("Phone number should be unique", findFirstMessage(constraintViolations));
    }

    private static final class TestTrackerView extends TrackerView {

        @Builder
        public TestTrackerView(final String imei, final String phoneNumber) {
            super(imei, phoneNumber);
        }

        @Override
        public Optional<Long> findId() {
            return empty();
        }
    }
}