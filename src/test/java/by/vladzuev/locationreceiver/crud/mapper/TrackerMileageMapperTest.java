package by.vladzuev.locationreceiver.crud.mapper;

import by.vladzuev.locationreceiver.base.AbstractSpringBootTest;
import by.vladzuev.locationreceiver.crud.dto.TrackerMileage;
import by.vladzuev.locationreceiver.crud.entity.MileageEntity;
import by.vladzuev.locationreceiver.util.entity.MileageEntityUtil;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static by.vladzuev.locationreceiver.util.entity.MileageEntityUtil.assertEquals;
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

        final MileageEntity actual = mapper.mapToEntity(givenDto);
        final MileageEntity expected = MileageEntity.builder()
                .id(255L)
                .urban(10)
                .country(15)
                .build();
        assertNotNull(actual);
        MileageEntityUtil.assertEquals(expected, actual);
    }

    @Test
    public void entityShouldBeMappedToDto() {
        final MileageEntity givenEntity = MileageEntity.builder()
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
