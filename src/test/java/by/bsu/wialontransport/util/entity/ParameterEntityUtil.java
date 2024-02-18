package by.bsu.wialontransport.util.entity;

import by.bsu.wialontransport.crud.entity.ParameterEntity;
import lombok.experimental.UtilityClass;

import java.util.List;
import java.util.stream.IntStream;

import static by.bsu.wialontransport.util.HibernateUtil.isPropertyFetched;
import static java.util.stream.IntStream.range;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

@UtilityClass
public final class ParameterEntityUtil {

    public static void checkEquals(final ParameterEntity expected, final ParameterEntity actual) {
        assertEquals(expected.getId(), actual.getId());
        assertEquals(expected.getName(), actual.getName());
        assertSame(expected.getType(), actual.getType());
        assertEquals(expected.getValue(), actual.getValue());
        assertEquals(expected.getData(), actual.getData());
    }

    public static void checkEquals(final List<ParameterEntity> expected, final List<ParameterEntity> actual) {
        assertEquals(expected.size(), actual.size());
        range(0, expected.size()).forEach(i -> checkEquals(expected.get(i), actual.get(i)));
    }

    public static boolean isDataFetched(final ParameterEntity entity) {
        return isPropertyFetched(entity, ParameterEntity::getData);
    }
}
