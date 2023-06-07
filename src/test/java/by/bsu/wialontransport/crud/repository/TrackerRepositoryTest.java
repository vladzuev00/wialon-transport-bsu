package by.bsu.wialontransport.crud.repository;

import by.bsu.wialontransport.base.AbstractContextTest;
import by.bsu.wialontransport.crud.entity.TrackerEntity;
import by.bsu.wialontransport.crud.entity.UserEntity;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.jdbc.Sql;

import java.util.List;
import java.util.Optional;

import static org.hibernate.Hibernate.isInitialized;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.springframework.data.domain.Pageable.ofSize;

public final class TrackerRepositoryTest extends AbstractContextTest {

    @Autowired
    private TrackerRepository repository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Test
    public void trackerShouldBeFoundById() {
        super.startQueryCount();
        final TrackerEntity actual = this.repository.findById(255L).orElseThrow();
        super.checkQueryCount(1);

        final TrackerEntity expected = TrackerEntity.builder()
                .id(255L)
                .imei("11112222333344445555")
                .password("password")
                .phoneNumber("447336934")
                .user(super.entityManager.getReference(UserEntity.class, 255L))
                .build();
        checkEquals(expected, actual);
    }

    @Test
    public void trackerShouldBeInserted() {
        final TrackerEntity givenTracker = TrackerEntity.builder()
                .id(255L)
                .imei("11112222333344445555")
                .password("password")
                .phoneNumber("447336934")
                .user(super.entityManager.getReference(UserEntity.class, 255L))
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

        final TrackerEntity expected = TrackerEntity.builder()
                .id(255L)
                .imei("11112222333344445555")
                .password("password")
                .phoneNumber("447336934")
                .user(super.entityManager.getReference(UserEntity.class, 255L))
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
    @Sql(statements = "INSERT INTO trackers(id, imei, encrypted_password, phone_number, user_id) "
            + "VALUES(355, '11111222223333344444', 'password', '447336935', 255)")
    @Sql(statements = "INSERT INTO trackers(id, imei, encrypted_password, phone_number, user_id) "
            + "VALUES(356, '11111222223333344445', 'password', '447336936', 255)")
    public void trackersShouldBeFoundByUserId() {
        super.startQueryCount();
        final List<TrackerEntity> actual = this.repository.findByUserId(255L, ofSize(5));
        super.checkQueryCount(1);

        final List<Long> actualIds = actual.stream()
                .map(TrackerEntity::getId)
                .toList();
        final List<Long> expectedIds = List.of(255L, 355L, 356L);
        assertEquals(expectedIds, actualIds);
    }

    @Test
    public void trackersShouldNotBeFoundByUserId() {
        super.startQueryCount();
        final List<TrackerEntity> actual = this.repository.findByUserId(256L, ofSize(5));
        super.checkQueryCount(1);

        assertTrue(actual.isEmpty());
    }

    @Test
    public void trackerShouldBeFoundByIdWithLoadedUser() {
        super.startQueryCount();
        final Optional<TrackerEntity> optionalActual = this.repository.findByIdWithUser(255L);
        super.checkQueryCount(1);

        assertTrue(optionalActual.isPresent());
        final TrackerEntity actual = optionalActual.get();
        assertTrue(isInitialized(actual.getUser()));

        final TrackerEntity expected = TrackerEntity.builder()
                .id(255L)
                .imei("11112222333344445555")
                .password("password")
                .phoneNumber("447336934")
                .user(super.entityManager.getReference(UserEntity.class, 255L))
                .build();
        checkEquals(expected, actual);
    }

    @Test
    public void trackerShouldNotBeFoundByIdWithLoadedUser() {
        super.startQueryCount();
        final Optional<TrackerEntity> optionalActual = this.repository.findByIdWithUser(256L);
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
        final TrackerEntity expected = TrackerEntity.builder()
                .id(255L)
                .imei("11112222333344445555")
                .password("password")
                .phoneNumber("447336934")
                .user(super.entityManager.getReference(UserEntity.class, 255L))
                .build();
        checkEquals(expected, actual);
    }

    @Test
    public void trackerShouldNotBeFoundByPhoneNumber() {
        super.startQueryCount();
        final Optional<TrackerEntity> optionalActual = this.repository.findByPhoneNumber("447336935");
        super.checkQueryCount(1);

        assertTrue(optionalActual.isEmpty());
    }

    @Test
    public void trackerPasswordShouldBeUpdated() {
        final Long givenId = 255L;
        final String givenNewPassword = "new-password";

        super.startQueryCount();
        this.repository.updatePassword(givenId, givenNewPassword);
        super.checkQueryCount(1);

        final TrackerEntity actual = this.repository.findById(givenId).orElseThrow();
        final TrackerEntity expected = TrackerEntity.builder()
                .id(givenId)
                .imei("11112222333344445555")
                .password(givenNewPassword)
                .phoneNumber("447336934")
                .user(super.entityManager.getReference(UserEntity.class, 255L))
                .build();
        checkEquals(expected, actual);
    }

    private static void checkEquals(final TrackerEntity expected, final TrackerEntity actual) {
        assertEquals(expected.getId(), actual.getId());
        assertEquals(expected.getImei(), actual.getImei());
        assertEquals(expected.getPassword(), actual.getPassword());
        assertEquals(expected.getPhoneNumber(), actual.getPhoneNumber());
        assertEquals(expected.getUser(), actual.getUser());
    }
}
