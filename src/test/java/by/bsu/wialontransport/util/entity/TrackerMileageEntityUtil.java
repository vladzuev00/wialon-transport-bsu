package by.bsu.wialontransport.util.entity;

import by.bsu.wialontransport.crud.entity.TrackerMileageEntity;
import lombok.experimental.UtilityClass;

import static org.junit.Assert.assertEquals;

@UtilityClass
public final class TrackerMileageEntityUtil {

    public static void checkEquals(final TrackerMileageEntity expected, final TrackerMileageEntity actual) {
        assertEquals(expected.getId(), actual.getId());
        assertEquals(expected.getUrban(), actual.getUrban(), 0.);
        assertEquals(expected.getCountry(), actual.getCountry(), 0.);
    }
}
