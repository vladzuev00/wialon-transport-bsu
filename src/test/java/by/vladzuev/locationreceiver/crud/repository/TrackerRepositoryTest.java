package by.vladzuev.locationreceiver.crud.repository;

import by.vladzuev.locationreceiver.base.AbstractSpringBootTest;
import by.vladzuev.locationreceiver.crud.entity.MileageEntity;
import by.vladzuev.locationreceiver.crud.entity.TrackerEntity;
import by.vladzuev.locationreceiver.crud.entity.UserEntity;
import by.vladzuev.locationreceiver.util.entity.TrackerEntityUtil;
import org.hibernate.Hibernate;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static java.lang.Long.MAX_VALUE;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.data.domain.PageRequest.ofSize;

public final class TrackerRepositoryTest extends AbstractSpringBootTest {

    @Autowired
    private TrackerRepository repository;

    @Test
    public void trackerShouldBeFoundById() {
        final Long givenId = 255L;

        final Optional<TrackerEntity> optionalActual = repository.findById(givenId);
        assertTrue(optionalActual.isPresent());
        final TrackerEntity actual = optionalActual.get();
        assertFetchedLazily(actual);
        final TrackerEntity expected = new TrackerEntity(
                givenId,
                "11112222333344445555",
                "$2a$10$8y9hC00YePN.9uH.OLCQ6OWeaR8G9q/U9MEvizLx9zaBkwe0KItHG",
                "447336934",
                UserEntity.builder().id(255L).build(),
                MileageEntity.builder().id(1L).build()
        );
        TrackerEntityUtil.assertEquals(expected, actual);
    }

    @Test
    public void trackerShouldBeSaved() {
        final TrackerEntity givenTracker = TrackerEntity.builder()
                .imei("11112222333344445557")
                .password("password")
                .phoneNumber("447336939")
                .user(UserEntity.builder().id(255L).build())
                .mileage(MileageEntity.builder().id(1L).build())
                .build();

        final TrackerEntity actual = repository.save(givenTracker);
        final TrackerEntity expected = new TrackerEntity(
                1L,
                "11112222333344445557",
                "password",
                "447336939",
                UserEntity.builder().id(255L).build(),
                MileageEntity.builder().id(1L).build()
        );
        TrackerEntityUtil.assertEquals(expected, actual);
    }

    @Test
    public void trackerShouldBeFoundByImei() {
        final String givenImei = "11112222333344445555";

        final Optional<TrackerEntity> optionalActual = repository.findByImei(givenImei);
        assertTrue(optionalActual.isPresent());
        final TrackerEntity actual = optionalActual.get();
        assertFetchedLazily(actual);
        final TrackerEntity expected = TrackerEntity.builder().id(255L).build();
        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void trackerShouldNotBeFoundByImei() {
        final String givenImei = "00000000000000000000";

        final Optional<TrackerEntity> optionalActual = repository.findByImei(givenImei);
        assertTrue(optionalActual.isEmpty());
    }

    @Test
    public void trackersShouldBeFoundByUserIdOrderingByImei() {
        final Long givenUserId = 255L;
        final Pageable givenPageable = ofSize(5);

        final Page<TrackerEntity> actual = repository.findByUserIdOrderingByImei(givenUserId, givenPageable);
        actual.forEach(this::assertFetchedLazily);
        final Page<TrackerEntity> expected = new PageImpl<>(
                List.of(
                        TrackerEntity.builder().id(257L).build(),
                        TrackerEntity.builder().id(255L).build(),
                        TrackerEntity.builder().id(256L).build()
                ),
                givenPageable,
                3
        );
        assertEquals(expected, actual);
    }

    @Test
    public void trackersShouldNotBeFoundByUserIdOrderingByImei() {
        final Long givenUserId = 256L;
        final Pageable givenPageable = ofSize(5);

        final Page<TrackerEntity> actual = repository.findByUserIdOrderingByImei(givenUserId, givenPageable);
        assertTrue(actual.isEmpty());
    }

    @Test
    public void trackerShouldBeFoundByIdFetchingUser() {
        final Long givenId = 255L;

        final Optional<TrackerEntity> optionalActual = repository.findByIdFetchingUser(givenId);
        assertTrue(optionalActual.isPresent());
        final TrackerEntity actual = optionalActual.get();
        assertOnlyUserFetched(actual);
        final TrackerEntity expected = TrackerEntity.builder().id(givenId).build();
        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void trackerShouldNotBeFoundByIdFetchingUser() {
        final Long givenId = MAX_VALUE;

        final Optional<TrackerEntity> optionalActual = repository.findByIdFetchingUser(givenId);
        assertTrue(optionalActual.isEmpty());
    }

    @Test
    public void trackerShouldBeFoundByPhoneNumber() {
        final String givenPhoneNumber = "447336934";

        final Optional<TrackerEntity> optionalActual = repository.findByPhoneNumber(givenPhoneNumber);
        assertTrue(optionalActual.isPresent());
        final TrackerEntity actual = optionalActual.get();
        assertFetchedLazily(actual);
        final TrackerEntity expected = TrackerEntity.builder().id(255L).build();
        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void trackerShouldNotBeFoundByPhoneNumber() {
        final String givenPhoneNumber = "000000000";

        final Optional<TrackerEntity> optionalActual = repository.findByPhoneNumber(givenPhoneNumber);
        assertTrue(optionalActual.isEmpty());
    }

    @Test
    public void trackerPasswordShouldBeUpdated() {
        final Long givenId = 255L;
        final String givenNewEncryptedPassword = "new-password";

        final int actual = repository.updatePassword(givenId, givenNewEncryptedPassword);
        final int expected = 1;
        assertEquals(expected, actual);
    }

    @Test
    public void trackerPasswordShouldNotBeUpdatedBecauseOfNoSuchTracker() {
        final Long givenId = MAX_VALUE;
        final String givenNewEncryptedPassword = "new-password";

        final int actual = repository.updatePassword(givenId, givenNewEncryptedPassword);
        final int expected = 0;
        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void trackerShouldBeFoundByByIdFetchingMileage() {
        final Long givenId = 255L;

        final Optional<TrackerEntity> optionalActual = repository.findByIdFetchingMileage(givenId);
        assertTrue(optionalActual.isPresent());
        final TrackerEntity actual = optionalActual.get();
        assertOnlyMileageFetched(actual);
        final TrackerEntity expected = TrackerEntity.builder().id(givenId).build();
        Assertions.assertEquals(expected, actual);
    }

    @Test
    public void trackerShouldNotBeFoundByIdFetchingMileage() {
        final Long givenId = MAX_VALUE;

        final Optional<TrackerEntity> optionalActual = repository.findByIdFetchingUser(givenId);
        assertTrue(optionalActual.isEmpty());
    }

    @Test
    public void trackerShouldBeExistByImei() {
        final String givenImei = "11112222333344445555";

        assertTrue(repository.existsByImei(givenImei));
    }

    @Test
    public void trackerShouldNotBeExistByImei() {
        final String givenImei = "00000000000000000000";

        assertFalse(repository.existsByImei(givenImei));
    }

    @Test
    public void trackerShouldBeExistByPhoneNumber() {
        final String givenPhoneNumber = "447336934";

        assertTrue(repository.existsByPhoneNumber(givenPhoneNumber));
    }

    @Test
    public void trackerShouldNotBeExistByPhoneNumber() {
        final String givenPhoneNumber = "000000000";

        assertFalse(repository.existsByPhoneNumber(givenPhoneNumber));
    }

    private void assertFetchedLazily(final TrackerEntity entity) {
        assertFalse(Hibernate.isInitialized(entity.getUser()));
        assertFalse(Hibernate.isInitialized(entity.getMileage()));
    }

    private void assertOnlyUserFetched(final TrackerEntity entity) {
        assertTrue(Hibernate.isInitialized(entity.getUser()));
        assertFalse(Hibernate.isInitialized(entity.getMileage()));
    }

    private void assertOnlyMileageFetched(final TrackerEntity entity) {
        assertFalse(Hibernate.isInitialized(entity.getUser()));
        assertTrue(Hibernate.isInitialized(entity.getMileage()));
    }
}
