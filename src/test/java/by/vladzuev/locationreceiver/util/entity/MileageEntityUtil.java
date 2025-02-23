package by.vladzuev.locationreceiver.util.entity;

import by.vladzuev.locationreceiver.crud.entity.MileageEntity;
import lombok.experimental.UtilityClass;
import org.junit.Assert;

import java.util.List;

import static java.util.stream.IntStream.range;

@UtilityClass
public final class MileageEntityUtil {

    public static void assertEquals(final MileageEntity expected, final MileageEntity actual) {
        checkEqualsExceptId(expected, actual);
        Assert.assertEquals(expected.getId(), actual.getId());
    }

    //TODO: remove
    public static void checkEqualsExceptId(final MileageEntity expected, final MileageEntity actual) {
        Assert.assertEquals(expected.getUrban(), actual.getUrban(), 0.);
        Assert.assertEquals(expected.getCountry(), actual.getCountry(), 0.);
    }

    public static void assertEquals(final List<MileageEntity> expected, final List<MileageEntity> actual) {
        Assert.assertEquals(expected.size(), actual.size());
        range(0, expected.size()).forEach(i -> assertEquals(expected.get(i), actual.get(i)));
    }
}
