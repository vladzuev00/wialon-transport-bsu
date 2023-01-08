package by.bsu.wialontransport.crud.mapper;

import by.bsu.wialontransport.base.AbstractContextTest;
import by.bsu.wialontransport.crud.dto.Data;
import by.bsu.wialontransport.crud.dto.ExtendedData;
import by.bsu.wialontransport.crud.entity.DataEntity;
import by.bsu.wialontransport.crud.entity.ExtendedDataEntity;
import by.bsu.wialontransport.crud.entity.TrackerEntity;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.time.LocalTime;

import static by.bsu.wialontransport.crud.entity.DataEntity.Latitude.Type.SOUTH;
import static by.bsu.wialontransport.crud.entity.DataEntity.Longitude.Type.EAST;
import static org.junit.Assert.*;

public final class ExtendedDataMapperTest extends AbstractContextTest {

    @Autowired
    private ExtendedDataMapper mapper;

    @Test
    public void entityShouldBeMappedToDto() {
        final ExtendedDataEntity givenEntity = ExtendedDataEntity.extendedDataEntityBuilder()
                .id(255L)
                .date(LocalDate.of(2023, 1, 8))
                .time(LocalTime.of(4, 18, 15))
                .latitude(new DataEntity.Latitude(20, 21, 22, SOUTH))
                .longitude(new DataEntity.Longitude(23, 24, 25, EAST))
                .speed(26)
                .course(27)
                .height(28)
                .amountOfSatellites(29)
                .tracker(super.entityManager.getReference(TrackerEntity.class, 255L))
                .reductionPrecision(0.1)
                .inputs(30)
                .outputs(31)
                .analogInputs(new double[]{0.1, 0.2, 0.3})
                .driverKeyCode("driver key code")
                .build();

        final ExtendedData actual = this.mapper.mapToDto(givenEntity);
        final ExtendedData expected = ExtendedData.extendedDataBuilder()
                .id(255L)
                .date(LocalDate.of(2023, 1, 8))
                .time(LocalTime.of(4, 18, 15))
                .latitude(new Data.Latitude(20, 21, 22, SOUTH))
                .longitude(new Data.Longitude(23, 24, 25, EAST))
                .speed(26)
                .course(27)
                .height(28)
                .amountOfSatellites(29)
                .reductionPrecision(0.1)
                .inputs(30)
                .outputs(31)
                .analogInputs(new double[]{0.1, 0.2, 0.3})
                .driverKeyCode("driver key code")
                .build();
        assertEquals(expected, actual);
    }

    @Test
    public void dtoShouldBeMappedToEntity() {
        final ExtendedData givenDto = ExtendedData.extendedDataBuilder()
                .id(255L)
                .date(LocalDate.of(2023, 1, 8))
                .time(LocalTime.of(4, 18, 15))
                .latitude(new Data.Latitude(20, 21, 22, SOUTH))
                .longitude(new Data.Longitude(23, 24, 25, EAST))
                .speed(26)
                .course(27)
                .height(28)
                .amountOfSatellites(29)
                .reductionPrecision(0.1)
                .inputs(30)
                .outputs(31)
                .analogInputs(new double[]{0.1, 0.2, 0.3})
                .driverKeyCode("driver key code")
                .build();

        final ExtendedDataEntity actual = this.mapper.mapToEntity(givenDto);
        final ExtendedDataEntity expected = ExtendedDataEntity.extendedDataEntityBuilder()
                .id(255L)
                .date(LocalDate.of(2023, 1, 8))
                .time(LocalTime.of(4, 18, 15))
                .latitude(new DataEntity.Latitude(20, 21, 22, SOUTH))
                .longitude(new DataEntity.Longitude(23, 24, 25, EAST))
                .speed(26)
                .course(27)
                .height(28)
                .amountOfSatellites(29)
                .reductionPrecision(0.1)
                .inputs(30)
                .outputs(31)
                .analogInputs(new double[]{0.1, 0.2, 0.3})
                .driverKeyCode("driver key code")
                .build();

        assertNotNull(actual);
        checkEquals(expected, actual);
    }

    private static void checkEquals(final ExtendedDataEntity expected, final ExtendedDataEntity actual) {
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
        assertEquals(expected.getReductionPrecision(), actual.getReductionPrecision(), 0.);
        assertEquals(expected.getInputs(), actual.getInputs());
        assertEquals(expected.getOutputs(), actual.getOutputs());
        assertArrayEquals(expected.getAnalogInputs(), actual.getAnalogInputs(), 0.);
        assertEquals(expected.getDriverKeyCode(), actual.getDriverKeyCode());
    }
}
