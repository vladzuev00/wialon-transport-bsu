package by.bsu.wialontransport.crud.mapper;

import by.bsu.wialontransport.base.AbstractContextTest;
import by.bsu.wialontransport.crud.dto.Data;
import by.bsu.wialontransport.crud.dto.Parameter;
import by.bsu.wialontransport.crud.dto.Tracker;
import by.bsu.wialontransport.crud.entity.DataEntity;
import by.bsu.wialontransport.crud.entity.ParameterEntity;
import by.bsu.wialontransport.crud.entity.TrackerEntity;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;

import static by.bsu.wialontransport.crud.entity.DataEntity.Latitude.Type.SOUTH;
import static by.bsu.wialontransport.crud.entity.DataEntity.Longitude.Type.EAST;
import static by.bsu.wialontransport.crud.entity.ParameterEntity.Type.INTEGER;
import static by.bsu.wialontransport.crud.entity.ParameterEntity.Type.STRING;
import static org.junit.Assert.*;

public final class DataMapperTest extends AbstractContextTest {

    @Autowired
    private DataMapper mapper;

    @Test
    public void dtoShouldBeMappedToEntity() {
        final Data givenDto = Data.builder()
                .id(255L)
                .date(LocalDate.of(2023, 1, 8))
                .time(LocalTime.of(4, 18, 15))
                .latitude(new Data.Latitude(20, 21, 22, SOUTH))
                .longitude(new Data.Longitude(23, 24, 25, EAST))
                .speed(26)
                .course(27)
                .altitude(28)
                .amountOfSatellites(29)
                .reductionPrecision(30.5)
                .inputs(31)
                .outputs(32)
                .analogInputs(new double[]{0.2, 0.3, 0.4})
                .driverKeyCode("driver key code")
                .parametersByNames(Map.of(
                        "first-param", new Parameter(256L, "first-param", INTEGER, "44"),
                        "second-param", new Parameter(257L, "second-param", STRING, "text")
                ))
                .tracker(Tracker.builder()
                        .id(256L)
                        .imei("11111222223333344444")
                        .password("password")
                        .phoneNumber("447336934")
                        .build())
                .build();

        final DataEntity actual = this.mapper.mapToEntity(givenDto);
        final DataEntity expected = DataEntity.builder()
                .id(255L)
                .date(LocalDate.of(2023, 1, 8))
                .time(LocalTime.of(4, 18, 15))
                .latitude(new DataEntity.Latitude(20, 21, 22, SOUTH))
                .longitude(new DataEntity.Longitude(23, 24, 25, EAST))
                .speed(26)
                .course(27)
                .altitude(28)
                .amountOfSatellites(29)
                .reductionPrecision(30.5)
                .inputs(31)
                .outputs(32)
                .analogInputs(new double[]{0.2, 0.3, 0.4})
                .driverKeyCode("driver key code")
                .parameters(List.of(
                        new ParameterEntity(256L, "first-param", INTEGER, "44", null),
                        new ParameterEntity(257L, "second-param", STRING, "text", null)
                ))
                .tracker(TrackerEntity.builder()
                        .id(256L)
                        .imei("11111222223333344444")
                        .password("password")
                        .phoneNumber("447336934")
                        .build())
                .build();

        assertNotNull(actual);
        checkEquals(expected, actual);
    }

    @Test
    public void entityShouldBeMappedToDto() {
        final DataEntity givenEntity = DataEntity.builder()
                .id(255L)
                .date(LocalDate.of(2023, 1, 8))
                .time(LocalTime.of(4, 18, 15))
                .latitude(new DataEntity.Latitude(20, 21, 22, SOUTH))
                .longitude(new DataEntity.Longitude(23, 24, 25, EAST))
                .speed(26)
                .course(27)
                .altitude(28)
                .amountOfSatellites(29)
                .reductionPrecision(30.5)
                .inputs(31)
                .outputs(32)
                .analogInputs(new double[]{0.2, 0.3, 0.4})
                .driverKeyCode("driver key code")
                .parameters(List.of(
                        new ParameterEntity(256L, "first-param", INTEGER, "44", null),
                        new ParameterEntity(257L, "second-param", STRING, "text", null)
                ))
                .tracker(TrackerEntity.builder()
                        .id(256L)
                        .imei("11111222223333344444")
                        .password("password")
                        .phoneNumber("447336934")
                        .build())
                .build();

        final Data actual = this.mapper.mapToDto(givenEntity);
        final Data expected = Data.builder()
                .id(255L)
                .date(LocalDate.of(2023, 1, 8))
                .time(LocalTime.of(4, 18, 15))
                .latitude(new Data.Latitude(20, 21, 22, SOUTH))
                .longitude(new Data.Longitude(23, 24, 25, EAST))
                .speed(26)
                .course(27)
                .altitude(28)
                .amountOfSatellites(29)
                .reductionPrecision(30.5)
                .inputs(31)
                .outputs(32)
                .analogInputs(new double[]{0.2, 0.3, 0.4})
                .driverKeyCode("driver key code")
                .parametersByNames(Map.of(
                        "first-param", new Parameter(256L, "first-param", INTEGER, "44"),
                        "second-param", new Parameter(257L, "second-param", STRING, "text")
                ))
                .tracker(Tracker.builder()
                        .id(256L)
                        .imei("11111222223333344444")
                        .password("password")
                        .phoneNumber("447336934")
                        .build())
                .build();
        assertEquals(expected, actual);
    }

    private static void checkEquals(final DataEntity expected, final DataEntity actual) {
        assertEquals(expected.getId(), actual.getId());
        assertEquals(expected.getDate(), actual.getDate());
        assertEquals(expected.getTime(), actual.getTime());
        assertEquals(expected.getLatitude(), actual.getLatitude());
        assertEquals(expected.getLongitude(), actual.getLongitude());
        assertEquals(expected.getSpeed(), actual.getSpeed());
        assertEquals(expected.getCourse(), actual.getCourse());
        assertEquals(expected.getAltitude(), actual.getAltitude());
        assertEquals(expected.getAmountOfSatellites(), actual.getAmountOfSatellites());
        assertEquals(expected.getReductionPrecision(), actual.getReductionPrecision(), 0.);
        assertEquals(expected.getInputs(), actual.getInputs());
        assertEquals(expected.getOutputs(), actual.getOutputs());
        assertArrayEquals(expected.getAnalogInputs(), actual.getAnalogInputs(), 0.);
        assertEquals(expected.getDriverKeyCode(), actual.getDriverKeyCode());
        checkEqualsWithoutOrder(expected.getParameters(), actual.getParameters());
        checkEquals(expected.getTracker(), actual.getTracker());
    }

    private static void checkEqualsWithoutOrder(final List<ParameterEntity> expected,
                                                final List<ParameterEntity> actual) {
        assertTrue(expected.size() == actual.size()
                && expected.containsAll(actual)
                && actual.containsAll(expected));
    }

    private static void checkEquals(final TrackerEntity expected, final TrackerEntity actual) {
        assertEquals(expected.getId(), actual.getId());
        assertEquals(expected.getImei(), actual.getImei());
        assertEquals(expected.getPassword(), actual.getPassword());
        assertEquals(expected.getPhoneNumber(), actual.getPhoneNumber());
        assertEquals(expected.getUser(), actual.getUser());
    }
}
