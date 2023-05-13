package by.bsu.wialontransport.util;

import by.bsu.wialontransport.crud.dto.SearchingCitiesProcess;
import by.bsu.wialontransport.crud.entity.AddressEntity;
import by.bsu.wialontransport.crud.entity.CityEntity;
import by.bsu.wialontransport.crud.entity.SearchingCitiesProcessEntity;
import lombok.experimental.UtilityClass;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

@UtilityClass
public final class EntityUtil {

    public static void checkEquals(final SearchingCitiesProcessEntity expected,
                                   final SearchingCitiesProcessEntity actual) {
        assertEquals(expected.getId(), actual.getId());
        assertEquals(expected.getBounds(), actual.getBounds());
        assertEquals(expected.getSearchStep(), actual.getSearchStep(), 0.);
        assertEquals(expected.getTotalPoints(), actual.getTotalPoints());
        assertEquals(expected.getHandledPoints(), actual.getHandledPoints());
        assertSame(expected.getStatus(), actual.getStatus());
    }

    public static void checkEquals(final AddressEntity expected, final AddressEntity actual) {
        assertEquals(expected.getId(), actual.getId());
        assertEquals(expected.getBoundingBox(), actual.getBoundingBox());
        assertEquals(expected.getCenter(), actual.getCenter());
        assertEquals(expected.getCityName(), actual.getCityName());
        assertEquals(expected.getCountryName(), actual.getCountryName());
        assertEquals(expected.getGeometry(), actual.getGeometry());
    }

    public static void checkEquals(final CityEntity expected, final CityEntity actual) {
        assertEquals(expected.getId(), actual.getId());
        assertEquals(expected.getAddress(), actual.getAddress());
        assertEquals(expected.getSearchingCitiesProcess(), actual.getSearchingCitiesProcess());
    }

    public static void checkDeepEquals(final CityEntity expected, final CityEntity actual) {
        assertEquals(expected.getId(), actual.getId());
        checkEquals(expected.getAddress(), actual.getAddress());
        checkEquals(expected.getSearchingCitiesProcess(), actual.getSearchingCitiesProcess());
    }

    public static SearchingCitiesProcess createSearchingCitiesProcess(final Long id) {
        return SearchingCitiesProcess.builder()
                .id(id)
                .build();
    }
}
