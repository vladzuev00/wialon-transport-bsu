package by.bsu.wialontransport.crud.entity.converter;

import by.bsu.wialontransport.crud.entity.DataEntity;
import by.bsu.wialontransport.crud.entity.DataEntity.Latitude.Type;
import org.junit.Test;

import static by.bsu.wialontransport.crud.entity.DataEntity.Latitude.Type.NORTH;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

public final class LatitudeTypeConverterTest {

    private final LatitudeTypeConverter converter = new LatitudeTypeConverter();

    @Test
    public void typeShouldBeConvertedToValue() {
        final Character actual = this.converter.convertToDatabaseColumn(NORTH);
        final Character expected = 'N';
        assertEquals(expected, actual);
    }

    @Test
    public void valueShouldBeConvertedToValue() {
        final Character givenValue = 'N';

        final Type actual = this.converter.convertToEntityAttribute(givenValue);
        assertSame(NORTH, actual);
    }
}
