package by.bsu.wialontransport.util.entity;

import by.bsu.wialontransport.crud.entity.CityEntity;
import lombok.experimental.UtilityClass;

import static by.bsu.wialontransport.util.HibernateUtil.isPropertyLoaded;
import static org.junit.Assert.assertEquals;

@UtilityClass
public final class CityEntityUtil {

    public static void checkEquals(final CityEntity expected, final CityEntity actual) {
        assertEquals(expected.getId(), actual.getId());
        assertEquals(expected.getAddress(), actual.getAddress());
        assertEquals(expected.getSearchingCitiesProcess(), actual.getSearchingCitiesProcess());
    }

    public static void checkDeepEquals(final CityEntity expected, final CityEntity actual) {
        assertEquals(expected.getId(), actual.getId());
        AddressEntityUtil.checkEquals(expected.getAddress(), actual.getAddress());
        SearchingCitiesProcessEntityUtil.checkEquals(
                expected.getSearchingCitiesProcess(),
                actual.getSearchingCitiesProcess()
        );
    }

    public static boolean isAddressLoaded(final CityEntity city) {
        return isPropertyLoaded(city, CityEntity::getAddress);
    }

    public static boolean isSearchingCitiesProcessLoaded(final CityEntity city) {
        return isPropertyLoaded(city, CityEntity::getSearchingCitiesProcess);
    }

}
