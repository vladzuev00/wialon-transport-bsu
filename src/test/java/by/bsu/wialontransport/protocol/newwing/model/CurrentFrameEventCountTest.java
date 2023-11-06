package by.bsu.wialontransport.protocol.newwing.model;

import org.junit.Test;

import java.util.OptionalInt;
import java.util.concurrent.atomic.AtomicInteger;

import static by.bsu.wialontransport.util.ReflectionUtil.findProperty;
import static by.bsu.wialontransport.util.ReflectionUtil.setProperty;
import static java.lang.Integer.MIN_VALUE;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public final class CurrentFrameEventCountTest {
    private static final String FIELD_NAME_VALUE = "value";
    private static final int EXPECTED_NOT_DEFINED_VALUE = MIN_VALUE;

    @Test
    public void valueShouldBeSet() {
        final CurrentFrameEventCount givenCurrentFrameEventCount = new CurrentFrameEventCount();
        final int givenNewValue = 5;

        givenCurrentFrameEventCount.setValue(givenNewValue);

        final int actual = findValue(givenCurrentFrameEventCount);
        assertEquals(givenNewValue, actual);
    }

    @Test
    public void valueShouldBeTaken() {
        final CurrentFrameEventCount givenCurrentFrameEventCount = new CurrentFrameEventCount();
        final int givenValue = 5;
        setValue(givenCurrentFrameEventCount, givenValue);

        final OptionalInt optionalActual = givenCurrentFrameEventCount.takeValue();
        assertTrue(optionalActual.isPresent());
        final int actual = optionalActual.getAsInt();
        assertEquals(givenValue, actual);

        final int actualNewValue = findValue(givenCurrentFrameEventCount);
        assertEquals(EXPECTED_NOT_DEFINED_VALUE, actualNewValue);
    }

    @Test
    public void valueShouldNotBeTaken() {
        final CurrentFrameEventCount givenCurrentFrameEventCount = new CurrentFrameEventCount();

        final OptionalInt optionalActual = givenCurrentFrameEventCount.takeValue();
        assertTrue(optionalActual.isEmpty());
    }

    private static int findValue(final CurrentFrameEventCount currentFrameEventCount) {
        final AtomicInteger atomicInteger = findProperty(currentFrameEventCount, FIELD_NAME_VALUE, AtomicInteger.class);
        return atomicInteger.get();
    }

    @SuppressWarnings("SameParameterValue")
    private static void setValue(final CurrentFrameEventCount currentFrameEventCount, final int value) {
        final AtomicInteger atomicInteger = new AtomicInteger(value);
        setProperty(currentFrameEventCount, FIELD_NAME_VALUE, atomicInteger);
    }
}
