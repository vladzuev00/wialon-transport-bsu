package by.bsu.wialontransport.crud.mapper;

import by.bsu.wialontransport.base.AbstractContextTest;
import by.bsu.wialontransport.crud.dto.DataCalculations;
import by.bsu.wialontransport.crud.entity.DataCalculationsEntity;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public final class DataCalculationsMapperTest extends AbstractContextTest {

    @Autowired
    private DataCalculationsMapper mapper;

    //TODO: correct test
    @Test
    public void dtoShouldBeMappedToEntity() {
        final DataCalculations givenDto = DataCalculations.builder()
                .id(256L)
                .gpsOdometer(10.5)
                .ignitionOn(true)
                .engineOnDurationSeconds(11)
                .build();

        final DataCalculationsEntity actual = this.mapper.mapToEntity(givenDto);
        final DataCalculationsEntity expected = DataCalculationsEntity.builder()
                .id(256L)
                .gpsOdometer(10.5)
                .ignitionOn(true)
                .engineOnDurationSeconds(11)
                .build();

        assertNotNull(actual);
        checkEquals(expected, actual);
    }

    //TODO: add test to map entity to dto

    private static void checkEquals(final DataCalculationsEntity expected, final DataCalculationsEntity actual) {
        assertEquals(expected.getId(), actual.getId());
        assertEquals(expected.getGpsOdometer(), actual.getGpsOdometer(), 0.);
        assertEquals(expected.isIgnitionOn(), actual.isIgnitionOn());
        assertEquals(expected.getEngineOnDurationSeconds(), actual.getEngineOnDurationSeconds());
        assertEquals(expected.getData(), actual.getData());
    }
}
