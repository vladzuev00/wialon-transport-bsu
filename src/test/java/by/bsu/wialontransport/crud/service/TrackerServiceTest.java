package by.bsu.wialontransport.crud.service;

import by.bsu.wialontransport.base.AbstractContextTest;
import by.bsu.wialontransport.crud.dto.Data;
import by.bsu.wialontransport.crud.dto.Tracker;
import by.bsu.wialontransport.crud.dto.User;
import by.bsu.wialontransport.model.Coordinate;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.jdbc.Sql;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static by.bsu.wialontransport.crud.entity.UserEntity.Role.USER;
import static java.lang.Long.MAX_VALUE;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public final class TrackerServiceTest extends AbstractContextTest {

    @Autowired
    private TrackerService service;

    @Test
    @Sql("classpath:sql/data/insert-data.sql")
    public void trackerShouldBeFoundByImeiFetchingLastData() {
        final Tracker actual = service.findByImeiFetchingLastData("11112222333344445555").orElseThrow();
        final Tracker expected = Tracker.builder()
                .id(255L)
                .imei("11112222333344445555")
                .password("$2a$10$8y9hC00YePN.9uH.OLCQ6OWeaR8G9q/U9MEvizLx9zaBkwe0KItHG")
                .phoneNumber("447336934")
                .lastData(
                        Data.builder()
                                .id(257L)
                                .dateTime(LocalDateTime.of(2019, 10, 26, 14, 39, 53))
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
                                .build()
                )
                .build();
        assertEquals(expected, actual);
    }

    @Test
    public void trackerShouldNotBeFoundByImeiFetchingLastData() {
        final Optional<Tracker> optionalActual = service.findByImeiFetchingLastData("00000000000000000000");
        assertTrue(optionalActual.isEmpty());
    }

    @Test
    public void allTrackersOrderedByImeiShouldBeFoundByUser() {
        final User givenUser = createUser(255L);

        final Page<Tracker> actual = service.findByUserOrderedByImei(givenUser);
        final List<Tracker> actualAsList = actual.toList();
        final List<Tracker> expectedAsList = List.of(
                Tracker.builder()
                        .id(255L)
                        .imei("11112222333344445555")
                        .password("$2a$10$8y9hC00YePN.9uH.OLCQ6OWeaR8G9q/U9MEvizLx9zaBkwe0KItHG")
                        .phoneNumber("447336934")
                        .build(),
                Tracker.builder()
                        .id(256L)
                        .imei("11112222333344445556")
                        .password("$2a$10$8y9hC00YePN.9uH.OLCQ6OWeaR8G9q/U9MEvizLx9zaBkwe0KItHG")
                        .phoneNumber("447336935")
                        .build()
        );
        assertEquals(expectedAsList, actualAsList);
    }

    @Test
    public void allTrackersOrderedByImeiShouldNotBeFoundByUser() {
        final User givenUser = createUser(256L);

        final Page<Tracker> actual = service.findByUserOrderedByImei(givenUser);
        assertTrue(actual.isEmpty());
    }

    @Test
    public void trackersOrderedByImeiShouldBeFoundByUser() {
        final User givenUser = createUser(255L);
        final PageRequest givenPageRequest = PageRequest.of(0, 5);

        final Page<Tracker> actual = service.findByUserOrderedByImei(givenUser, givenPageRequest);
        final List<Tracker> actualAsList = actual.toList();
        final List<Tracker> expectedAsList = List.of(
                Tracker.builder()
                        .id(255L)
                        .imei("11112222333344445555")
                        .password("$2a$10$8y9hC00YePN.9uH.OLCQ6OWeaR8G9q/U9MEvizLx9zaBkwe0KItHG")
                        .phoneNumber("447336934")
                        .build(),
                Tracker.builder()
                        .id(256L)
                        .imei("11112222333344445556")
                        .password("$2a$10$8y9hC00YePN.9uH.OLCQ6OWeaR8G9q/U9MEvizLx9zaBkwe0KItHG")
                        .phoneNumber("447336935")
                        .build()
        );
        assertEquals(expectedAsList, actualAsList);
    }

    @Test
    public void trackersOrderedByImeiShouldNotBeFoundByUser() {
        final User givenUser = createUser(256L);
        final PageRequest givenPageRequest = PageRequest.of(0, 5);

        final Page<Tracker> actual = service.findByUserOrderedByImei(givenUser, givenPageRequest);
        assertTrue(actual.isEmpty());
    }

    @Test
    public void trackerShouldBeFoundByIdFetchingUser() {
        final Optional<Tracker> optionalActual = service.findByIdFetching(255L);
        assertTrue(optionalActual.isPresent());

        final Tracker actual = optionalActual.get();
        final Tracker expected = Tracker.builder()
                .id(255L)
                .imei("11112222333344445555")
                .password("$2a$10$8y9hC00YePN.9uH.OLCQ6OWeaR8G9q/U9MEvizLx9zaBkwe0KItHG")
                .phoneNumber("447336934")
                .user(
                        User.builder()
                                .id(255L)
                                .email("vladzuev.00@mail.ru")
                                .password("$2a$10$8y9hC00YePN.9uH.OLCQ6OWeaR8G9q/U9MEvizLx9zaBkwe0KItHG")
                                .role(USER)
                                .build()
                )
                .build();
        assertEquals(expected, actual);
    }

    @Test
    public void trackerShouldNotBeFoundByIdFetchingUser() {
        final Optional<Tracker> optionalActual = service.findByIdFetching(MAX_VALUE);
        assertTrue(optionalActual.isEmpty());
    }

    @Test
    public void trackerShouldBeFoundByPhoneNumber() {
        final Optional<Tracker> optionalActual = service.findByPhoneNumber("447336934");
        assertTrue(optionalActual.isPresent());

        final Tracker actual = optionalActual.get();
        final Tracker expected = Tracker.builder()
                .id(255L)
                .imei("11112222333344445555")
                .password("$2a$10$8y9hC00YePN.9uH.OLCQ6OWeaR8G9q/U9MEvizLx9zaBkwe0KItHG")
                .phoneNumber("447336934")
                .build();
        assertEquals(expected, actual);
    }

    @Test
    public void trackerShouldNotBeFoundByPhoneNumber() {
        final Optional<Tracker> optionalActual = service.findByPhoneNumber("997336935");
        assertTrue(optionalActual.isEmpty());
    }

    private static User createUser(final Long id) {
        return User.builder()
                .id(id)
                .build();
    }
}
