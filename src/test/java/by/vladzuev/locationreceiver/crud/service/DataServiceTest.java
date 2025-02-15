package by.vladzuev.locationreceiver.crud.service;

import by.vladzuev.locationreceiver.base.AbstractSpringBootTest;
import by.vladzuev.locationreceiver.crud.dto.Address;
import by.vladzuev.locationreceiver.crud.dto.Location;
import by.vladzuev.locationreceiver.crud.dto.Tracker;
import by.vladzuev.locationreceiver.crud.dto.User;
import by.vladzuev.locationreceiver.model.GpsCoordinate;
import by.vladzuev.locationreceiver.model.DateInterval;
import by.vladzuev.locationreceiver.util.GeometryTestUtil;
import by.vladzuev.locationreceiver.util.StreamUtil;
import org.junit.Test;
import org.locationtech.jts.geom.GeometryFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

import static by.vladzuev.locationreceiver.util.GeometryTestUtil.createPolygon;
import static java.lang.Long.MIN_VALUE;
import static java.util.stream.Collectors.toSet;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public final class DataServiceTest extends AbstractSpringBootTest {

    @Autowired
    private LocationService service;

    @Autowired
    private GeometryFactory geometryFactory;

    @Test
    @Sql("classpath:sql/data/insert-data.sql")
    public void trackerLastDataShouldBeFoundFetchingParameters() {
        final Tracker givenTracker = createTracker(255L);

        final Optional<Location> optionalActual = service.findLastFetchingParameters(givenTracker);
        assertTrue(optionalActual.isPresent());
        final Location actual = optionalActual.get();

        final Long actualId = actual.getId();
        final Long expectedId = 257L;
        assertEquals(expectedId, actualId);
    }

    @Test
    public void trackerLastDataShouldNotBeFoundFetchingParametersBecauseOfThereIsNoDataFromGivenTracker() {
        final Tracker givenTracker = createTracker(255L);

        final Optional<Location> optionalActual = service.findLastFetchingParameters(givenTracker);
        assertTrue(optionalActual.isEmpty());
    }

    @Test
    public void trackerLastDataShouldNotBeFoundFetchingParametersBecauseOfThereIsSuchTracker() {
        final Tracker givenTracker = createTracker(MIN_VALUE);

        final Optional<Location> optionalActual = service.findLastFetchingParameters(givenTracker);
        assertTrue(optionalActual.isEmpty());
    }

    @Test
    @Sql("classpath:sql/data/insert-data.sql")
    public void dataShouldBeFoundByUserAndDateIntervalFetchingTrackerAndAddress() {
        final User givenUser = createUser(255L);
        final DateInterval givenDateInterval = new DateInterval(
                LocalDateTime.of(2019, 10, 23, 14, 39, 50),
                LocalDateTime.of(2019, 10, 24, 14, 39, 51)
        );

        try (
                final Stream<Location> actual = service.findDataByUserIdFetchingTrackerAndAddress(
                        givenUser,
                        givenDateInterval
                )
        ) {
            final Set<Location> actualAsSet = actual.collect(toSet());
            final Set<Location> expectedAsSet = Set.of(
                    Location.builder()
                            .id(254L)
                            .dateTime(LocalDateTime.of(2019, 10, 23, 14, 39, 50))
                            .coordinate(new GpsCoordinate(53.233, 27.3434))
                            .speed(8)
                            .course(9)
                            .altitude(10)
                            .satelliteCount(11)
                            .hdop(12.4)
                            .inputs(13)
                            .outputs(14)
                            .analogInputs(new double[]{0.2, 0.3, 0.4})
                            .driverKeyCode("driver key code")
                            .tracker(
                                    Tracker.builder()
                                            .id(255L)
                                            .imei("11112222333344445555")
                                            .password("$2a$10$8y9hC00YePN.9uH.OLCQ6OWeaR8G9q/U9MEvizLx9zaBkwe0KItHG")
                                            .phoneNumber("447336934")
                                            .build()
                            )
                            .address(
                                    Address.builder()
                                            .id(258L)
                                            .boundingBox(GeometryTestUtil.createPolygon(geometryFactory, 1, 2, 3, 4, 5, 6, 6, 7))
                                            .center(GeometryTestUtil.createPoint(geometryFactory, 53.050286, 24.873635))
                                            .cityName("city")
                                            .countryName("country")
                                            .geometry(GeometryTestUtil.createPolygon(geometryFactory, 1, 2, 3, 4, 5, 6))
                                            .build()
                            )
                            .build(),
                    Location.builder()
                            .id(255L)
                            .dateTime(LocalDateTime.of(2019, 10, 24, 14, 39, 51))
                            .coordinate(new GpsCoordinate(53.232, 27.3433))
                            .speed(8)
                            .course(9)
                            .altitude(10)
                            .satelliteCount(11)
                            .hdop(12.4)
                            .inputs(13)
                            .outputs(14)
                            .analogInputs(new double[]{0.2, 0.3, 0.4})
                            .driverKeyCode("driver key code")
                            .tracker(
                                    Tracker.builder()
                                            .id(255L)
                                            .imei("11112222333344445555")
                                            .password("$2a$10$8y9hC00YePN.9uH.OLCQ6OWeaR8G9q/U9MEvizLx9zaBkwe0KItHG")
                                            .phoneNumber("447336934")
                                            .build()
                            )
                            .address(
                                    Address.builder()
                                            .id(258L)
                                            .boundingBox(GeometryTestUtil.createPolygon(geometryFactory, 1, 2, 3, 4, 5, 6, 6, 7))
                                            .center(GeometryTestUtil.createPoint(geometryFactory, 53.050286, 24.873635))
                                            .cityName("city")
                                            .countryName("country")
                                            .geometry(GeometryTestUtil.createPolygon(geometryFactory, 1, 2, 3, 4, 5, 6))
                                            .build()
                            )
                            .build()
            );
            assertEquals(expectedAsSet, actualAsSet);
        }
    }

    @Test
    public void dataShouldNotBeFoundByUserAndDateIntervalFetchingTrackerAndAddress() {
        final User givenUser = createUser(256L);
        final DateInterval givenDateInterval = new DateInterval(
                LocalDateTime.of(2019, 10, 23, 14, 39, 50),
                LocalDateTime.of(2019, 10, 24, 14, 39, 51)
        );

        try (
                final Stream<Location> actual = service.findDataByUserIdFetchingTrackerAndAddress(
                        givenUser,
                        givenDateInterval
                )
        ) {
            assertTrue(StreamUtil.isEmpty(actual));
        }
    }

    @Test
    @Sql("classpath:sql/data/insert-data.sql")
    public void trackerLastDataDateTimeShouldBeFound() {
        final Tracker givenTracker = createTracker(255L);

        final Optional<LocalDateTime> optionalActual = service.findTrackerLastDataDateTime(givenTracker);
        assertTrue(optionalActual.isPresent());
        final LocalDateTime actual = optionalActual.get();
        final LocalDateTime expected = LocalDateTime.of(2019, 10, 26, 14, 39, 53);
        assertEquals(expected, actual);
    }

    @Test
    @Sql("classpath:sql/data/insert-data.sql")
    public void trackerLastDataDateTimeShouldNotBeFoundBecauseOfThereIsNoDataFromGivenTracker() {
        final Tracker givenTracker = createTracker(256L);

        final Optional<LocalDateTime> optionalActual = service.findTrackerLastDataDateTime(givenTracker);
        assertTrue(optionalActual.isEmpty());
    }

    @Test
    @Sql("classpath:sql/data/insert-data.sql")
    public void trackerLastDataDateTimeShouldNotBeFoundBecauseOfThereIsNoTrackerWithGivenId() {
        final Tracker givenTracker = createTracker(MIN_VALUE);

        final Optional<LocalDateTime> optionalActual = service.findTrackerLastDataDateTime(givenTracker);
        assertTrue(optionalActual.isEmpty());
    }

    @Test
    @Sql("classpath:sql/data/insert-data.sql")
    public void trackerLastDataShouldBeFound() {
        final Tracker givenTracker = createTracker(255L);

        final Optional<Location> optionalActual = service.findTrackerLastData(givenTracker);
        assertTrue(optionalActual.isPresent());
        final Location actual = optionalActual.get();

        final Long actualId = actual.getId();
        final Long expectedId = 257L;
        assertEquals(expectedId, actualId);
    }

    @Test
    public void trackerLastDataShouldNotBeFoundBecauseOfThereIsNoDataFromGivenTracker() {
        final Tracker givenTracker = createTracker(255L);

        final Optional<Location> optionalActual = service.findTrackerLastData(givenTracker);
        assertTrue(optionalActual.isEmpty());
    }

    @Test
    public void trackerLastDataShouldNotBeFoundBecauseOfThereIsSuchTracker() {
        final Tracker givenTracker = createTracker(MIN_VALUE);

        final Optional<Location> optionalActual = service.findTrackerLastData(givenTracker);
        assertTrue(optionalActual.isEmpty());
    }

    @SuppressWarnings("SameParameterValue")
    private static Tracker createTracker(final Long id) {
        return Tracker.builder()
                .id(id)
                .build();
    }

    private static User createUser(final Long id) {
        return User.builder()
                .id(id)
                .build();
    }
}
