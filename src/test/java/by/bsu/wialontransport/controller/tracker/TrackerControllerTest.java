package by.bsu.wialontransport.controller.tracker;

import by.bsu.wialontransport.controller.tracker.view.TrackerView;
import by.bsu.wialontransport.crud.dto.Tracker;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public final class TrackerControllerTest {
    private final TrackerController controller = new TrackerController(null);

    @Test
    public void responseViewShouldBeCreated() {
        final Long givenId = 255L;
        final String givenImei = "11112222333344445555";
        final String givenPhoneNumber = "447336934";
        final Tracker givenTracker = Tracker.builder()
                .id(givenId)
                .imei(givenImei)
                .phoneNumber(givenPhoneNumber)
                .build();

        final TrackerView actual = controller.createResponseView(givenTracker);
        final TrackerView expected = new TrackerView(givenId, givenImei, givenPhoneNumber);
        assertEquals(expected, actual);
    }
}
