package by.bsu.wialontransport.crud.service;

import by.bsu.wialontransport.base.AbstractContextTest;
import by.bsu.wialontransport.crud.dto.Tracker;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public final class TrackerServiceTest extends AbstractContextTest {

    @Autowired
    private TrackerService service;

    @Test
    public void trackerShouldBeFoundByImei() {
        final Tracker actual = this.service.findByImei("11112222333344445555").orElseThrow();
        final Tracker expected = Tracker.builder()
                .id(255L)
                .imei("11112222333344445555")
                .password("password")
                .phoneNumber("447336934")
                .build();
        assertEquals(expected, actual);
    }

    @Test
    public void trackerShouldNotBeFoundByImei() {
        final Optional<Tracker> optionalFoundTracker = this.service.findByImei("00000000000000000000");
        assertTrue(optionalFoundTracker.isEmpty());
    }
}
