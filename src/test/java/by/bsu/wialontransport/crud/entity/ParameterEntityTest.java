package by.bsu.wialontransport.crud.entity;

import by.bsu.wialontransport.crud.entity.ParameterEntity.Type;
import org.junit.Test;

import static by.bsu.wialontransport.crud.entity.ParameterEntity.Type.*;
import static org.junit.Assert.assertSame;

public final class ParameterEntityTest {

    @Test
    public void typeShouldBeFoundByValue() {
        final byte givenValue = 1;

        final Type actual = findByValue(givenValue);
        assertSame(INTEGER, actual);
    }

    @Test
    public void typeShouldNotBeFoundByValue() {
        final byte givenValue = 127;

        final Type actual = findByValue(givenValue);
        assertSame(NOT_DEFINED, actual);
    }
}
