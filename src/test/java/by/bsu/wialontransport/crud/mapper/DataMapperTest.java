//package by.bsu.wialontransport.crud.mapper;
//
//import by.bsu.wialontransport.base.AbstractContextTest;
//import by.bsu.wialontransport.crud.dto.Address;
//import by.bsu.wialontransport.crud.dto.Data;
//import by.bsu.wialontransport.crud.dto.Parameter;
//import by.bsu.wialontransport.crud.dto.Tracker;
//import by.bsu.wialontransport.crud.entity.AddressEntity;
//import by.bsu.wialontransport.crud.entity.DataEntity;
//import by.bsu.wialontransport.crud.entity.ParameterEntity;
//import by.bsu.wialontransport.crud.entity.TrackerEntity;
//import by.bsu.wialontransport.model.Coordinate;
//import by.bsu.wialontransport.util.entity.DataEntityUtil;
//import org.hibernate.Hibernate;
//import org.junit.Test;
//import org.locationtech.jts.geom.GeometryFactory;
//import org.mockito.MockedStatic;
//import org.springframework.beans.factory.annotation.Autowired;
//
//import java.time.LocalDateTime;
//import java.util.List;
//import java.util.Map;
//
//import static by.bsu.wialontransport.crud.entity.ParameterEntity.Type.INTEGER;
//import static by.bsu.wialontransport.crud.entity.ParameterEntity.Type.STRING;
//import static by.bsu.wialontransport.util.GeometryTestUtil.createPoint;
//import static by.bsu.wialontransport.util.GeometryTestUtil.createPolygon;
//import static java.util.Collections.emptyList;
//import static org.hibernate.Hibernate.isInitialized;
//import static org.junit.Assert.assertEquals;
//import static org.junit.Assert.assertNotNull;
//import static org.mockito.ArgumentMatchers.same;
//import static org.mockito.Mockito.mockStatic;
//
//public final class DataMapperTest extends AbstractContextTest {
//
//    @Autowired
//    private DataMapper mapper;
//
//    @Autowired
//    private GeometryFactory geometryFactory;
//
//    @Test
//    public void dtoShouldBeMappedToEntity() {
//        final Data givenDto = Data.builder()
//                .id(255L)
//                .dateTime(LocalDateTime.of(2023, 1, 8, 4, 18, 15))
//                .coordinate(new Coordinate(5.5, 6.6))
//                .speed(26)
//                .course(27)
//                .altitude(28)
//                .amountOfSatellites(29)
//                .reductionPrecision(30.5)
//                .inputs(31)
//                .outputs(32)
//                .analogInputs(new double[]{0.2, 0.3, 0.4})
//                .driverKeyCode("driver key code")
//                .parametersByNames(Map.of(
//                        "first-param", new Parameter(256L, "first-param", INTEGER, "44"),
//                        "second-param", new Parameter(257L, "second-param", STRING, "text")
//                ))
//                .tracker(Tracker.builder()
//                        .id(256L)
//                        .imei("11111222223333344444")
//                        .password("password")
//                        .phoneNumber("447336934")
//                        .build())
//                .address(Address.builder()
//                        .id(257L)
//                        .boundingBox(createPolygon(this.geometryFactory, 1, 2, 3, 4, 5, 6, 7, 8))
//                        .center(createPoint(this.geometryFactory, 5.5, 6.6))
//                        .cityName("city")
//                        .countryName("country")
//                        .build())
//                .build();
//
//        final DataEntity actual = this.mapper.mapToEntity(givenDto);
//        final DataEntity expected = DataEntity.builder()
//                .id(255L)
//                .dateTime(LocalDateTime.of(2023, 1, 8, 4, 18, 15))
//                .coordinate(new DataEntity.Coordinate(5.5, 6.6))
//                .speed(26)
//                .course(27)
//                .altitude(28)
//                .amountOfSatellites(29)
//                .reductionPrecision(30.5)
//                .inputs(31)
//                .outputs(32)
//                .analogInputs(new double[]{0.2, 0.3, 0.4})
//                .driverKeyCode("driver key code")
//                .parameters(List.of(
//                        new ParameterEntity(256L, "first-param", INTEGER, "44", null),
//                        new ParameterEntity(257L, "second-param", STRING, "text", null)
//                ))
//                .tracker(TrackerEntity.builder()
//                        .id(256L)
//                        .imei("11111222223333344444")
//                        .password("password")
//                        .phoneNumber("447336934")
//                        .build())
//                .address(AddressEntity.builder()
//                        .id(257L)
//                        .boundingBox(createPolygon(this.geometryFactory, 1, 2, 3, 4, 5, 6, 7, 8))
//                        .center(createPoint(this.geometryFactory, 5.5, 6.6))
//                        .cityName("city")
//                        .countryName("country")
//                        .build())
//                .build();
//
//        assertNotNull(actual);
//        DataEntityUtil.checkEquals(expected, actual);
//    }
//
//    @Test
//    public void entityWithLoadedPropertiesShouldBeMappedToDto() {
//        final DataEntity givenEntity = DataEntity.builder()
//                .id(255L)
//                .dateTime(LocalDateTime.of(2023, 1, 8, 4, 18, 15))
//                .coordinate(new DataEntity.Coordinate(5.5, 6.6))
//                .speed(26)
//                .course(27)
//                .altitude(28)
//                .amountOfSatellites(29)
//                .reductionPrecision(30.5)
//                .inputs(31)
//                .outputs(32)
//                .analogInputs(new double[]{0.2, 0.3, 0.4})
//                .driverKeyCode("driver key code")
//                .parameters(List.of(
//                        new ParameterEntity(256L, "first-param", INTEGER, "44", null),
//                        new ParameterEntity(257L, "second-param", STRING, "text", null)
//                ))
//                .tracker(TrackerEntity.builder()
//                        .id(256L)
//                        .imei("11111222223333344444")
//                        .password("password")
//                        .phoneNumber("447336934")
//                        .build())
//                .address(AddressEntity.builder()
//                        .id(257L)
//                        .boundingBox(createPolygon(this.geometryFactory, 1, 2, 3, 4, 5, 6, 7, 8))
//                        .center(createPoint(this.geometryFactory, 5.5, 6.6))
//                        .cityName("city")
//                        .countryName("country")
//                        .build())
//                .build();
//
//        final Data actual = this.mapper.mapToDto(givenEntity);
//        final Data expected = Data.builder()
//                .id(255L)
//                .dateTime(LocalDateTime.of(2023, 1, 8, 4, 18, 15))
//                .coordinate(new Coordinate(5.5, 6.6))
//                .speed(26)
//                .course(27)
//                .altitude(28)
//                .amountOfSatellites(29)
//                .reductionPrecision(30.5)
//                .inputs(31)
//                .outputs(32)
//                .analogInputs(new double[]{0.2, 0.3, 0.4})
//                .driverKeyCode("driver key code")
//                .parametersByNames(Map.of(
//                        "first-param", new Parameter(256L, "first-param", INTEGER, "44"),
//                        "second-param", new Parameter(257L, "second-param", STRING, "text")
//                ))
//                .tracker(Tracker.builder()
//                        .id(256L)
//                        .imei("11111222223333344444")
//                        .password("password")
//                        .phoneNumber("447336934")
//                        .build())
//                .address(Address.builder()
//                        .id(257L)
//                        .boundingBox(createPolygon(this.geometryFactory, 1, 2, 3, 4, 5, 6, 7, 8))
//                        .center(createPoint(this.geometryFactory, 5.5, 6.6))
//                        .cityName("city")
//                        .countryName("country")
//                        .build())
//                .build();
//        assertEquals(expected, actual);
//    }
//
//    @Test
//    public void entityWithNotLoadedPropertiesShouldBeMappedToDto() {
//        final List<ParameterEntity> givenParameters = emptyList();
//        final TrackerEntity givenTracker = new TrackerEntity();
//        final AddressEntity givenAddress = new AddressEntity();
//        try (final MockedStatic<Hibernate> mockedStatic = mockStatic(Hibernate.class)) {
//            mockedStatic
//                    .when(() -> isInitialized(same(givenParameters)))
//                    .thenReturn(false);
//            mockedStatic
//                    .when(() -> isInitialized(same(givenTracker)))
//                    .thenReturn(false);
//            mockedStatic
//                    .when(() -> isInitialized(same(givenAddress)))
//                    .thenReturn(false);
//
//            final DataEntity givenEntity = DataEntity.builder()
//                    .id(255L)
//                    .dateTime(LocalDateTime.of(2023, 1, 8, 4, 18, 15))
//                    .coordinate(new DataEntity.Coordinate(5.5, 6.6))
//                    .speed(26)
//                    .course(27)
//                    .altitude(28)
//                    .amountOfSatellites(29)
//                    .reductionPrecision(30.5)
//                    .inputs(31)
//                    .outputs(32)
//                    .analogInputs(new double[]{0.2, 0.3, 0.4})
//                    .driverKeyCode("driver key code")
//                    .parameters(givenParameters)
//                    .tracker(givenTracker)
//                    .address(givenAddress)
//                    .build();
//
//            final Data actual = this.mapper.mapToDto(givenEntity);
//            final Data expected = Data.builder()
//                    .id(255L)
//                    .dateTime(LocalDateTime.of(2023, 1, 8, 4, 18, 15))
//                    .coordinate(new Coordinate(5.5, 6.6))
//                    .speed(26)
//                    .course(27)
//                    .altitude(28)
//                    .amountOfSatellites(29)
//                    .reductionPrecision(30.5)
//                    .inputs(31)
//                    .outputs(32)
//                    .analogInputs(new double[]{0.2, 0.3, 0.4})
//                    .driverKeyCode("driver key code")
//                    .parametersByNames(null)
//                    .tracker(null)
//                    .address(null)
//                    .build();
//            assertEquals(expected, actual);
//        }
//    }
//}
