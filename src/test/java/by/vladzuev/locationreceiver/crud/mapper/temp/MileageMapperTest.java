package by.vladzuev.locationreceiver.crud.mapper.temp;

import by.vladzuev.locationreceiver.base.AbstractSpringBootTest;
import by.vladzuev.locationreceiver.crud.dto.Mileage;
import by.vladzuev.locationreceiver.crud.entity.MileageEntity;
import by.vladzuev.locationreceiver.crud.mapper.TrackerMileageMapper;
import by.vladzuev.locationreceiver.util.entity.MileageEntityUtil;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public final class MileageMapperTest extends AbstractSpringBootTest {

    @Autowired
    private TrackerMileageMapper mapper;

    @Test
    public void dtoShouldBeMappedToEntity() {
        final Mileage givenDto = Mileage.builder()
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

        final Mileage actual = mapper.mapToDto(givenEntity);
        final Mileage expected = Mileage.builder()
                .id(255L)
                .urban(10)
                .country(15)
                .build();
        assertEquals(expected, actual);
    }
}
