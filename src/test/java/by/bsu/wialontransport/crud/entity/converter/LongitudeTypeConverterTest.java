package by.bsu.wialontransport.crud.entity.converter;

import by.bsu.wialontransport.crud.entity.DataEntity.Longitude.Type;
import org.junit.Test;

import static by.bsu.wialontransport.crud.entity.DataEntity.Longitude.Type.WESTERN;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

public final class LongitudeTypeConverterTest {

    private final LongitudeTypeConverter converter = new LongitudeTypeConverter();

    @Test
    public void typeShouldBeConvertedToCharacter() {
        final Character actual = this.converter.convertToDatabaseColumn(WESTERN);
        final Character expected = 'W';
        assertEquals(expected, actual);
    }

    @Test
    public void characterShouldBeConvertedToType() {
        final Character givenCharacter = 'W';

        final Type actual = this.converter.convertToEntityAttribute(givenCharacter);
        assertSame(WESTERN, actual);
    }
}
