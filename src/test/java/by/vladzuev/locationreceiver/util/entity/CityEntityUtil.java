package by.vladzuev.locationreceiver.util.entity;

import by.vladzuev.locationreceiver.crud.entity.CityEntity;
import by.vladzuev.locationreceiver.util.HibernateUtil;
import lombok.experimental.UtilityClass;
import org.junit.Assert;

import static org.junit.Assert.assertEquals;

@UtilityClass
public final class CityEntityUtil {

    public static void assertEquals(final CityEntity expected, final CityEntity actual) {
        Assert.assertEquals(expected.getId(), actual.getId());
        Assert.assertEquals(expected.getAddress(), actual.getAddress());
//        assertEquals(expected.getSearchingCitiesProcess(), actual.getSearchingCitiesProcess());
    }

    public static void checkDeepEquals(final CityEntity expected, final CityEntity actual) {
        Assert.assertEquals(expected.getId(), actual.getId());
        AddressEntityUtil.assertEquals(expected.getAddress(), actual.getAddress());
//        SearchingCitiesProcessEntityUtil.checkEquals(
//                expected.getSearchingCitiesProcess(),
//                actual.getSearchingCitiesProcess()
//        );
    }

    public static boolean isAddressFetched(final CityEntity city) {
        return HibernateUtil.isPropertyFetched(city, CityEntity::getAddress);
    }

    public static boolean isSearchingCitiesProcessFetched(final CityEntity city) {
        return false;
//        return HibernateUtil.isPropertyFetched(city, CityEntity::getSearchingCitiesProcess);
    }

}
