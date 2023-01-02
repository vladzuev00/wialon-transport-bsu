package by.bsu.wialontransport.crud.repository;

import by.bsu.wialontransport.base.AbstractContextTest;
import by.bsu.wialontransport.crud.entity.TrackerEntity;
import by.bsu.wialontransport.crud.entity.UserEntity;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.Assert.assertEquals;

public final class TrackerRepositoryTest extends AbstractContextTest {

    @Autowired
    private TrackerRepository repository;

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

    private static void checkEquals(final TrackerEntity expected, final TrackerEntity actual) {
        assertEquals(expected.getId(), actual.getId());
        assertEquals(expected.getImei(), actual.getImei());
        assertEquals(expected.getPassword(), actual.getPassword());
        assertEquals(expected.getPhoneNumber(), actual.getPhoneNumber());
        assertEquals(expected.getUser(), actual.getUser());
    }
}
