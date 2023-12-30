package by.bsu.wialontransport.util.entity;

import by.bsu.wialontransport.crud.entity.ParameterEntity;
import lombok.experimental.UtilityClass;

import static by.bsu.wialontransport.util.HibernateUtil.isPropertyFetched;
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

    public static boolean isDataFetched(final ParameterEntity entity) {
        return isPropertyFetched(entity, ParameterEntity::getData);
    }
}
