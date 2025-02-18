package by.vladzuev.locationreceiver.util.entity;

import by.vladzuev.locationreceiver.crud.entity.AddressEntity;
import lombok.experimental.UtilityClass;
import org.junit.Assert;

import static org.junit.Assert.assertEquals;

@UtilityClass
public final class AddressEntityUtil {

    public static void assertEquals(final AddressEntity expected, final AddressEntity actual) {
        Assert.assertEquals(expected.getId(), actual.getId());
        Assert.assertEquals(expected.getBoundingBox(), actual.getBoundingBox());
        Assert.assertEquals(expected.getCenter(), actual.getCenter());
        Assert.assertEquals(expected.getCityName(), actual.getCityName());
        Assert.assertEquals(expected.getCountryName(), actual.getCountryName());
        Assert.assertEquals(expected.getGeometry(), actual.getGeometry());
    }

}
