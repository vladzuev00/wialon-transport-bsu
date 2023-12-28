package by.bsu.wialontransport.util.entity;

import by.bsu.wialontransport.crud.entity.SearchingCitiesProcessEntity;
import lombok.experimental.UtilityClass;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

@UtilityClass
public class SearchingCitiesProcessEntityUtil {

    public static void checkEquals(final SearchingCitiesProcessEntity expected,
                                   final SearchingCitiesProcessEntity actual) {
        assertEquals(expected.getId(), actual.getId());
        assertEquals(expected.getBounds(), actual.getBounds());
        assertEquals(expected.getSearchStep(), actual.getSearchStep(), 0.);
        assertEquals(expected.getTotalPoints(), actual.getTotalPoints());
        assertEquals(expected.getHandledPoints(), actual.getHandledPoints());
        assertSame(expected.getStatus(), actual.getStatus());
    }
}
