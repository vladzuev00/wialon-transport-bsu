package by.bsu.wialontransport.crud.service;

import by.bsu.wialontransport.base.AbstractContextTest;
import by.bsu.wialontransport.crud.dto.Address;
import by.bsu.wialontransport.crud.dto.Data;
import by.bsu.wialontransport.crud.dto.Tracker;
import by.bsu.wialontransport.crud.dto.User;
import by.bsu.wialontransport.model.Coordinate;
import by.bsu.wialontransport.model.DateInterval;
import org.junit.Test;
import org.locationtech.jts.geom.GeometryFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

import static by.bsu.wialontransport.util.GeometryTestUtil.createPoint;
import static by.bsu.wialontransport.util.GeometryTestUtil.createPolygon;
import static by.bsu.wialontransport.util.StreamUtil.isEmpty;
import static java.util.stream.Collectors.toSet;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public final class DataServiceTest extends AbstractContextTest {

    @Autowired
    private DataService dataService;

    @Autowired
    private GeometryFactory geometryFactory;

    @Test
    @Sql("classpath:sql/data/insert-data.sql")
    public void trackerLastDataShouldBeFoundByTrackerId() {
        final Tracker givenTracker = createTracker(255L);

        final Optional<Data> optionalActual = dataService.findTrackerLastData(givenTracker);
        final Long actualId = optionalActual.map(Data::getId).orElseThrow();
        final Long expectedId = 257L;
        assertEquals(expectedId, actualId);
    }

    @Test
    public void trackerLastDataShouldNotBeFoundByTrackerId() {
        final Tracker givenTracker = createTracker(255L);

        final Optional<Data> optionalActual = dataService.findTrackerLastData(givenTracker);
        assertTrue(optionalActual.isEmpty());
    }

    @Test
    @Sql("classpath:sql/data/insert-data.sql")
    public void dataWithTrackerAndAddressShouldBeFoundByUserAndDateInterval() {
        final User givenUser = createUser(255L);
        final DateInterval givenDateInterval = new DateInterval(
                LocalDateTime.of(2019, 10, 23, 14, 39, 50),
                LocalDateTime.of(2019, 10, 24, 14, 39, 51)
        );

        try (final Stream<Data> actual = dataService.findDataWithTrackerAndAddress(givenUser, givenDateInterval)) {
            final Set<Data> actualAsSet = actual.collect(toSet());
            final Set<Data> expectedAsSet = Set.of(
                    Data.builder()
                            .id(254L)
                            .dateTime(LocalDateTime.of(2019, 10, 23, 14, 39, 50))
                            .coordinate(new Coordinate(53.233, 27.3434))
                            .speed(8)
                            .course(9)
                            .altitude(10)
                            .amountOfSatellites(11)
                            .reductionPrecision(12.4)
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
                                            .boundingBox(createPolygon(geometryFactory, 1, 2, 3, 4, 5, 6, 6, 7))
                                            .center(createPoint(geometryFactory, 53.050286, 24.873635))
                                            .cityName("city")
                                            .countryName("country")
                                            .geometry(createPolygon(geometryFactory, 1, 2, 3, 4, 5, 6))
                                            .build()
                            )
                            .build(),
                    Data.builder()
                            .id(255L)
                            .dateTime(LocalDateTime.of(2019, 10, 24, 14, 39, 51))
                            .coordinate(new Coordinate(53.232, 27.3433))
                            .speed(8)
                            .course(9)
                            .altitude(10)
                            .amountOfSatellites(11)
                            .reductionPrecision(12.4)
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
                                            .boundingBox(createPolygon(geometryFactory, 1, 2, 3, 4, 5, 6, 6, 7))
                                            .center(createPoint(geometryFactory, 53.050286, 24.873635))
                                            .cityName("city")
                                            .countryName("country")
                                            .geometry(createPolygon(geometryFactory, 1, 2, 3, 4, 5, 6))
                                            .build()
                            )
                            .build()
            );
            assertEquals(expectedAsSet, actualAsSet);
        }
    }

    @Test
    public void dataWithTrackerAndAddressShouldNotBeFoundByUser() {
        final User givenUser = createUser(256L);
        final DateInterval givenDateInterval = new DateInterval(
                LocalDateTime.of(2019, 10, 23, 14, 39, 50),
                LocalDateTime.of(2019, 10, 24, 14, 39, 51)
        );

        try (final Stream<Data> actual = dataService.findDataWithTrackerAndAddress(givenUser, givenDateInterval)) {
            assertTrue(isEmpty(actual));
        }
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
