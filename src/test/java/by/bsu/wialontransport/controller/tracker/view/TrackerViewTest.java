package by.bsu.wialontransport.controller.tracker.view;

import by.bsu.wialontransport.crud.dto.Tracker;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public final class TrackerViewTest {

    @Test
    public void viewShouldBeCreated() {
        final Long givenId = 255L;
        final String givenImei = "11112222333344445555";
        final String givenPhoneNumber = "447336934";
        final Tracker givenTracker = Tracker.builder()
                .id(givenId)
                .imei(givenImei)
                .phoneNumber(givenPhoneNumber)
                .build();

        final TrackerView actual = new TrackerView(givenTracker);
        final TrackerView expected = new TrackerView(givenId, givenImei, givenPhoneNumber);
        assertEquals(expected, actual);
    }
}
