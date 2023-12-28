package by.bsu.wialontransport.util.entity;

import by.bsu.wialontransport.crud.entity.AddressEntity;
import lombok.experimental.UtilityClass;

import static org.junit.Assert.assertEquals;

@UtilityClass
public final class AddressEntityUtil {

    public static void checkEquals(final AddressEntity expected, final AddressEntity actual) {
        assertEquals(expected.getId(), actual.getId());
        assertEquals(expected.getBoundingBox(), actual.getBoundingBox());
        assertEquals(expected.getCenter(), actual.getCenter());
        assertEquals(expected.getCityName(), actual.getCityName());
        assertEquals(expected.getCountryName(), actual.getCountryName());
        assertEquals(expected.getGeometry(), actual.getGeometry());
    }

}
