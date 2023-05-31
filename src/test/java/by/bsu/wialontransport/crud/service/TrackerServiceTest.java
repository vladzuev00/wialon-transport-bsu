package by.bsu.wialontransport.crud.service;

import by.bsu.wialontransport.base.AbstractContextTest;
import by.bsu.wialontransport.crud.dto.Tracker;
import by.bsu.wialontransport.crud.dto.User;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import static java.util.Comparator.comparing;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public final class TrackerServiceTest extends AbstractContextTest {

    @Autowired
    private TrackerService service;

    @Test
    public void trackerShouldBeFoundByImei() {
        final Tracker actual = this.service.findByImei("11112222333344445555").orElseThrow();
        final Tracker expected = Tracker.builder()
                .id(255L)
                .imei("11112222333344445555")
                .password("password")
                .phoneNumber("447336934")
                .build();
        assertEquals(expected, actual);
    }

    @Test
    public void trackerShouldNotBeFoundByImei() {
        final Optional<Tracker> optionalFoundTracker = this.service.findByImei("00000000000000000000");
        assertTrue(optionalFoundTracker.isEmpty());
    }

    @Test
    @Sql(statements = "INSERT INTO trackers(id, imei, encrypted_password, phone_number, user_id) "
            + "VALUES(355, '11111222223333344444', 'password', '447336935', 255)")
    @Sql(statements = "INSERT INTO trackers(id, imei, encrypted_password, phone_number, user_id) "
            + "VALUES(356, '11111222223333344445', 'password', '447336936', 255)")
    public void trackersShouldBeFoundByUserId() {
        final User givenUser = createUser(255L);

        final List<Tracker> actual = this.service.findByUser(givenUser, 0, 5);
        final List<Tracker> expected = List.of(
                Tracker.builder()
                        .id(255L)
                        .imei("11112222333344445555")
                        .password("password")
                        .phoneNumber("447336934")
                        .build(),
                Tracker.builder()
                        .id(355L)
                        .imei("11111222223333344444")
                        .password("password")
                        .phoneNumber("447336935")
                        .build(),
                Tracker.builder()
                        .id(356L)
                        .imei("11111222223333344445")
                        .password("password")
                        .phoneNumber("447336936")
                        .build()
        );
        assertEquals(expected, actual);
    }

    @Test
    public void trackersShouldNotBeFoundByUserId() {
        final User givenUser = createUser(256L);

        final List<Tracker> actual = this.service.findByUser(givenUser, 0, 5);
        assertTrue(actual.isEmpty());
    }

    @Test
    @Sql(statements = "INSERT INTO trackers(id, imei, encrypted_password, phone_number, user_id) "
            + "VALUES(355, '11111222223333344444', 'password', '447336935', 255)")
    @Sql(statements = "INSERT INTO trackers(id, imei, encrypted_password, phone_number, user_id) "
            + "VALUES(356, '11111222223333344445', 'password', '447336936', 255)")
    public void sortedTrackersShouldBeFoundByUserId() {
        final User givenUser = createUser(255L);
        final Comparator<Tracker> givenComparator = comparing(Tracker::getImei).reversed();

        final List<Tracker> actual = this.service.findByUser(givenUser, 0, 5, givenComparator);
        final List<Tracker> expected = List.of(
                Tracker.builder()
                        .id(255L)
                        .imei("11112222333344445555")
                        .password("password")
                        .phoneNumber("447336934")
                        .build(),
                Tracker.builder()
                        .id(356L)
                        .imei("11111222223333344445")
                        .password("password")
                        .phoneNumber("447336936")
                        .build(),
                Tracker.builder()
                        .id(355L)
                        .imei("11111222223333344444")
                        .password("password")
                        .phoneNumber("447336935")
                        .build()
        );
        assertEquals(expected, actual);
    }

    @Test
    public void sortedTrackersShouldNotBeFoundByUserId() {
        final User givenUser = createUser(256L);
        final Comparator<Tracker> givenComparator = comparing(Tracker::getImei).reversed();

        final List<Tracker> actual = this.service.findByUser(givenUser, 0, 5, givenComparator);
        assertTrue(actual.isEmpty());
    }

    private static User createUser(final Long id) {
        return User.builder()
                .id(id)
                .build();
    }
}
