//package by.bsu.wialontransport.crud.repository;
//
//import by.bsu.wialontransport.base.AbstractSpringBootTest;
//import by.bsu.wialontransport.crud.entity.AddressEntity;
//import by.bsu.wialontransport.crud.entity.DataEntity;
//import by.bsu.wialontransport.crud.entity.DataEntity.Coordinate;
//import by.bsu.wialontransport.crud.entity.ParameterEntity;
//import by.bsu.wialontransport.crud.entity.TrackerEntity;
//import by.bsu.wialontransport.util.entity.EntityUtil;
//import org.junit.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.test.context.jdbc.Sql;
//
//import java.time.LocalDateTime;
//import java.util.List;
//import java.util.Optional;
//import java.util.Set;
//import java.util.stream.Stream;
//
//import static by.bsu.wialontransport.crud.entity.ParameterEntity.Type.INTEGER;
//import static by.bsu.wialontransport.util.StreamUtil.isEmpty;
//import static by.bsu.wialontransport.util.entity.DataEntityUtil.*;
//import static java.lang.Long.MAX_VALUE;
//import static java.lang.Long.MIN_VALUE;
//import static java.util.Collections.emptyList;
//import static java.util.stream.Collectors.toSet;
//import static org.junit.Assert.*;
//
//public final class DataRepositoryTest extends AbstractSpringBootTest {
//
//    @Autowired
//    private DataRepository repository;
//
//    @Test
//    @Sql("classpath:sql/data/insert-data.sql")
//    public void dataShouldBeFoundById() {
//        final Long givenId = 256L;
//
//        startQueryCount();
//        final Optional<DataEntity> optionalActual = repository.findById(givenId);
//        checkQueryCount(1);
//
//        assertTrue(optionalActual.isPresent());
//        final DataEntity actual = optionalActual.get();
//        assertFalse(areParametersFetched(actual));
//        assertFalse(isTrackerFetched(actual));
//        assertFalse(isAddressFetched(actual));
//
//        final DataEntity expected = DataEntity.builder()
//                .id(givenId)
//                .dateTime(LocalDateTime.of(2019, 10, 24, 14, 39, 52))
//                .coordinate(new Coordinate(53.233, 27.3434))
//                .speed(8)
//                .course(9)
//                .altitude(10)
//                .amountOfSatellites(11)
//                .hdop(12.4)
//                .inputs(13)
//                .outputs(14)
//                .analogInputs(new double[]{0.2, 0.3, 0.4})
//                .driverKeyCode("driver key code")
//                .parameters(List.of(entityManager.getReference(ParameterEntity.class, 257L)))
//                .tracker(entityManager.getReference(TrackerEntity.class, 255L))
//                .address(entityManager.getReference(AddressEntity.class, 258L))
//                .build();
//        checkEquals(expected, actual);
//    }
//
//    @Test
//    @Sql("classpath:sql/data/insert-data.sql")
//    public void dataShouldBeInserted() {
//        final DataEntity givenData = DataEntity.builder()
//                .dateTime(LocalDateTime.of(2019, 10, 24, 14, 39, 53))
//                .coordinate(new Coordinate(53.233, 27.3434))
//                .speed(8)
//                .course(9)
//                .altitude(10)
//                .amountOfSatellites(11)
//                .hdop(12.4)
//                .inputs(13)
//                .outputs(14)
//                .analogInputs(new double[]{0.2, 0.3, 0.4})
//                .driverKeyCode("driver key code")
//                .tracker(entityManager.getReference(TrackerEntity.class, 255L))
//                .address(entityManager.getReference(AddressEntity.class, 258L))
//                .build();
//        final List<ParameterEntity> givenParameters = List.of(ParameterEntity.builder()
//                .name("name")
//                .type(INTEGER)
//                .value("44")
//                .data(givenData)
//                .build()
//        );
//        givenData.setParameters(givenParameters);
//
//        startQueryCount();
//        repository.save(givenData);
//        checkQueryCount(4);
//    }
//
//    @Test
//    @Sql("classpath:sql/data/insert-data.sql")
//    public void trackerLastDataShouldBeFoundByTrackerIdFetchingParameters() {
//        final Long givenTrackerId = 255L;
//
//        startQueryCount();
//        final Optional<DataEntity> optionalActual = repository.findTrackerLastDataByTrackerIdFetchingParameters(
//                givenTrackerId
//        );
//        checkQueryCount(1);
//
//        assertTrue(optionalActual.isPresent());
//        final DataEntity actual = optionalActual.get();
//        assertTrue(areParametersFetched(actual));
//        assertFalse(isTrackerFetched(actual));
//        assertFalse(isAddressFetched(actual));
//
//        final DataEntity expected = DataEntity.builder()
//                .id(257L)
//                .dateTime(LocalDateTime.of(2019, 10, 26, 14, 39, 53))
//                .coordinate(new Coordinate(53.233, 27.3434))
//                .speed(8)
//                .course(9)
//                .altitude(10)
//                .amountOfSatellites(11)
//                .hdop(12.4)
//                .inputs(13)
//                .outputs(14)
//                .analogInputs(new double[]{0.2, 0.3, 0.4})
//                .driverKeyCode("driver key code")
//                .parameters(emptyList())
//                .tracker(entityManager.getReference(TrackerEntity.class, givenTrackerId))
//                .address(entityManager.getReference(AddressEntity.class, 258L))
//                .build();
//        checkEquals(expected, actual);
//    }
//
//    @Test
//    public void trackerLastDataShouldNotBeFoundByTrackerIdFetchingParametersBecauseOfTrackerWithGivenIdDoesNotHaveData() {
//        final Long givenTrackerId = 256L;
//
//        startQueryCount();
//        final Optional<DataEntity> optionalActual = repository.findTrackerLastDataByTrackerIdFetchingParameters(
//                givenTrackerId
//        );
//        checkQueryCount(1);
//
//        assertTrue(optionalActual.isEmpty());
//    }
//
//    @Test
//    public void trackerLastDataShouldNotBeFoundByTrackerIdFetchingParametersBecauseOfTrackerWithGivenIdDoesNotExist() {
//        final Long givenTrackerId = MAX_VALUE;
//
//        startQueryCount();
//        final Optional<DataEntity> optionalActual = repository.findTrackerLastDataByTrackerIdFetchingParameters(
//                givenTrackerId
//        );
//        checkQueryCount(1);
//
//        assertTrue(optionalActual.isEmpty());
//    }
//
//    @Test
//    @Sql("classpath:sql/data/insert-data.sql")
//    public void dataShouldBeFoundByUserIdFetchingTrackerAndAddress() {
//        final Long givenUserId = 255L;
//        final LocalDateTime givenStartDateTime = LocalDateTime.of(
//                2019,
//                10,
//                23,
//                0,
//                0,
//                0);
//        final LocalDateTime givenEndDateTime = LocalDateTime.of(
//                2019,
//                10,
//                25,
//                0,
//                0,
//                0
//        );
//
//        startQueryCount();
//        try (
//                final Stream<DataEntity> actual = repository.findDataByUserIdFetchingTrackerAndAddress(
//                        givenUserId,
//                        givenStartDateTime,
//                        givenEndDateTime
//                )
//        ) {
//            checkQueryCount(1);
//
//            final Set<DataEntity> actualAsSet = actual.collect(toSet());
//
//            assertTrue(areParametersNotFetched(actualAsSet));
//            assertTrue(areTrackersFetched(actualAsSet));
//            assertTrue(areAddressesFetched(actualAsSet));
//
//            final Set<Long> actualIds = EntityUtil.mapToIdsSet(actualAsSet);
//            final Set<Long> expectedIds = Set.of(254L, 255L, 256L);
//            assertEquals(expectedIds, actualIds);
//        }
//    }
//
//    @Test
//    @Sql("classpath:sql/data/insert-data.sql")
//    public void dataShouldNotBeFoundByUserIdFetchingTrackerAndAddress() {
//        final Long givenUserId = 255L;
//        final LocalDateTime givenStartDateTime = LocalDateTime.of(
//                2015,
//                10,
//                23,
//                0,
//                0,
//                0
//        );
//        final LocalDateTime givenEndDateTime = LocalDateTime.of(
//                2015,
//                10,
//                25,
//                0,
//                0,
//                0
//        );
//
//        startQueryCount();
//        try (
//                final Stream<DataEntity> actual = repository.findDataByUserIdFetchingTrackerAndAddress(
//                        givenUserId,
//                        givenStartDateTime,
//                        givenEndDateTime
//                )
//        ) {
//            checkQueryCount(1);
//            assertTrue(isEmpty(actual));
//        }
//    }
//
//    @Test
//    @Sql("classpath:sql/data/insert-data.sql")
//    public void trackerLastDataDateTimeShouldBeFoundByTrackerId() {
//        final Long givenTrackerId = 255L;
//
//        startQueryCount();
//        final Optional<LocalDateTime> optionalActual = repository.findTrackerLastDataDateTimeByTrackerId(givenTrackerId);
//        checkQueryCount(1);
//
//        assertTrue(optionalActual.isPresent());
//        final LocalDateTime actual = optionalActual.get();
//        final LocalDateTime expected = LocalDateTime.of(2019, 10, 26, 14, 39, 53);
//        assertEquals(expected, actual);
//    }
//
//    @Test
//    @Sql("classpath:sql/data/insert-data.sql")
//    public void trackerLastDataDateTimeShouldNotBeFoundByTrackerIdBecauseOfThereIsNoDataFromGivenTracker() {
//        final Long givenTrackerId = 256L;
//
//        startQueryCount();
//        final Optional<LocalDateTime> optionalActual = repository.findTrackerLastDataDateTimeByTrackerId(givenTrackerId);
//        checkQueryCount(1);
//
//        assertTrue(optionalActual.isEmpty());
//    }
//
//    @Test
//    @Sql("classpath:sql/data/insert-data.sql")
//    public void trackerLastDataDateTimeShouldNotBeFoundByTrackerIdBecauseOfThereIsNoTrackerWithGivenId() {
//        startQueryCount();
//        final Optional<LocalDateTime> optionalActual = repository.findTrackerLastDataDateTimeByTrackerId(MIN_VALUE);
//        checkQueryCount(1);
//
//        assertTrue(optionalActual.isEmpty());
//    }
//
//    @Test
//    @Sql("classpath:sql/data/insert-data.sql")
//    public void trackerLastDataShouldBeFoundByTrackerId() {
//        final Long givenTrackerId = 255L;
//
//        startQueryCount();
//        final Optional<DataEntity> optionalActual = repository.findTrackerLastDataByTrackerId(givenTrackerId);
//        checkQueryCount(1);
//
//        assertTrue(optionalActual.isPresent());
//        final DataEntity actual = optionalActual.get();
//        assertFalse(areParametersFetched(actual));
//        assertFalse(isTrackerFetched(actual));
//        assertFalse(isAddressFetched(actual));
//
//        final DataEntity expected = DataEntity.builder()
//                .id(257L)
//                .dateTime(LocalDateTime.of(2019, 10, 26, 14, 39, 53))
//                .coordinate(new Coordinate(53.233, 27.3434))
//                .speed(8)
//                .course(9)
//                .altitude(10)
//                .amountOfSatellites(11)
//                .hdop(12.4)
//                .inputs(13)
//                .outputs(14)
//                .analogInputs(new double[]{0.2, 0.3, 0.4})
//                .driverKeyCode("driver key code")
//                .parameters(emptyList())
//                .tracker(entityManager.getReference(TrackerEntity.class, givenTrackerId))
//                .address(entityManager.getReference(AddressEntity.class, 258L))
//                .build();
//        checkEquals(expected, actual);
//    }
//
//    @Test
//    public void trackerLastDataShouldNotBeFoundByTrackerIdBecauseOfTrackerWithGivenIdDoesNotHaveData() {
//        final Long givenTrackerId = 256L;
//
//        startQueryCount();
//        final Optional<DataEntity> optionalActual = repository.findTrackerLastDataByTrackerId(givenTrackerId);
//        checkQueryCount(1);
//
//        assertTrue(optionalActual.isEmpty());
//    }
//
//    @Test
//    public void trackerLastDataShouldNotBeFoundByTrackerIdBecauseOfTrackerWithGivenIdDoesNotExist() {
//        final Long givenTrackerId = MAX_VALUE;
//
//        startQueryCount();
//        final Optional<DataEntity> optionalActual = repository.findTrackerLastDataByTrackerId(givenTrackerId);
//        checkQueryCount(1);
//
//        assertTrue(optionalActual.isEmpty());
//    }
//}
