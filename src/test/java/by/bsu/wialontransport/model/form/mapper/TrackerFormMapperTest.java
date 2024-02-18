package by.bsu.wialontransport.model.form.mapper;

import by.bsu.wialontransport.base.AbstractSpringBootTest;
import by.bsu.wialontransport.crud.dto.Tracker;
import by.bsu.wialontransport.crud.dto.User;
import by.bsu.wialontransport.model.form.TrackerForm;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.Assert.assertEquals;

public final class TrackerFormMapperTest extends AbstractSpringBootTest {

    @Autowired
    private TrackerFormMapper mapper;

    @Test
    public void trackerShouldBeMappedToForm() {
        final Tracker givenTracker = Tracker.builder()
                .id(255L)
                .imei("11112222333344445555")
                .password("password")
                .phoneNumber("447336934")
                .build();

        final TrackerForm actual = this.mapper.map(givenTracker);
        final TrackerForm expected = TrackerForm.builder()
                .id(255L)
                .imei("11112222333344445555")
                .password("password")
                .phoneNumber("447336934")
                .build();
        assertEquals(expected, actual);
    }

    @Test
    public void formShouldBeMappedToTracker() {
        final User givenUser = User.builder()
                .id(255L)
                .build();
        final TrackerForm givenForm = TrackerForm.builder()
                .id(256L)
                .imei("11112222333344445555")
                .password("password")
                .phoneNumber("447336934")
                .build();

       final Tracker actual = this.mapper.map(givenForm, givenUser);
       final Tracker expected = Tracker.builder()
                .id(256L)
                .imei("11112222333344445555")
                .password("password")
                .phoneNumber("447336934")
                .user(givenUser)
                .build();
       assertEquals(expected, actual);
    }

}
