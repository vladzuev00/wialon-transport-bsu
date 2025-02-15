package by.vladzuev.locationreceiver.crud.mapper;

import by.vladzuev.locationreceiver.base.AbstractSpringBootTest;
import by.vladzuev.locationreceiver.crud.dto.TrackerMileage;
import by.vladzuev.locationreceiver.crud.entity.TrackerMileageEntity;
import by.vladzuev.locationreceiver.util.entity.TrackerMileageEntityUtil;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static by.vladzuev.locationreceiver.util.entity.TrackerMileageEntityUtil.checkEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public final class TrackerMileageMapperTest extends AbstractSpringBootTest {

    @Autowired
    private TrackerMileageMapper mapper;

    @Test
    public void dtoShouldBeMappedToEntity() {
        final TrackerMileage givenDto = TrackerMileage.builder()
                .id(255L)
                .urban(10)
                .country(15)
                .build();

        final TrackerMileageEntity actual = mapper.mapToEntity(givenDto);
        final TrackerMileageEntity expected = TrackerMileageEntity.builder()
                .id(255L)
                .urban(10)
                .country(15)
                .build();
        assertNotNull(actual);
        TrackerMileageEntityUtil.checkEquals(expected, actual);
    }

    @Test
    public void entityShouldBeMappedToDto() {
        final TrackerMileageEntity givenEntity = TrackerMileageEntity.builder()
                .id(255L)
                .urban(10)
                .country(15)
                .build();

        final TrackerMileage actual = mapper.mapToDto(givenEntity);
        final TrackerMileage expected = TrackerMileage.builder()
                .id(255L)
                .urban(10)
                .country(15)
                .build();
        assertEquals(expected, actual);
    }
}
