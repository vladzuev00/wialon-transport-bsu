package by.bsu.wialontransport.util.entity;

import by.bsu.wialontransport.crud.entity.DataEntity;
import by.bsu.wialontransport.crud.entity.ParameterEntity;
import lombok.experimental.UtilityClass;

import java.util.List;

import static org.junit.Assert.*;

@UtilityClass
public final class DataEntityUtil {

    public static void checkEquals(final DataEntity expected, final DataEntity actual) {
        assertEquals(expected.getId(), actual.getId());
        assertEquals(expected.getDateTime(), actual.getDateTime());
        assertEquals(expected.getCoordinate(), actual.getCoordinate());
        assertEquals(expected.getSpeed(), actual.getSpeed(), 0.);
        assertEquals(expected.getCourse(), actual.getCourse());
        assertEquals(expected.getAltitude(), actual.getAltitude());
        assertEquals(expected.getAmountOfSatellites(), actual.getAmountOfSatellites());
        assertEquals(expected.getReductionPrecision(), actual.getReductionPrecision(), 0.);
        assertEquals(expected.getInputs(), actual.getInputs());
        assertEquals(expected.getOutputs(), actual.getOutputs());
        assertArrayEquals(expected.getAnalogInputs(), actual.getAnalogInputs(), 0.);
        assertEquals(expected.getDriverKeyCode(), actual.getDriverKeyCode());
        checkEqualsWithoutOrder(expected.getParameters(), actual.getParameters());
        TrackerEntityUtil.checkEquals(expected.getTracker(), actual.getTracker());
        AddressEntityUtil.checkEquals(expected.getAddress(), actual.getAddress());
    }

    private static void checkEqualsWithoutOrder(final List<ParameterEntity> expected,
                                                final List<ParameterEntity> actual) {
        assertTrue(
                expected.size() == actual.size()
                        && expected.containsAll(actual)
                        && actual.containsAll(expected)
        );
    }
}
