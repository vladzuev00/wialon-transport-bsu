package by.bsu.wialontransport.crud.mapper;

import by.bsu.wialontransport.base.AbstractContextTest;
import by.bsu.wialontransport.crud.dto.TrackerMileage;
import by.bsu.wialontransport.crud.entity.TrackerMileageEntity;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public final class TrackerOdometerMapperTest extends AbstractContextTest {

    @Autowired
    private TrackerOdometerMapper mapper;

    @Test
    public void dtoShouldBeMappedToEntity() {
        final TrackerMileage givenDto = TrackerMileage.builder()
                .id(255L)
                .urban(10)
                .country(15)
                .build();

        final TrackerMileageEntity actual = this.mapper.mapToEntity(givenDto);
        final TrackerMileageEntity expected = TrackerMileageEntity.builder()
                .id(255L)
                .urban(10)
                .country(15)
                .build();
        assertNotNull(actual);
        checkEquals(expected, actual);
    }

    @Test
    public void entityShouldBeMappedToDto() {
        final TrackerMileageEntity givenEntity = TrackerMileageEntity.builder()
                .id(255L)
                .urban(10)
                .country(15)
                .build();

        final TrackerMileage actual = this.mapper.mapToDto(givenEntity);
        final TrackerMileage expected = TrackerMileage.builder()
                .id(255L)
                .urban(10)
                .country(15)
                .build();
        assertEquals(expected, actual);
    }

    private static void checkEquals(final TrackerMileageEntity expected, final TrackerMileageEntity actual) {
        assertEquals(expected.getId(), actual.getId());
        assertEquals(expected.getUrban(), actual.getUrban(), 0.);
        assertEquals(expected.getCountry(), actual.getCountry(), 0.);
    }
}
