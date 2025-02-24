package by.vladzuev.locationreceiver.util.entity;

import by.vladzuev.locationreceiver.crud.entity.ParameterEntity;
import by.vladzuev.locationreceiver.util.HibernateUtil;
import lombok.experimental.UtilityClass;
import org.junit.Assert;

import java.util.List;

import static java.util.stream.IntStream.range;
import static org.junit.Assert.assertSame;

@UtilityClass
public final class ParameterEntityUtil {

    public static void assertEquals(final ParameterEntity expected, final ParameterEntity actual) {
        Assert.assertEquals(expected.getId(), actual.getId());
        Assert.assertEquals(expected.getName(), actual.getName());
        assertSame(expected.getType(), actual.getType());
        Assert.assertEquals(expected.getValue(), actual.getValue());
        Assert.assertEquals(expected.getLocation(), actual.getLocation());
    }

    public static void assertEquals(final List<ParameterEntity> expected, final List<ParameterEntity> actual) {
        Assert.assertEquals(expected.size(), actual.size());
        range(0, expected.size()).forEach(i -> assertEquals(expected.get(i), actual.get(i)));
    }

    public static boolean isDataFetched(final ParameterEntity entity) {
        return HibernateUtil.isPropertyFetched(entity, ParameterEntity::getLocation);
    }
}
