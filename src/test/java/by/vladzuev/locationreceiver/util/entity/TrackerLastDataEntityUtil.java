package by.vladzuev.locationreceiver.util.entity;

import by.vladzuev.locationreceiver.crud.entity.LastLocationEntity;
import lombok.experimental.UtilityClass;

import java.util.List;

import static java.util.stream.IntStream.range;
import static org.junit.Assert.assertEquals;

@UtilityClass
public final class TrackerLastDataEntityUtil {

    public static void checkEquals(final LastLocationEntity expected, final LastLocationEntity actual) {
        assertEquals(expected.getId(), actual.getId());
        assertEquals(expected.getTracker(), actual.getTracker());
        assertEquals(expected.getLocation(), actual.getLocation());
    }

    public static void checkEquals(final List<LastLocationEntity> expected, final List<LastLocationEntity> actual) {
        assertEquals(expected.size(), actual.size());
        range(0, expected.size()).forEach(i -> checkEquals(expected.get(i), actual.get(i)));
    }
}
