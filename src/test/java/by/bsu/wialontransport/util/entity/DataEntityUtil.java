package by.bsu.wialontransport.util.entity;

import by.bsu.wialontransport.crud.entity.DataEntity;
import by.bsu.wialontransport.crud.entity.ParameterEntity;
import lombok.experimental.UtilityClass;

import java.util.Collection;
import java.util.List;

import static by.bsu.wialontransport.util.CollectionUtil.areAllMatch;
import static by.bsu.wialontransport.util.HibernateUtil.isPropertyFetched;
import static org.junit.Assert.*;

//TODO: refactor
@UtilityClass
public final class DataEntityUtil {

    public static void checkEquals(final DataEntity expected, final DataEntity actual) {
        checkEqualsExceptIdAndParameters(expected, actual);
        assertEquals(expected.getId(), actual.getId());
        checkEqualsWithoutOrder(expected.getParameters(), actual.getParameters());
    }

    //TODO: remove
    public static void checkEqualsExceptIdAndParameters(final DataEntity expected, final DataEntity actual) {
        assertEquals(expected.getDateTime(), actual.getDateTime());
        assertEquals(expected.getCoordinate(), actual.getCoordinate());
        assertEquals(expected.getSpeed(), actual.getSpeed(), 0.);
        assertEquals(expected.getCourse(), actual.getCourse());
        assertEquals(expected.getAltitude(), actual.getAltitude());
        assertEquals(expected.getAmountOfSatellites(), actual.getAmountOfSatellites());
        assertEquals(expected.getHdop(), actual.getHdop(), 0.);
        assertEquals(expected.getInputs(), actual.getInputs());
        assertEquals(expected.getOutputs(), actual.getOutputs());
        assertArrayEquals(expected.getAnalogInputs(), actual.getAnalogInputs(), 0.);
        assertEquals(expected.getDriverKeyCode(), actual.getDriverKeyCode());
        assertEquals(expected.getTracker(), actual.getTracker());
        assertEquals(expected.getAddress(), actual.getAddress());
    }

    public static void checkEqualsExceptParameters(final DataEntity expected, final DataEntity actual) {
        assertEquals(expected.getId(), actual.getId());
        assertEquals(expected.getDateTime(), actual.getDateTime());
        assertEquals(expected.getCoordinate(), actual.getCoordinate());
        assertEquals(expected.getSpeed(), actual.getSpeed(), 0.);
        assertEquals(expected.getCourse(), actual.getCourse());
        assertEquals(expected.getAltitude(), actual.getAltitude());
        assertEquals(expected.getAmountOfSatellites(), actual.getAmountOfSatellites());
        assertEquals(expected.getHdop(), actual.getHdop(), 0.);
        assertEquals(expected.getInputs(), actual.getInputs());
        assertEquals(expected.getOutputs(), actual.getOutputs());
        assertArrayEquals(expected.getAnalogInputs(), actual.getAnalogInputs(), 0.);
        assertEquals(expected.getDriverKeyCode(), actual.getDriverKeyCode());
        assertEquals(expected.getTracker(), actual.getTracker());
        assertEquals(expected.getAddress(), actual.getAddress());
    }

    public static boolean areParametersFetched(final DataEntity data) {
        return isPropertyFetched(data, DataEntity::getParameters);
    }

    public static boolean isTrackerFetched(final DataEntity data) {
        return isPropertyFetched(data, DataEntity::getTracker);
    }

    public static boolean isAddressFetched(final DataEntity data) {
        return isPropertyFetched(data, DataEntity::getAddress);
    }

    public static boolean areParametersNotFetched(final Collection<DataEntity> entities) {
        return areAllMatch(entities, entity -> !areParametersFetched(entity));
    }

    public static boolean areTrackersFetched(final Collection<DataEntity> entities) {
        return areAllMatch(entities, DataEntityUtil::isTrackerFetched);
    }

    public static boolean areAddressesFetched(final Collection<DataEntity> entities) {
        return areAllMatch(entities, DataEntityUtil::isAddressFetched);
    }

    private static void checkEqualsWithoutOrder(final List<ParameterEntity> expected,
                                                final List<ParameterEntity> actual) {
        if (expected != null && actual != null) {
            assertTrue(
                    expected.size() == actual.size()
                            && expected.containsAll(actual)
                            && actual.containsAll(expected)
            );
        } else {
            assertNull(expected);
            assertNull(actual);
        }
    }
}
