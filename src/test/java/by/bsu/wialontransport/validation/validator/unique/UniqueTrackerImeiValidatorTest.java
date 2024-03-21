package by.bsu.wialontransport.validation.validator.unique;

import by.bsu.wialontransport.crud.service.TrackerService;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.same;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public final class UniqueTrackerImeiValidatorTest {
    private final UniqueTrackerImeiValidator validator = new UniqueTrackerImeiValidator(null);

    @Test
    public void imeiShouldExist() {
        final String givenImei = "11112222333344445555";
        final TrackerService givenService = mock(TrackerService.class);

        when(givenService.isExistByImei(same(givenImei))).thenReturn(true);

        final boolean exists = validator.isExist(givenService, givenImei);
        assertTrue(exists);
    }

    @Test
    public void imeiShouldNotExist() {
        final String givenImei = "11112222333344445555";
        final TrackerService givenService = mock(TrackerService.class);

        when(givenService.isExistByImei(same(givenImei))).thenReturn(false);

        final boolean exists = validator.isExist(givenService, givenImei);
        assertFalse(exists);
    }
}
