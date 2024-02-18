package by.bsu.wialontransport.crud.repository;

import by.bsu.wialontransport.base.AbstractSpringBootTest;
import by.bsu.wialontransport.crud.entity.TrackerEntity;
import by.bsu.wialontransport.crud.entity.TrackerMileageEntity;
import by.bsu.wialontransport.crud.entity.UserEntity;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static by.bsu.wialontransport.util.entity.EntityUtil.mapToIdsList;
import static by.bsu.wialontransport.util.entity.TrackerEntityUtil.*;
import static java.lang.Long.MAX_VALUE;
import static org.junit.Assert.*;
import static org.springframework.data.domain.Pageable.ofSize;

public final class TrackerRepositoryTest extends AbstractSpringBootTest {

    @Autowired
    private TrackerRepository repository;

    @Test
    public void trackerShouldBeFoundById() {
        final Long givenId = 255L;

        startQueryCount();
        final Optional<TrackerEntity> optionalActual = repository.findById(givenId);
        checkQueryCount(1);

        assertTrue(optionalActual.isPresent());
        final TrackerEntity actual = optionalActual.get();
        assertFalse(isUserFetched(actual));
        assertFalse(isMileageFetched(actual));

        final TrackerEntity expected = TrackerEntity.builder()
                .id(givenId)
                .imei("11112222333344445555")
                .password("$2a$10$8y9hC00YePN.9uH.OLCQ6OWeaR8G9q/U9MEvizLx9zaBkwe0KItHG")
                .phoneNumber("447336934")
                .user(entityManager.getReference(UserEntity.class, 255L))
                .mileage(entityManager.getReference(TrackerMileageEntity.class, 1L))
                .build();
        checkEquals(expected, actual);
    }

    @Test
    public void trackerShouldBeInserted() {
        final TrackerEntity givenTracker = TrackerEntity.builder()
                .imei("11112222333344445557")
                .password("password")
                .phoneNumber("447336936")
                .user(entityManager.getReference(UserEntity.class, 255L))
                .mileage(entityManager.getReference(TrackerMileageEntity.class, 1L))
                .build();

        startQueryCount();
        repository.save(givenTracker);
        checkQueryCount(1);
    }

    @Test
    public void trackerShouldBeFoundByImei() {
        final String givenImei = "11112222333344445555";

        startQueryCount();
        final Optional<TrackerEntity> optionalActual = repository.findByImei(givenImei);
        checkQueryCount(1);

        assertTrue(optionalActual.isPresent());
        final TrackerEntity actual = optionalActual.get();
        assertFalse(isUserFetched(actual));
        assertFalse(isMileageFetched(actual));

        final TrackerEntity expected = TrackerEntity.builder()
                .id(255L)
                .imei(givenImei)
                .password("$2a$10$8y9hC00YePN.9uH.OLCQ6OWeaR8G9q/U9MEvizLx9zaBkwe0KItHG")
                .phoneNumber("447336934")
                .user(entityManager.getReference(UserEntity.class, 255L))
                .mileage(entityManager.getReference(TrackerMileageEntity.class, 1L))
                .build();
        checkEquals(expected, actual);
    }

    @Test
    public void trackerShouldNotBeFoundByImei() {
        final String givenImei = "00000000000000000000";

        startQueryCount();
        final Optional<TrackerEntity> optionalFoundTracker = repository.findByImei(givenImei);
        checkQueryCount(1);

        assertTrue(optionalFoundTracker.isEmpty());
    }

    @Test
    public void trackersOrderedByImeiShouldBeFoundByUserId() {
        final Long givenUserId = 255L;
        final Pageable givenPageable = ofSize(5);

        startQueryCount();
        final Page<TrackerEntity> actual = repository.findByUserIdOrderedByImei(givenUserId, givenPageable);
        checkQueryCount(1);

        final List<TrackerEntity> actualAsSet = actual.toList();

        assertTrue(areUsersNotFetched(actualAsSet));
        assertTrue(areMileagesNotFetched(actualAsSet));

        final List<Long> actualIds = mapToIdsList(actualAsSet);
        final List<Long> expectedIds = List.of(255L, 256L);
        assertEquals(expectedIds, actualIds);
    }

    @Test
    public void trackersOrderedByImeiShouldNotBeFoundByUserId() {
        final Long givenUserId = 256L;
        final Pageable givenPageable = ofSize(5);

        startQueryCount();
        final Page<TrackerEntity> actual = repository.findByUserIdOrderedByImei(givenUserId, givenPageable);
        checkQueryCount(1);

        assertTrue(actual.isEmpty());
    }

    @Test
    public void trackerShouldBeFoundByIdFetchingUser() {
        final Long givenId = 255L;

        startQueryCount();
        final Optional<TrackerEntity> optionalActual = repository.findByIdFetchingUser(givenId);
        checkQueryCount(1);

        assertTrue(optionalActual.isPresent());
        final TrackerEntity actual = optionalActual.get();

        assertTrue(isUserFetched(actual));
        assertFalse(isMileageFetched(actual));

        final TrackerEntity expected = TrackerEntity.builder()
                .id(givenId)
                .imei("11112222333344445555")
                .password("$2a$10$8y9hC00YePN.9uH.OLCQ6OWeaR8G9q/U9MEvizLx9zaBkwe0KItHG")
                .phoneNumber("447336934")
                .user(entityManager.getReference(UserEntity.class, 255L))
                .mileage(entityManager.getReference(TrackerMileageEntity.class, 1L))
                .build();
        checkEquals(expected, actual);
    }

    @Test
    public void trackerShouldNotBeFoundByIdFetchingUser() {
        final Long givenId = MAX_VALUE;

        startQueryCount();
        final Optional<TrackerEntity> optionalActual = repository.findByIdFetchingUser(givenId);
        checkQueryCount(1);

        assertTrue(optionalActual.isEmpty());
    }

    @Test
    public void trackerShouldBeFoundByPhoneNumber() {
        final String givenPhoneNumber = "447336934";

        startQueryCount();
        final Optional<TrackerEntity> optionalActual = repository.findByPhoneNumber(givenPhoneNumber);
        checkQueryCount(1);

        assertTrue(optionalActual.isPresent());
        final TrackerEntity actual = optionalActual.get();

        assertFalse(isUserFetched(actual));
        assertFalse(isMileageFetched(actual));

        final TrackerEntity expected = TrackerEntity.builder()
                .id(255L)
                .imei("11112222333344445555")
                .password("$2a$10$8y9hC00YePN.9uH.OLCQ6OWeaR8G9q/U9MEvizLx9zaBkwe0KItHG")
                .phoneNumber(givenPhoneNumber)
                .user(entityManager.getReference(UserEntity.class, 255L))
                .mileage(entityManager.getReference(TrackerMileageEntity.class, 1L))
                .build();
        checkEquals(expected, actual);
    }

    @Test
    public void trackerShouldNotBeFoundByPhoneNumber() {
        final String givenPhoneNumber = "000000000";

        startQueryCount();
        final Optional<TrackerEntity> optionalActual = repository.findByPhoneNumber(givenPhoneNumber);
        checkQueryCount(1);

        assertTrue(optionalActual.isEmpty());
    }

    @Test
    public void trackerPasswordShouldBeUpdated() {
        final Long givenId = 255L;
        final String givenNewPassword = "new-password";

        startQueryCount();
        final int actualCountUpdatedRows = repository.updatePassword(givenId, givenNewPassword);
        checkQueryCount(1);

        final int expectedCountUpdatedRows = 1;
        assertEquals(expectedCountUpdatedRows, actualCountUpdatedRows);

        final TrackerEntity actual = repository.findById(givenId).orElseThrow();
        final TrackerEntity expected = TrackerEntity.builder()
                .id(givenId)
                .imei("11112222333344445555")
                .password(givenNewPassword)
                .phoneNumber("447336934")
                .user(entityManager.getReference(UserEntity.class, 255L))
                .mileage(entityManager.getReference(TrackerMileageEntity.class, 1L))
                .build();
        checkEquals(expected, actual);
    }

    @Test
    public void trackerPasswordShouldNotBeUpdatedBecauseOfNotExistingId() {
        final Long givenId = MAX_VALUE;
        final String givenNewPassword = "new-password";

        startQueryCount();
        final int actualCountUpdatedRows = repository.updatePassword(givenId, givenNewPassword);
        checkQueryCount(1);

        final int expectedCountUpdatedRows = 0;
        assertEquals(expectedCountUpdatedRows, actualCountUpdatedRows);
    }

    @Test
    public void trackerShouldBeFoundByByIdFetchingMileage() {
        final Long givenId = 255L;

        startQueryCount();
        final Optional<TrackerEntity> optionalActual = repository.findByIdFetchingMileage(givenId);
        checkQueryCount(1);

        assertTrue(optionalActual.isPresent());
        final TrackerEntity actual = optionalActual.get();
        assertFalse(isUserFetched(actual));
        assertTrue(isMileageFetched(actual));

        final TrackerEntity expected = TrackerEntity.builder()
                .id(givenId)
                .imei("11112222333344445555")
                .password("$2a$10$8y9hC00YePN.9uH.OLCQ6OWeaR8G9q/U9MEvizLx9zaBkwe0KItHG")
                .phoneNumber("447336934")
                .user(entityManager.getReference(UserEntity.class, 255L))
                .mileage(entityManager.getReference(TrackerMileageEntity.class, 1L))
                .build();
        checkEquals(expected, actual);
    }

    @Test
    public void trackerShouldNotBeFoundByIdFetchingMileage() {
        final Long givenId = MAX_VALUE;

        startQueryCount();
        final Optional<TrackerEntity> optionalActual = repository.findByIdFetchingUser(givenId);
        checkQueryCount(1);

        assertTrue(optionalActual.isEmpty());
    }
}
