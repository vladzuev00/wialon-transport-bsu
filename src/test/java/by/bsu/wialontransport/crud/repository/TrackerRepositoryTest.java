package by.bsu.wialontransport.crud.repository;

import by.bsu.wialontransport.base.AbstractContextTest;
import by.bsu.wialontransport.crud.entity.TrackerEntity;
import by.bsu.wialontransport.crud.entity.TrackerMileageEntity;
import by.bsu.wialontransport.crud.entity.UserEntity;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;
import java.util.Set;
import java.util.stream.Stream;

import static by.bsu.wialontransport.util.StreamUtil.isEmpty;
import static by.bsu.wialontransport.util.entity.EntityUtil.mapToIds;
import static by.bsu.wialontransport.util.entity.TrackerEntityUtil.*;
import static java.lang.Long.MAX_VALUE;
import static java.util.stream.Collectors.toSet;
import static org.junit.Assert.*;
import static org.springframework.data.domain.Pageable.ofSize;

public final class TrackerRepositoryTest extends AbstractContextTest {

    @Autowired
    private TrackerRepository repository;

    @Test
    public void trackerShouldBeFoundById() {
        super.startQueryCount();
        final TrackerEntity actual = this.repository.findById(255L).orElseThrow();
        super.checkQueryCount(1);

        assertFalse(isUserLoaded(actual));
        assertFalse(isMileageLoaded(actual));

        final TrackerEntity expected = TrackerEntity.builder()
                .id(255L)
                .imei("11112222333344445555")
                .password("$2a$10$8y9hC00YePN.9uH.OLCQ6OWeaR8G9q/U9MEvizLx9zaBkwe0KItHG")
                .phoneNumber("447336934")
                .user(super.entityManager.getReference(UserEntity.class, 255L))
                .mileage(super.entityManager.getReference(TrackerMileageEntity.class, 1L))
                .build();
        checkEquals(expected, actual);
    }

    @Test
    public void trackerShouldBeInserted() {
        final TrackerEntity givenTracker = TrackerEntity.builder()
                .imei("11112222333344445557")
                .password("password")
                .phoneNumber("447336936")
                .user(super.entityManager.getReference(UserEntity.class, 255L))
                .mileage(super.entityManager.getReference(TrackerMileageEntity.class, 1L))
                .build();

        super.startQueryCount();
        this.repository.save(givenTracker);
        super.checkQueryCount(1);
    }

    @Test
    public void trackerShouldBeFoundByImei() {
        super.startQueryCount();
        final TrackerEntity actual = this.repository.findByImei("11112222333344445555").orElseThrow();
        super.checkQueryCount(1);

        assertFalse(isUserLoaded(actual));
        assertFalse(isMileageLoaded(actual));

        final TrackerEntity expected = TrackerEntity.builder()
                .id(255L)
                .imei("11112222333344445555")
                .password("$2a$10$8y9hC00YePN.9uH.OLCQ6OWeaR8G9q/U9MEvizLx9zaBkwe0KItHG")
                .phoneNumber("447336934")
                .user(super.entityManager.getReference(UserEntity.class, 255L))
                .mileage(super.entityManager.getReference(TrackerMileageEntity.class, 1L))
                .build();
        checkEquals(expected, actual);
    }

    @Test
    public void trackerShouldNotBeFoundByImei() {
        super.startQueryCount();
        final Optional<TrackerEntity> optionalFoundTracker = this.repository.findByImei("00000000000000000000");
        super.checkQueryCount(1);

        assertTrue(optionalFoundTracker.isEmpty());
    }

    @Test
    public void trackersShouldBeFoundByUserId() {
        super.startQueryCount();
        final Stream<TrackerEntity> actual = this.repository.findByUserId(255L, ofSize(5));
        super.checkQueryCount(1);

        final Set<TrackerEntity> actualAsSet = actual.collect(toSet());

        assertTrue(areUsersNotLoaded(actualAsSet));
        assertTrue(areMileagesNotLoaded(actualAsSet));

        final Set<Long> actualIds = mapToIds(actualAsSet);
        final Set<Long> expectedIds = Set.of(255L, 256L);
        assertEquals(expectedIds, actualIds);
    }

    @Test
    public void trackersShouldNotBeFoundByUserId() {
        super.startQueryCount();
        final Stream<TrackerEntity> actual = this.repository.findByUserId(256L, ofSize(5));
        super.checkQueryCount(1);

        assertTrue(isEmpty(actual));
    }

    @Test
    public void trackerShouldBeFoundByIdWithLoadedUser() {
        super.startQueryCount();
        final Optional<TrackerEntity> optionalActual = this.repository.findByIdWithUser(255L);
        super.checkQueryCount(1);

        assertTrue(optionalActual.isPresent());
        final TrackerEntity actual = optionalActual.get();

        assertTrue(isUserLoaded(actual));
        assertFalse(isMileageLoaded(actual));

        final TrackerEntity expected = TrackerEntity.builder()
                .id(255L)
                .imei("11112222333344445555")
                .password("$2a$10$8y9hC00YePN.9uH.OLCQ6OWeaR8G9q/U9MEvizLx9zaBkwe0KItHG")
                .phoneNumber("447336934")
                .user(super.entityManager.getReference(UserEntity.class, 255L))
                .mileage(super.entityManager.getReference(TrackerMileageEntity.class, 1L))
                .build();
        checkEquals(expected, actual);
    }

    @Test
    public void trackerShouldNotBeFoundByIdWithLoadedUser() {
        super.startQueryCount();
        final Optional<TrackerEntity> optionalActual = this.repository.findByIdWithUser(257L);
        super.checkQueryCount(1);

        assertTrue(optionalActual.isEmpty());
    }

    @Test
    public void trackerShouldBeFoundByPhoneNumber() {
        super.startQueryCount();
        final Optional<TrackerEntity> optionalActual = this.repository.findByPhoneNumber("447336934");
        super.checkQueryCount(1);

        assertTrue(optionalActual.isPresent());
        final TrackerEntity actual = optionalActual.get();

        assertFalse(isUserLoaded(actual));
        assertFalse(isMileageLoaded(actual));

        final TrackerEntity expected = TrackerEntity.builder()
                .id(255L)
                .imei("11112222333344445555")
                .password("$2a$10$8y9hC00YePN.9uH.OLCQ6OWeaR8G9q/U9MEvizLx9zaBkwe0KItHG")
                .phoneNumber("447336934")
                .user(super.entityManager.getReference(UserEntity.class, 255L))
                .mileage(super.entityManager.getReference(TrackerMileageEntity.class, 1L))
                .build();
        checkEquals(expected, actual);
    }

    @Test
    public void trackerShouldNotBeFoundByPhoneNumber() {
        super.startQueryCount();
        final Optional<TrackerEntity> optionalActual = this.repository.findByPhoneNumber("447336936");
        super.checkQueryCount(1);

        assertTrue(optionalActual.isEmpty());
    }

    @Test
    public void trackerPasswordShouldBeUpdated() {
        final Long givenId = 255L;
        final String givenNewPassword = "new-password";

        super.startQueryCount();
        final int actualCountUpdatedRows = this.repository.updatePassword(givenId, givenNewPassword);
        super.checkQueryCount(1);

        final int expectedCountUpdatedRows = 1;
        assertEquals(expectedCountUpdatedRows, actualCountUpdatedRows);

        final TrackerEntity actual = this.repository.findById(givenId).orElseThrow();
        final TrackerEntity expected = TrackerEntity.builder()
                .id(givenId)
                .imei("11112222333344445555")
                .password(givenNewPassword)
                .phoneNumber("447336934")
                .user(super.entityManager.getReference(UserEntity.class, 255L))
                .mileage(super.entityManager.getReference(TrackerMileageEntity.class, 1L))
                .build();
        checkEquals(expected, actual);
    }

    @Test
    public void trackerPasswordShouldNotBeUpdatedBecauseOfNotExistingId() {
        final Long givenId = MAX_VALUE;
        final String givenNewPassword = "new-password";

        super.startQueryCount();
        final int actualCountUpdatedRows = this.repository.updatePassword(givenId, givenNewPassword);
        super.checkQueryCount(1);

        final int expectedCountUpdatedRows = 0;
        assertEquals(expectedCountUpdatedRows, actualCountUpdatedRows);
    }
}
