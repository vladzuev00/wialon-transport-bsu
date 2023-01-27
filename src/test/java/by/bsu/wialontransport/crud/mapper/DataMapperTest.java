package by.bsu.wialontransport.crud.mapper;

import by.bsu.wialontransport.base.AbstractContextTest;
import by.bsu.wialontransport.crud.dto.Data;
import by.bsu.wialontransport.crud.entity.DataEntity;
import by.bsu.wialontransport.crud.entity.TrackerEntity;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.time.LocalTime;

import static by.bsu.wialontransport.crud.entity.DataEntity.Latitude.Type.SOUTH;
import static by.bsu.wialontransport.crud.entity.DataEntity.Longitude.Type.EAST;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public final class DataMapperTest extends AbstractContextTest {

    @Autowired
    private DataMapper mapper;

    @Test
    public void dtoShouldBeMappedToEntity() {
//        final Data givenDto = Data.dataBuilder()
//                .id(255L)
//                .date(LocalDate.of(2023, 1, 8))
//                .time(LocalTime.of(4, 18, 15))
//                .latitude(new Data.Latitude(20, 21, 22, SOUTH))
//                .longitude(new Data.Longitude(23, 24, 25, EAST))
//                .speed(26)
//                .course(27)
//                .height(28)
//                .amountOfSatellites(29)
//                .build();
//
//        final DataEntity actual = this.mapper.mapToEntity(givenDto);
//        final DataEntity expected = DataEntity.dataEntityBuilder()
//                .id(255L)
//                .date(LocalDate.of(2023, 1, 8))
//                .time(LocalTime.of(4, 18, 15))
//                .latitude(new DataEntity.Latitude(20, 21, 22, SOUTH))
//                .longitude(new DataEntity.Longitude(23, 24, 25, EAST))
//                .speed(26)
//                .course(27)
//                .height(28)
//                .amountOfSatellites(29)
//                .build();
//
//        assertNotNull(actual);
//        checkEquals(expected, actual);
    }

    @Test
    public void entityShouldBeMappedToDto() {
//        final DataEntity givenEntity = DataEntity.dataEntityBuilder()
//                .id(255L)
//                .date(LocalDate.of(2023, 1, 8))
//                .time(LocalTime.of(4, 18, 15))
//                .latitude(new DataEntity.Latitude(20, 21, 22, SOUTH))
//                .longitude(new DataEntity.Longitude(23, 24, 25, EAST))
//                .speed(26)
//                .course(27)
//                .height(28)
//                .amountOfSatellites(29)
//                .tracker(super.entityManager.getReference(TrackerEntity.class, 255L))
//                .build();

//        final Data actual = this.mapper.mapToDto(givenEntity);
//        final Data expected = Data.dataBuilder()
//                .id(255L)
//                .date(LocalDate.of(2023, 1, 8))
//                .time(LocalTime.of(4, 18, 15))
//                .latitude(new Data.Latitude(20, 21, 22, SOUTH))
//                .longitude(new Data.Longitude(23, 24, 25, EAST))
//                .speed(26)
//                .course(27)
//                .height(28)
//                .amountOfSatellites(29)
//                .build();
//        assertEquals(expected, actual);
    }

    private static void checkEquals(final DataEntity expected, final DataEntity actual) {
        assertEquals(expected.getId(), actual.getId());
        assertEquals(expected.getDate(), actual.getDate());
        assertEquals(expected.getTime(), actual.getTime());
        assertEquals(expected.getLatitude(), actual.getLatitude());
        assertEquals(expected.getLongitude(), actual.getLongitude());
        assertEquals(expected.getSpeed(), actual.getSpeed());
        assertEquals(expected.getCourse(), actual.getCourse());
        assertEquals(expected.getHeight(), actual.getHeight());
        assertEquals(expected.getAmountOfSatellites(), actual.getAmountOfSatellites());
        assertEquals(expected.getTracker(), actual.getTracker());
    }
}
