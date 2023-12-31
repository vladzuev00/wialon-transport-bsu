package by.bsu.wialontransport.crud.repository;

import by.bsu.wialontransport.base.AbstractContextTest;
import by.bsu.wialontransport.crud.entity.TrackerEntity;
import by.bsu.wialontransport.crud.entity.TrackerMileageEntity;
import by.bsu.wialontransport.crud.entity.UserEntity;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Optional;

import static by.bsu.wialontransport.util.entity.EntityUtil.mapToIdsList;
import static by.bsu.wialontransport.util.entity.TrackerEntityUtil.*;
import static java.lang.Long.MAX_VALUE;
import static org.junit.Assert.*;
import static org.springframework.data.domain.Pageable.ofSize;

public final class TrackerRepositoryTest extends AbstractContextTest {

    @Autowired
    private TrackerRepository repository;

    @Test
    public void trackerShouldBeFoundById() {
        startQueryCount();
        final TrackerEntity actual = repository.findById(255L).orElseThrow();
        checkQueryCount(1);

        assertFalse(isUserFetched(actual));
        assertFalse(isMileageFetched(actual));

        final TrackerEntity expected = TrackerEntity.builder()
                .id(255L)
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
        startQueryCount();
        final TrackerEntity actual = repository.findByImei("11112222333344445555").orElseThrow();
        checkQueryCount(1);

        assertFalse(isUserFetched(actual));
        assertFalse(isMileageFetched(actual));

        final TrackerEntity expected = TrackerEntity.builder()
                .id(255L)
                .imei("11112222333344445555")
                .password("$2a$10$8y9hC00YePN.9uH.OLCQ6OWeaR8G9q/U9MEvizLx9zaBkwe0KItHG")
                .phoneNumber("447336934")
                .user(entityManager.getReference(UserEntity.class, 255L))
                .mileage(entityManager.getReference(TrackerMileageEntity.class, 1L))
                .build();
        checkEquals(expected, actual);
    }

    @Test
    public void trackerShouldNotBeFoundByImei() {
        startQueryCount();
        final Optional<TrackerEntity> optionalFoundTracker = repository.findByImei("00000000000000000000");
        checkQueryCount(1);

        assertTrue(optionalFoundTracker.isEmpty());
    }

    @Test
    public void trackersOrderedByImeiShouldBeFoundByUserId() {
        startQueryCount();
        final Page<TrackerEntity> actual = repository.findByUserIdOrderedByImei(255L, ofSize(5));
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
        startQueryCount();
        final Page<TrackerEntity> actual = repository.findByUserIdOrderedByImei(256L, ofSize(5));
        checkQueryCount(1);

        assertTrue(actual.isEmpty());
    }

    @Test
    public void trackerShouldBeFoundByIdFetchingUser() {
        startQueryCount();
        final Optional<TrackerEntity> optionalActual = repository.findByIdFetchingUser(255L);
        checkQueryCount(1);

        assertTrue(optionalActual.isPresent());
        final TrackerEntity actual = optionalActual.get();

        assertTrue(isUserFetched(actual));
        assertFalse(isMileageFetched(actual));

        final TrackerEntity expected = TrackerEntity.builder()
                .id(255L)
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
        startQueryCount();
        final Optional<TrackerEntity> optionalActual = repository.findByIdFetchingUser(257L);
        checkQueryCount(1);

        assertTrue(optionalActual.isEmpty());
    }

    @Test
    public void trackerShouldBeFoundByPhoneNumber() {
        startQueryCount();
        final Optional<TrackerEntity> optionalActual = repository.findByPhoneNumber("447336934");
        checkQueryCount(1);

        assertTrue(optionalActual.isPresent());
        final TrackerEntity actual = optionalActual.get();

        assertFalse(isUserFetched(actual));
        assertFalse(isMileageFetched(actual));

        final TrackerEntity expected = TrackerEntity.builder()
                .id(255L)
                .imei("11112222333344445555")
                .password("$2a$10$8y9hC00YePN.9uH.OLCQ6OWeaR8G9q/U9MEvizLx9zaBkwe0KItHG")
                .phoneNumber("447336934")
                .user(entityManager.getReference(UserEntity.class, 255L))
                .mileage(entityManager.getReference(TrackerMileageEntity.class, 1L))
                .build();
        checkEquals(expected, actual);
    }

    @Test
    public void trackerShouldNotBeFoundByPhoneNumber() {
        startQueryCount();
        final Optional<TrackerEntity> optionalActual = repository.findByPhoneNumber("447336936");
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
}
