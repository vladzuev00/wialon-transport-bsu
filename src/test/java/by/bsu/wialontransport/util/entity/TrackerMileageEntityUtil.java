package by.bsu.wialontransport.util.entity;

import by.bsu.wialontransport.crud.entity.TrackerMileageEntity;
import lombok.experimental.UtilityClass;

import java.util.List;

import static java.util.stream.IntStream.range;
import static org.junit.Assert.assertEquals;

@UtilityClass
public final class TrackerMileageEntityUtil {

    public static void checkEquals(final TrackerMileageEntity expected, final TrackerMileageEntity actual) {
        checkEqualsExceptId(expected, actual);
        assertEquals(expected.getId(), actual.getId());
    }

    //TODO: remove
    public static void checkEqualsExceptId(final TrackerMileageEntity expected, final TrackerMileageEntity actual) {
        assertEquals(expected.getUrban(), actual.getUrban(), 0.);
        assertEquals(expected.getCountry(), actual.getCountry(), 0.);
    }

    public static void checkEquals(final List<TrackerMileageEntity> expected, final List<TrackerMileageEntity> actual) {
        assertEquals(expected.size(), actual.size());
        range(0, expected.size()).forEach(i -> checkEquals(expected.get(i), actual.get(i)));
    }
}
