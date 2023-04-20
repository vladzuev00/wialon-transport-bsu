package by.bsu.wialontransport.crud.mapper;

import by.bsu.wialontransport.base.AbstractContextTest;
import by.bsu.wialontransport.crud.dto.Address;
import by.bsu.wialontransport.crud.dto.Data;
import by.bsu.wialontransport.crud.dto.Parameter;
import by.bsu.wialontransport.crud.dto.Tracker;
import by.bsu.wialontransport.crud.entity.AddressEntity;
import by.bsu.wialontransport.crud.entity.DataEntity;
import by.bsu.wialontransport.crud.entity.ParameterEntity;
import by.bsu.wialontransport.crud.entity.TrackerEntity;
import org.hibernate.Hibernate;
import org.hibernate.annotations.LazyCollection;
import org.hibernate.proxy.HibernateProxy;
import org.hibernate.proxy.LazyInitializer;
import org.junit.Test;
import org.locationtech.jts.geom.*;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static by.bsu.wialontransport.crud.entity.DataEntity.Latitude.Type.SOUTH;
import static by.bsu.wialontransport.crud.entity.DataEntity.Longitude.Type.EAST;
import static by.bsu.wialontransport.crud.entity.ParameterEntity.Type.INTEGER;
import static by.bsu.wialontransport.crud.entity.ParameterEntity.Type.STRING;
import static java.util.Collections.emptyList;
import static org.hibernate.Hibernate.isInitialized;
import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.same;
import static org.mockito.Mockito.mockStatic;

public final class DataMapperTest extends AbstractContextTest {

    @Autowired
    private DataMapper mapper;

    @Autowired
    private GeometryFactory geometryFactory;

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
                .address(Address.builder()
                        .id(257L)
                        .boundingBox(this.createPolygon(1, 2, 3, 4, 5, 6, 7, 8))
                        .center(this.createPoint(5.5, 6.6))
                        .cityName("city")
                        .countryName("country")
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
                .address(AddressEntity.builder()
                        .id(257L)
                        .boundingBox(this.createPolygon(1, 2, 3, 4, 5, 6, 7, 8))
                        .center(this.createPoint(5.5, 6.6))
                        .cityName("city")
                        .countryName("country")
                        .build())
                .build();

        assertNotNull(actual);
        checkEquals(expected, actual);
    }

    @Test
    public void entityWithLoadedPropertiesShouldBeMappedToDto() {
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
                .address(AddressEntity.builder()
                        .id(257L)
                        .boundingBox(this.createPolygon(1, 2, 3, 4, 5, 6, 7, 8))
                        .center(this.createPoint(5.5, 6.6))
                        .cityName("city")
                        .countryName("country")
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
                .address(Address.builder()
                        .id(257L)
                        .boundingBox(this.createPolygon(1, 2, 3, 4, 5, 6, 7, 8))
                        .center(this.createPoint(5.5, 6.6))
                        .cityName("city")
                        .countryName("country")
                        .build())
                .build();
        assertEquals(expected, actual);
    }

    @Test
    public void entityWithNotLoadedPropertiesShouldBeMappedToDto() {
        final List<ParameterEntity> givenParameters = emptyList();
        final TrackerEntity givenTracker = new TrackerEntity();
        final AddressEntity givenAddress = new AddressEntity();
        try (final MockedStatic<Hibernate> mockedStatic = mockStatic(Hibernate.class)) {
            mockedStatic
                    .when(() -> isInitialized(same(givenParameters)))
                    .thenReturn(false);
            mockedStatic
                    .when(() -> isInitialized(same(givenTracker)))
                    .thenReturn(false);
            mockedStatic
                    .when(() -> isInitialized(same(givenAddress)))
                    .thenReturn(false);

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
                    .parameters(givenParameters)
                    .tracker(givenTracker)
                    .address(givenAddress)
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
                    .parametersByNames(null)
                    .tracker(null)
                    .address(null)
                    .build();
            assertEquals(expected, actual);
        }
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
        checkEquals(expected.getAddress(), actual.getAddress());
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

    private static void checkEquals(final AddressEntity expected, final AddressEntity actual) {
        assertEquals(expected.getId(), actual.getId());
        assertEquals(expected.getBoundingBox(), actual.getBoundingBox());
        assertEquals(expected.getCenter(), actual.getCenter());
        assertEquals(expected.getCityName(), actual.getCityName());
        assertEquals(expected.getCountryName(), actual.getCountryName());
    }

    private Point createPoint(final double longitude, final double latitude) {
        final CoordinateXY coordinate = new CoordinateXY(longitude, latitude);
        return this.geometryFactory.createPoint(coordinate);
    }

    private Geometry createPolygon(final double firstLongitude, final double firstLatitude,
                                   final double secondLongitude, final double secondLatitude,
                                   final double thirdLongitude, final double thirdLatitude,
                                   final double fourthLongitude, final double fourthLatitude) {
        return this.geometryFactory.createPolygon(new Coordinate[]{
                        new CoordinateXY(firstLongitude, firstLatitude),
                        new CoordinateXY(secondLongitude, secondLatitude),
                        new CoordinateXY(thirdLongitude, thirdLatitude),
                        new CoordinateXY(fourthLongitude, fourthLatitude),
                        new CoordinateXY(firstLongitude, firstLatitude)
                }
        );
    }
}
