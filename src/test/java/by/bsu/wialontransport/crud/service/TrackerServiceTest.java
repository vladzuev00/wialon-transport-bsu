package by.bsu.wialontransport.crud.service;

import by.bsu.wialontransport.base.AbstractContextTest;
import by.bsu.wialontransport.crud.dto.Tracker;
import by.bsu.wialontransport.crud.dto.User;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.Optional;
import java.util.Set;

import static by.bsu.wialontransport.crud.entity.UserEntity.Role.USER;
import static java.lang.Long.MAX_VALUE;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public final class TrackerServiceTest extends AbstractContextTest {

    @Autowired
    private TrackerService service;

    @Test
    public void trackerShouldBeFoundByImei() {
        final Tracker actual = service.findByImei("11112222333344445555").orElseThrow();
        final Tracker expected = Tracker.builder()
                .id(255L)
                .imei("11112222333344445555")
                .password("$2a$10$8y9hC00YePN.9uH.OLCQ6OWeaR8G9q/U9MEvizLx9zaBkwe0KItHG")
                .phoneNumber("447336934")
                .build();
        assertEquals(expected, actual);
    }

    @Test
    public void trackerShouldNotBeFoundByImei() {
        final Optional<Tracker> optionalActual = service.findByImei("00000000000000000000");
        assertTrue(optionalActual.isEmpty());
    }

    @Test
    public void allTrackersShouldBeFoundByUser() {
        final User givenUser = createUser(255L);

        final Page<Tracker> actual = service.findByUser(givenUser);
        final Set<Tracker> actualAsSet = actual.toSet();
        final Set<Tracker> expectedAsSet = Set.of(
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
        assertEquals(expectedAsSet, actualAsSet);
    }

    @Test
    public void allTrackersShouldNotBeFoundByUser() {
        final User givenUser = createUser(256L);

        final Page<Tracker> actual = service.findByUser(givenUser);
        assertTrue(actual.isEmpty());
    }

    @Test
    public void trackersShouldBeFoundByUser() {
        final User givenUser = createUser(255L);
        final PageRequest givenPageRequest = PageRequest.of(0, 5);

        final Page<Tracker> actual = service.findByUser(givenUser, givenPageRequest);
        final Set<Tracker> actualAsSet = actual.toSet();
        final Set<Tracker> expectedAsSet = Set.of(
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
        assertEquals(expectedAsSet, actualAsSet);
    }

    @Test
    public void trackersShouldNotBeFoundByUser() {
        final User givenUser = createUser(256L);
        final PageRequest givenPageRequest = PageRequest.of(0, 5);

        final Page<Tracker> actual = service.findByUser(givenUser, givenPageRequest);
        assertTrue(actual.isEmpty());
    }

    @Test
    public void trackerShouldBeFoundByIdWithUser() {
        final Optional<Tracker> optionalActual = service.findByIdWithUser(255L);
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
    public void trackerShouldNotBeFoundByIdWithUser() {
        final Optional<Tracker> optionalActual = service.findByIdWithUser(MAX_VALUE);
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
