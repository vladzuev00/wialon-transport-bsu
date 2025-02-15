package by.vladzuev.locationreceiver.controller.tracker;

import by.vladzuev.locationreceiver.controller.exception.NoSuchEntityException;
import by.vladzuev.locationreceiver.controller.tracker.view.SaveTrackerView;
import by.vladzuev.locationreceiver.crud.dto.Tracker;
import by.vladzuev.locationreceiver.crud.dto.User;
import by.vladzuev.locationreceiver.crud.service.TrackerService;
import by.vladzuev.locationreceiver.crud.service.UserService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Optional;

import static java.util.Optional.empty;
import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.same;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public final class TrackerControllerTest {

    @Mock
    private TrackerService mockedTrackerService;

    @Mock
    private UserService mockedUserService;

    private TrackerController controller;

    @Before
    public void initializeController() {
        controller = new TrackerController(mockedTrackerService, mockedUserService);
    }

    @Test
    public void trackerShouldBeCreatedBySaveView() {
        final String givenImei = "11112222333344445555";
        final String givenPassword = "password";
        final String givenPhoneNumber = "447336934";
        final Long givenUserId = 255L;
        final SaveTrackerView givenView = new SaveTrackerView(givenImei, givenPassword, givenPhoneNumber, givenUserId);

        final User givenUser = createUser(givenUserId);
        when(mockedUserService.findById(same(givenUserId))).thenReturn(Optional.of(givenUser));

        final Tracker actual = controller.createDtoBySaveView(givenView);
        final Tracker expected = Tracker.builder()
                .imei(givenImei)
                .password(givenPassword)
                .phoneNumber(givenPhoneNumber)
                .user(givenUser)
                .build();
        assertEquals(expected, actual);

        verifyNoInteractions(mockedTrackerService);
    }

    @Test(expected = NoSuchEntityException.class)
    public void trackerShouldNotBeCreatedBySaveView() {
        final String givenImei = "11112222333344445555";
        final String givenPassword = "password";
        final String givenPhoneNumber = "447336934";
        final Long givenUserId = 255L;
        final SaveTrackerView givenView = new SaveTrackerView(givenImei, givenPassword, givenPhoneNumber, givenUserId);

        when(mockedUserService.findById(same(givenUserId))).thenReturn(empty());

        controller.createDtoBySaveView(givenView);
    }

//    @Test
//    public void trackerShouldBeCreatedByUpdateView() {
//        final Long givenId = 255L;
//        final String givenImei = "11112222333344445555";
//        final String givenPassword = "password";
//        final String givenPhoneNumber = "447336934";
//        final UpdateTrackerView givenView = new UpdateTrackerView(givenId, givenImei, givenPassword, givenPhoneNumber);
//
//        final Tracker actual = controller.createDtoByUpdateView(givenView);
//        final Tracker expected = Tracker.builder()
//                .id(givenId)
//                .imei(givenImei)
//                .password(givenPassword)
//                .phoneNumber(givenPhoneNumber)
//                .build();
//        assertEquals(expected, actual);
//    }
//
    @Test
    public void responseViewShouldBeCreated() {
//        final Long givenId = 255L;
//        final String givenImei = "11112222333344445555";
//        final String givenPhoneNumber = "447336934";
//        final Tracker givenTracker = Tracker.builder()
//                .id(givenId)
//                .imei(givenImei)
//                .phoneNumber(givenPhoneNumber)
//                .build();
//
//        final ResponseTrackerView actual = controller.createResponseView(givenTracker);
//        final ResponseTrackerView expected = new ResponseTrackerView(givenId, givenImei, givenPhoneNumber);
//        assertEquals(expected, actual);
        throw new RuntimeException();
    }

    private static User createUser(final Long id) {
        return User.builder()
                .id(id)
                .build();
    }
}
