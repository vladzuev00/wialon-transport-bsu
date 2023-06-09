package by.bsu.wialontransport.service.useraction;

import by.bsu.wialontransport.crud.dto.Tracker;
import by.bsu.wialontransport.crud.dto.User;
import by.bsu.wialontransport.crud.service.TrackerService;
import by.bsu.wialontransport.model.form.TrackerForm;
import by.bsu.wialontransport.model.form.mapper.TrackerFormMapper;
import by.bsu.wialontransport.model.sortingkey.TrackerSortingKey;
import by.bsu.wialontransport.security.service.SecurityService;
import by.bsu.wialontransport.service.useraction.changepassword.ChangingPasswordService;
import by.bsu.wialontransport.service.useraction.exception.TrackerUniqueConstraintException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.ui.Model;

import java.util.List;

import static by.bsu.wialontransport.model.sortingkey.TrackerSortingKey.IMEI;
import static java.util.Optional.empty;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.same;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public final class UserActionServiceTest {

    @Mock
    private SecurityService mockedSecurityService;

    @Mock
    private TrackerService mockedTrackerService;

    @Mock
    private TrackerFormMapper mockedMapper;

    @Mock
    private ChangingPasswordService mockedChangingPasswordService;

    private UserActionService userActionService;

    @Before
    public void initializeUserActionService() {
        this.userActionService = new UserActionService(
                this.mockedSecurityService,
                this.mockedTrackerService,
                this.mockedMapper,
                this.mockedChangingPasswordService
        );
    }

    @Test
    public void attributeOfTrackersShouldBeAddedToShowProfilePage() {
        final int givenPageNumber = 0;
        final int givenPageSize = 5;
        final TrackerSortingKey givenSortingKey = IMEI;
        final Model givenModel = mock(Model.class);
        final String givenAttributeName = "listed_trackers";

        final User givenLoggedOnUser = createUser(255L);
        when(this.mockedSecurityService.findLoggedOnUser()).thenReturn(givenLoggedOnUser);

        final List<Tracker> givenListedTrackers = List.of(createTracker(256L), createTracker(257L));
        when(this.mockedTrackerService.findByUser(
                same(givenLoggedOnUser),
                eq(givenPageNumber),
                eq(givenPageSize),
                same(givenSortingKey.getComparator())
        )).thenReturn(givenListedTrackers);

        this.userActionService.addAttributeOfTrackersToShowProfilePage(
                givenPageNumber, givenPageSize, givenSortingKey, givenModel, givenAttributeName
        );

        verify(givenModel, times(1)).addAttribute(
                same(givenAttributeName), same(givenListedTrackers)
        );
    }

    @Test
    public void attributeOfTrackerFormShouldBeAddedToAddTracker() {
        final Model givenModel = mock(Model.class);
        final String givenAttributeName = "tracker_form";

        this.userActionService.addAttributeOfTrackerFormToAddTracker(givenModel, givenAttributeName);

        final TrackerForm expectedTrackerForm = new TrackerForm();
        verify(givenModel, times(1)).addAttribute(
                same(givenAttributeName), eq(expectedTrackerForm)
        );
    }

    @Test
    public void trackerShouldBeAdded()
            throws TrackerUniqueConstraintException {
        final TrackerForm givenForm = createTrackerForm("11112222333344445555", "447336934");
        final Model givenModel = mock(Model.class);

        when(this.mockedTrackerService.findByImei(eq("11112222333344445555"))).thenReturn(empty());
        when(this.mockedTrackerService.findByPhoneNumber(eq("447336934"))).thenReturn(empty());

        final User givenLoggedOnUser = createUser(255L);
        when(this.mockedSecurityService.findLoggedOnUser()).thenReturn(givenLoggedOnUser);

        final Tracker givenTracker = createTracker(256L);
        when(this.mockedMapper.map(same(givenForm), same(givenLoggedOnUser))).thenReturn(givenTracker);

        this.userActionService.addTracker(givenForm, givenModel);

        verify(this.mockedTrackerService, times(1)).save(same(givenTracker));
    }

    @Test
    public void trackerShouldNotBeAddedBecauseOfImeiUniqueConstraint() {
        throw new RuntimeException();
    }

    private static User createUser(final Long id) {
        return User.builder()
                .id(id)
                .build();
    }

    private static Tracker createTracker(final Long id) {
        return Tracker.builder()
                .id(id)
                .build();
    }

    private static TrackerForm createTrackerForm(final String imei, final String phoneNumber) {
        return TrackerForm.builder()
                .imei(imei)
                .phoneNumber(phoneNumber)
                .build();
    }

}
