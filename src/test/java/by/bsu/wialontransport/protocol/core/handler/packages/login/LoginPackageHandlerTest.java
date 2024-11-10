package by.bsu.wialontransport.protocol.core.handler.packages.login;

import by.bsu.wialontransport.crud.dto.Location;
import by.bsu.wialontransport.crud.dto.Tracker;
import by.bsu.wialontransport.crud.service.LocationService;
import by.bsu.wialontransport.crud.service.TrackerService;
import by.bsu.wialontransport.protocol.core.contextattributemanager.ContextAttributeManager;
import by.bsu.wialontransport.protocol.core.contextmanager.ChannelHandlerContextManager;
import by.bsu.wialontransport.protocol.core.model.login.LoginPackage;
import io.netty.channel.ChannelHandlerContext;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import lombok.Value;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Objects;
import java.util.Optional;

import static java.util.Optional.empty;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.same;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public final class LoginPackageHandlerTest {

    @Mock
    private ContextAttributeManager mockedContextAttributeManager;

    @Mock
    private TrackerService mockedTrackerService;

    @Mock
    private ChannelHandlerContextManager mockedContextManager;

    @Mock
    private LocationService mockedLocationService;

    private TestLoginPackageHandler handler;

    @BeforeEach
    public void initializeHandler() {
        handler = new TestLoginPackageHandler(
                mockedContextAttributeManager,
                mockedTrackerService,
                mockedContextManager,
                mockedLocationService
        );
    }

    @Test
    public void requestShouldBeHandledInternallySuccessfullyWithMemorizingLastData() {
        final String givenImei = "11112222333344445555";
        final String givenPassword = "111";
        final TestPackage givenRequest = new TestPackage(givenImei, givenPassword);
        final ChannelHandlerContext givenContext = mock(ChannelHandlerContext.class);

        final Tracker givenTracker = mockTrackerExistence(givenImei, givenPassword);
        final Location givenLocation = mockLastLocationExistence(givenTracker);

        final Object actual = handler.handleInternal(givenRequest, givenContext);
        final TestResponsePackage expected = new TestResponsePackage(ResponseStatus.SUCCESS);
        assertEquals(expected, actual);

        verifyMemorizingImei(givenContext, givenImei);
        verifyMemorizingTracker(givenContext, givenTracker);
        verifyMemorizingLastLocation(givenContext, givenLocation);
        verifyMemorizingContext(givenContext);
    }

    @Test
    public void requestShouldBeHandledInternallySuccessfullyWithoutMemorizingLastData() {
        final String givenImei = "11112222333344445556";
        final String givenPassword = "112";
        final TestPackage givenRequest = new TestPackage(givenImei, givenPassword);
        final ChannelHandlerContext givenContext = mock(ChannelHandlerContext.class);

        final Tracker givenTracker = mockTrackerExistence(givenImei, givenPassword);
        mockLastLocationAbsence(givenTracker);

        final Object actual = handler.handleInternal(givenRequest, givenContext);
        final TestResponsePackage expected = new TestResponsePackage(ResponseStatus.SUCCESS);
        assertEquals(expected, actual);

        verifyMemorizingImei(givenContext, givenImei);
        verifyMemorizingTracker(givenContext, givenTracker);
        verifyNotMemorizingLastLocation();
        verifyMemorizingContext(givenContext);
    }

    @Test
    public void requestShouldBeHandledInternallyFailureBecauseOfNoSuchImei() {
        final String givenImei = "11112222333344445555";
        final String givenPassword = "111";
        final TestPackage givenRequest = new TestPackage(givenImei, givenPassword);
        final ChannelHandlerContext givenContext = mock(ChannelHandlerContext.class);

        mockTrackerAbsence(givenImei);

        final Object actual = handler.handleInternal(givenRequest, givenContext);
        final TestResponsePackage expected = new TestResponsePackage(ResponseStatus.NO_SUCH_IMEI);
        assertEquals(expected, actual);

        verifyMemorizingImei(givenContext, givenImei);
        verifyNotMemorizingTracker();
        verifyNotMemorizingLastLocation();
        verifyNoInteractions(mockedContextManager, mockedLocationService);
    }

    @Test
    public void requestShouldBeHandledInternallyFailureBecauseOfWrongPassword() {
        final String givenImei = "11112222333344445555";
        final TestPackage givenRequest = new TestPackage(givenImei, "111");
        final ChannelHandlerContext givenContext = mock(ChannelHandlerContext.class);

        mockTrackerExistence(givenImei, "1111");

        final Object actual = handler.handleInternal(givenRequest, givenContext);
        final TestResponsePackage expected = new TestResponsePackage(ResponseStatus.WRONG_PASSWORD);
        assertEquals(expected, actual);

        verifyMemorizingImei(givenContext, givenImei);
        verifyNotMemorizingTracker();
        verifyNotMemorizingLastLocation();
        verifyNoInteractions(mockedContextManager, mockedLocationService);
    }

    private Tracker mockTrackerExistence(final String imei, final String password) {
        final Tracker tracker = Tracker.builder().imei(imei).password(password).build();
        when(mockedTrackerService.findByImei(same(imei))).thenReturn(Optional.of(tracker));
        return tracker;
    }

    private Location mockLastLocationExistence(final Tracker tracker) {
        final Location location = Location.builder().build();
        when(mockedLocationService.findLastLocationFetchingParameters(same(tracker))).thenReturn(Optional.of(location));
        return location;
    }

    private void mockTrackerAbsence(@SuppressWarnings("SameParameterValue") final String imei) {
        when(mockedTrackerService.findByImei(same(imei))).thenReturn(empty());
    }

    private void mockLastLocationAbsence(final Tracker tracker) {
        when(mockedLocationService.findLastLocationFetchingParameters(same(tracker))).thenReturn(empty());
    }

    private void verifyMemorizingImei(final ChannelHandlerContext context, final String imei) {
        verify(mockedContextAttributeManager, times(1)).putTrackerImei(same(context), same(imei));
    }

    private void verifyMemorizingTracker(final ChannelHandlerContext context, final Tracker tracker) {
        verify(mockedContextAttributeManager, times(1)).putTracker(same(context), same(tracker));
    }

    private void verifyMemorizingLastLocation(final ChannelHandlerContext context, final Location location) {
        verify(mockedContextAttributeManager, times(1)).putLastLocation(same(context), same(location));
    }

    private void verifyMemorizingContext(final ChannelHandlerContext context) {
        verify(mockedContextManager, times(1)).add(same(context));
    }

    private void verifyNotMemorizingTracker() {
        verify(mockedContextAttributeManager, times(0)).putTracker(any(ChannelHandlerContext.class), any(Tracker.class));
    }

    private void verifyNotMemorizingLastLocation() {
        verify(mockedContextAttributeManager, times(0)).putLastLocation(any(ChannelHandlerContext.class), any(Location.class));
    }

    @Getter
    @EqualsAndHashCode(callSuper = true)
    @ToString(callSuper = true)
    private static final class TestPackage extends LoginPackage {
        private final String password;

        public TestPackage(final String imei, final String password) {
            super(imei);
            this.password = password;
        }
    }

    private enum ResponseStatus {
        SUCCESS, NO_SUCH_IMEI, WRONG_PASSWORD
    }

    @Value
    private static class TestResponsePackage {
        ResponseStatus status;
    }

    private static final class TestLoginPackageHandler extends LoginPackageHandler<TestPackage> {

        public TestLoginPackageHandler(final ContextAttributeManager contextAttributeManager,
                                       final TrackerService trackerService,
                                       final ChannelHandlerContextManager contextManager,
                                       final LocationService locationService) {
            super(TestPackage.class, contextAttributeManager, trackerService, contextManager, locationService);
        }

        @Override
        protected TestResponsePackage createNoSuchImeiResponse() {
            return new TestResponsePackage(ResponseStatus.NO_SUCH_IMEI);
        }

        @Override
        protected Optional<Object> loginCreatingFailedResponse(final Tracker tracker, final TestPackage request) {
            return Optional.of(request.password)
                    .filter(password -> !Objects.equals(password, tracker.getPassword()))
                    .map(password -> new TestResponsePackage(ResponseStatus.WRONG_PASSWORD));
        }

        @Override
        protected TestResponsePackage createSuccessResponse() {
            return new TestResponsePackage(ResponseStatus.SUCCESS);
        }
    }
}
