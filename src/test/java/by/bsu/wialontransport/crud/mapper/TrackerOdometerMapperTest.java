package by.bsu.wialontransport.crud.mapper;

import by.bsu.wialontransport.base.AbstractContextTest;
import by.bsu.wialontransport.crud.dto.TrackerOdometer;
import by.bsu.wialontransport.crud.entity.TrackerOdometerEntity;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public final class TrackerOdometerMapperTest extends AbstractContextTest {

    @Autowired
    private TrackerOdometerMapper mapper;

    @Test
    public void dtoShouldBeMappedToEntity() {
        final TrackerOdometer givenDto = TrackerOdometer.builder()
                .id(255L)
                .urban(10)
                .country(15)
                .build();

        final TrackerOdometerEntity actual = this.mapper.mapToEntity(givenDto);
        final TrackerOdometerEntity expected = TrackerOdometerEntity.builder()
                .id(255L)
                .urban(10)
                .country(15)
                .build();
        assertNotNull(actual);
        checkEquals(expected, actual);
    }

    @Test
    public void entityShouldBeMappedToDto() {
        final TrackerOdometerEntity givenEntity = TrackerOdometerEntity.builder()
                .id(255L)
                .urban(10)
                .country(15)
                .build();

        final TrackerOdometer actual = this.mapper.mapToDto(givenEntity);
        final TrackerOdometer expected = TrackerOdometer.builder()
                .id(255L)
                .urban(10)
                .country(15)
                .build();
        assertEquals(expected, actual);
    }

    private static void checkEquals(final TrackerOdometerEntity expected, final TrackerOdometerEntity actual) {
        assertEquals(expected.getId(), actual.getId());
        assertEquals(expected.getUrban(), actual.getUrban(), 0.);
        assertEquals(expected.getCountry(), actual.getCountry(), 0.);
    }
}
