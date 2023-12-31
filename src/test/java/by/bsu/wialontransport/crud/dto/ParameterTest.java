package by.bsu.wialontransport.crud.dto;

import org.junit.Test;

import static by.bsu.wialontransport.crud.dto.Parameter.createDoubleParameter;
import static by.bsu.wialontransport.crud.entity.ParameterEntity.Type.DOUBLE;
import static org.junit.Assert.assertEquals;

public final class ParameterTest {

    @Test
    public void doubleParameterShouldBeCreated() {
        final String givenName = "name";
        final double givenValue = 10;

        final Parameter actual = createDoubleParameter(givenName, givenValue);
        final Parameter expected = Parameter.builder()
                .name(givenName)
                .type(DOUBLE)
                .value(Double.toString(givenValue))
                .build();
        assertEquals(expected, actual);
    }
}
