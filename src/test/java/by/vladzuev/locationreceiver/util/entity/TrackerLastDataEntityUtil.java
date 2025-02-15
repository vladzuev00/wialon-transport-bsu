package by.vladzuev.locationreceiver.util.entity;

import by.vladzuev.locationreceiver.crud.entity.TrackerLastDataEntity;
import lombok.experimental.UtilityClass;

import java.util.List;

import static java.util.stream.IntStream.range;
import static org.junit.Assert.assertEquals;

@UtilityClass
public final class TrackerLastDataEntityUtil {

    public static void checkEquals(final TrackerLastDataEntity expected, final TrackerLastDataEntity actual) {
        assertEquals(expected.getId(), actual.getId());
        assertEquals(expected.getTracker(), actual.getTracker());
        assertEquals(expected.getData(), actual.getData());
    }

    public static void checkEquals(final List<TrackerLastDataEntity> expected, final List<TrackerLastDataEntity> actual) {
        assertEquals(expected.size(), actual.size());
        range(0, expected.size()).forEach(i -> checkEquals(expected.get(i), actual.get(i)));
    }
}
