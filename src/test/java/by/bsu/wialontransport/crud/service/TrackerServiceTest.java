package by.bsu.wialontransport.crud.service;

import by.bsu.wialontransport.base.AbstractContextTest;
import by.bsu.wialontransport.crud.dto.Tracker;
import by.bsu.wialontransport.crud.dto.TrackerMileage;
import by.bsu.wialontransport.crud.dto.User;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

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
    public void trackerShouldBeFoundByImei() {
        final String givenImei = "11112222333344445555";

        final Optional<Tracker> optionalActual = service.findByImei(givenImei);
        assertTrue(optionalActual.isPresent());
        final Tracker actual = optionalActual.get();
        final Tracker expected = Tracker.builder()
                .id(255L)
                .imei(givenImei)
                .password("$2a$10$8y9hC00YePN.9uH.OLCQ6OWeaR8G9q/U9MEvizLx9zaBkwe0KItHG")
                .phoneNumber("447336934")
                .build();
        assertEquals(expected, actual);
    }

    @Test
    public void trackerShouldNotBeFoundByImei() {
        final String givenImei = "00000000000000000000";

        final Optional<Tracker> optionalActual = service.findByImei(givenImei);
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
        final Long givenId = 255L;

        final Optional<Tracker> optionalActual = service.findByIdFetchingUser(givenId);
        assertTrue(optionalActual.isPresent());

        final Tracker actual = optionalActual.get();
        final Tracker expected = Tracker.builder()
                .id(givenId)
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
        final Optional<Tracker> optionalActual = service.findByIdFetchingUser(MAX_VALUE);
        assertTrue(optionalActual.isEmpty());
    }

    @Test
    public void trackerShouldBeFoundByPhoneNumber() {
        final String givenPhoneNumber = "447336934";

        final Optional<Tracker> optionalActual = service.findByPhoneNumber(givenPhoneNumber);
        assertTrue(optionalActual.isPresent());

        final Tracker actual = optionalActual.get();
        final Tracker expected = Tracker.builder()
                .id(255L)
                .imei("11112222333344445555")
                .password("$2a$10$8y9hC00YePN.9uH.OLCQ6OWeaR8G9q/U9MEvizLx9zaBkwe0KItHG")
                .phoneNumber(givenPhoneNumber)
                .build();
        assertEquals(expected, actual);
    }

    @Test
    public void trackerShouldNotBeFoundByPhoneNumber() {
        final String givenPhoneNumber = "000000000";

        final Optional<Tracker> optionalActual = service.findByPhoneNumber(givenPhoneNumber);
        assertTrue(optionalActual.isEmpty());
    }

    @Test
    public void trackerShouldBeFoundByIdFetchingMileage() {
        final Long givenId = 255L;

        final Optional<Tracker> optionalActual = service.findByIdFetchingMileage(givenId);
        assertTrue(optionalActual.isPresent());

        final Tracker actual = optionalActual.get();
        final Tracker expected = Tracker.builder()
                .id(255L)
                .imei("11112222333344445555")
                .password("$2a$10$8y9hC00YePN.9uH.OLCQ6OWeaR8G9q/U9MEvizLx9zaBkwe0KItHG")
                .phoneNumber("447336934")
                .mileage(new TrackerMileage(1L, 0, 0))
                .build();
        assertEquals(expected, actual);
    }

    @Test
    public void trackerShouldNotBeFoundByIdFetchingMileage() {
        final Long givenId = MAX_VALUE;

        final Optional<Tracker> optionalActual = service.findByIdFetchingMileage(givenId);
        assertTrue(optionalActual.isEmpty());
    }

    private static User createUser(final Long id) {
        return User.builder()
                .id(id)
                .build();
    }
}
