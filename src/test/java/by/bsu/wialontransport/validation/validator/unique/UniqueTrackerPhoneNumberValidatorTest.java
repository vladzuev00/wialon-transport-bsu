package by.bsu.wialontransport.validation.validator.unique;

import by.bsu.wialontransport.crud.service.TrackerService;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.same;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public final class UniqueTrackerPhoneNumberValidatorTest {
    private final UniqueTrackerPhoneNumberValidator validator = new UniqueTrackerPhoneNumberValidator(null);

    @Test
    public void phoneNumberShouldExist() {
//        final String givenPhoneNumber = "447336934";
//        final TrackerService givenService = mock(TrackerService.class);
//
//        when(givenService.isExistByPhoneNumber(same(givenPhoneNumber))).thenReturn(true);
//
//        final boolean exists = validator.isExist(givenService, givenPhoneNumber);
//        assertTrue(exists);
        throw new RuntimeException();
    }

    @Test
    public void phoneNumberShouldNotExist() {
//        final String givenPhoneNumber = "447336934";
//        final TrackerService givenService = mock(TrackerService.class);
//
//        when(givenService.isExistByPhoneNumber(same(givenPhoneNumber))).thenReturn(false);
//
//        final boolean exists = validator.isExist(givenService, givenPhoneNumber);
//        assertFalse(exists);
        throw new RuntimeException();
    }
}
