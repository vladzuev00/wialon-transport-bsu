package by.vladzuev.locationreceiver.util.entity;

import by.vladzuev.locationreceiver.crud.entity.LocationEntity;
import by.vladzuev.locationreceiver.crud.entity.ParameterEntity;
import by.vladzuev.locationreceiver.util.CollectionUtil;
import by.vladzuev.locationreceiver.util.HibernateUtil;
import lombok.experimental.UtilityClass;

import java.util.Collection;
import java.util.List;

import static org.junit.Assert.*;

//TODO: refactor
@UtilityClass
public final class DataEntityUtil {

    public static void checkEquals(final LocationEntity expected, final LocationEntity actual) {
        checkEqualsExceptIdAndParameters(expected, actual);
        assertEquals(expected.getId(), actual.getId());
        checkEqualsWithoutOrder(expected.getParameters(), actual.getParameters());
    }

    //TODO: remove
    public static void checkEqualsExceptIdAndParameters(final LocationEntity expected, final LocationEntity actual) {
        assertEquals(expected.getDateTime(), actual.getDateTime());
        assertEquals(expected.getCoordinate(), actual.getCoordinate());
        assertEquals(expected.getSpeed(), actual.getSpeed(), 0.);
        assertEquals(expected.getCourse(), actual.getCourse());
        assertEquals(expected.getAltitude(), actual.getAltitude());
//        assertEquals(expected.getAmountOfSatellites(), actual.getAmountOfSatellites());
        assertEquals(expected.getHdop(), actual.getHdop(), 0.);
        assertEquals(expected.getInputs(), actual.getInputs());
        assertEquals(expected.getOutputs(), actual.getOutputs());
        assertArrayEquals(expected.getAnalogInputs(), actual.getAnalogInputs(), 0.);
        assertEquals(expected.getDriverKeyCode(), actual.getDriverKeyCode());
        assertEquals(expected.getTracker(), actual.getTracker());
        assertEquals(expected.getAddress(), actual.getAddress());
    }

    public static void checkEqualsExceptParameters(final LocationEntity expected, final LocationEntity actual) {
        assertEquals(expected.getId(), actual.getId());
        assertEquals(expected.getDateTime(), actual.getDateTime());
        assertEquals(expected.getCoordinate(), actual.getCoordinate());
        assertEquals(expected.getSpeed(), actual.getSpeed(), 0.);
        assertEquals(expected.getCourse(), actual.getCourse());
        assertEquals(expected.getAltitude(), actual.getAltitude());
//        assertEquals(expected.getAmountOfSatellites(), actual.getAmountOfSatellites());
        assertEquals(expected.getHdop(), actual.getHdop(), 0.);
        assertEquals(expected.getInputs(), actual.getInputs());
        assertEquals(expected.getOutputs(), actual.getOutputs());
        assertArrayEquals(expected.getAnalogInputs(), actual.getAnalogInputs(), 0.);
        assertEquals(expected.getDriverKeyCode(), actual.getDriverKeyCode());
        assertEquals(expected.getTracker(), actual.getTracker());
        assertEquals(expected.getAddress(), actual.getAddress());
    }

    public static boolean areParametersFetched(final LocationEntity data) {
        return HibernateUtil.isPropertyFetched(data, LocationEntity::getParameters);
    }

    public static boolean isTrackerFetched(final LocationEntity data) {
        return HibernateUtil.isPropertyFetched(data, LocationEntity::getTracker);
    }

    public static boolean isAddressFetched(final LocationEntity data) {
        return HibernateUtil.isPropertyFetched(data, LocationEntity::getAddress);
    }

    public static boolean areParametersNotFetched(final Collection<LocationEntity> entities) {
        return CollectionUtil.areAllMatch(entities, entity -> !areParametersFetched(entity));
    }

    public static boolean areTrackersFetched(final Collection<LocationEntity> entities) {
        return CollectionUtil.areAllMatch(entities, DataEntityUtil::isTrackerFetched);
    }

    public static boolean areAddressesFetched(final Collection<LocationEntity> entities) {
        return CollectionUtil.areAllMatch(entities, DataEntityUtil::isAddressFetched);
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
