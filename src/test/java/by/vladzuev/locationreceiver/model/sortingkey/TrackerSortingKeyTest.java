package by.vladzuev.locationreceiver.model.sortingkey;

import by.vladzuev.locationreceiver.crud.dto.Tracker;
import org.junit.Test;

import java.util.Comparator;

import static by.vladzuev.locationreceiver.model.sortingkey.TrackerSortingKey.IMEI;
import static by.vladzuev.locationreceiver.model.sortingkey.TrackerSortingKey.PHONE_NUMBER;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public final class TrackerSortingKeyTest {

    @Test
    public void trackersShouldBeSortedByImei() {
        final Tracker firstGivenTracker = createByImei("11112222333344445555");
        final Tracker secondGivenTracker = createByImei("11112222133344445555");
        final Tracker thirdGivenTracker = createByImei("11112222133344445555");

        final Comparator<Tracker> comparator = IMEI.getComparator();
        assertTrue(comparator.compare(firstGivenTracker, secondGivenTracker) > 0);
        assertTrue(comparator.compare(secondGivenTracker, firstGivenTracker) < 0);
        assertEquals(0, comparator.compare(secondGivenTracker, thirdGivenTracker));
    }

    @Test
    public void trackersShouldBeSortedByPhoneNumber() {
        final Tracker firstGivenTracker = createByPhoneNumber("447336934");
        final Tracker secondGivenTracker = createByPhoneNumber("447336935");
        final Tracker thirdGivenTracker = createByPhoneNumber("447336934");

        final Comparator<Tracker> comparator = PHONE_NUMBER.getComparator();
        assertTrue(comparator.compare(firstGivenTracker, secondGivenTracker) < 0);
        assertTrue(comparator.compare(secondGivenTracker, firstGivenTracker) > 0);
        assertEquals(0, comparator.compare(firstGivenTracker, thirdGivenTracker));
    }

    private static Tracker createByPhoneNumber(final String phoneNumber) {
        return Tracker.builder()
                .phoneNumber(phoneNumber)
                .build();
    }

    private static Tracker createByImei(final String imei) {
        return Tracker.builder()
                .imei(imei)
                .build();
    }
}
