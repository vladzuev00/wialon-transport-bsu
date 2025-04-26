package by.vladzuev.locationreceiver.protocol.core.handler.packages.login;

import by.vladzuev.locationreceiver.crud.dto.Location;
import by.vladzuev.locationreceiver.crud.dto.Tracker;
import by.vladzuev.locationreceiver.crud.service.LocationService;
import by.vladzuev.locationreceiver.crud.service.TrackerService;
import by.vladzuev.locationreceiver.protocol.core.manager.ContextAttributeManager;
import by.vladzuev.locationreceiver.protocol.core.manager.ContextManager;
import by.vladzuev.locationreceiver.protocol.core.handler.packages.login.factory.TrackerImeiFactory;
import by.vladzuev.locationreceiver.protocol.core.model.LoginPackage;
import io.netty.channel.ChannelHandlerContext;
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
    private TrackerImeiFactory mockedImeiFactory;

    @Mock
    private ContextAttributeManager mockedContextAttributeManager;

    @Mock
    private TrackerService mockedTrackerService;

    @Mock
    private ContextManager mockedContextManager;

    @Mock
    private LocationService mockedLocationService;

    private TestLoginPackageHandler handler;

    @BeforeEach
    public void initializeHandler() {
        handler = new TestLoginPackageHandler(
                mockedImeiFactory,
                mockedContextAttributeManager,
                mockedTrackerService,
                mockedContextManager,
                mockedLocationService
        );
    }

    @Test
    public void requestShouldBeHandledInternallySuccessfullyWithMemorizingLastData() {
        final String givenPassword = "111";
        final TestPackage givenRequest = new TestPackage(givenPassword);
        final ChannelHandlerContext givenContext = mock(ChannelHandlerContext.class);

        final String givenImei = mockImei("00000000000000006308", givenRequest);
        final Tracker givenTracker = mockTracker(givenImei, givenPassword);
        final Location givenLocation = mockLastLocation(givenTracker);

        final Object actual = handler.handleInternal(givenRequest, givenContext);
        final TestResponsePackage expected = new TestResponsePackage(ResponseStatus.SUCCESS);
        assertEquals(expected, actual);

        verify(mockedContextAttributeManager, times(1)).putImei(same(givenContext), same(givenImei));
        verify(mockedContextAttributeManager, times(1)).putTracker(same(givenContext), same(givenTracker));
        verify(mockedContextAttributeManager, times(1)).putLastLocation(same(givenContext), same(givenLocation));
        verify(mockedContextManager, times(1)).add(same(givenContext));
    }

    @Test
    public void requestShouldBeHandledInternallySuccessfullyWithoutMemorizingLastData() {
        final String givenPassword = "112";
        final TestPackage givenRequest = new TestPackage(givenPassword);
        final ChannelHandlerContext givenContext = mock(ChannelHandlerContext.class);

        final String givenImei = mockImei("00000000000000006308", givenRequest);
        final Tracker givenTracker = mockTracker(givenImei, givenPassword);
        mockNoLastLocation(givenTracker);

        final Object actual = handler.handleInternal(givenRequest, givenContext);
        final TestResponsePackage expected = new TestResponsePackage(ResponseStatus.SUCCESS);
        assertEquals(expected, actual);

        verify(mockedContextAttributeManager, times(1)).putImei(same(givenContext), same(givenImei));
        verify(mockedContextAttributeManager, times(1)).putTracker(same(givenContext), same(givenTracker));
        verify(mockedContextAttributeManager, times(0)).putLastLocation(any(ChannelHandlerContext.class), any(Location.class));
        verify(mockedContextManager, times(1)).add(same(givenContext));
    }

    @Test
    public void requestShouldBeHandledInternallyFailureBecauseOfNoSuchTracker() {
        final String givenPassword = "111";
        final TestPackage givenRequest = new TestPackage(givenPassword);
        final ChannelHandlerContext givenContext = mock(ChannelHandlerContext.class);

        final String givenImei = mockImei("00000000000000006308", givenRequest);
        mockNoTracker(givenImei);

        final Object actual = handler.handleInternal(givenRequest, givenContext);
        final TestResponsePackage expected = new TestResponsePackage(ResponseStatus.NO_SUCH_IMEI);
        assertEquals(expected, actual);

        verify(mockedContextAttributeManager, times(1)).putImei(same(givenContext), same(givenImei));
        verify(mockedContextAttributeManager, times(0)).putTracker(any(ChannelHandlerContext.class), any(Tracker.class));
        verify(mockedContextAttributeManager, times(0)).putLastLocation(any(ChannelHandlerContext.class), any(Location.class));
        verifyNoInteractions(mockedContextManager, mockedLocationService);
    }

    @Test
    public void requestShouldBeHandledInternallyFailureBecauseOfWrongPassword() {
        final TestPackage givenRequest = new TestPackage("111");
        final ChannelHandlerContext givenContext = mock(ChannelHandlerContext.class);

        final String givenImei = mockImei("00000000000000006309", givenRequest);
        mockTracker(givenImei, "1111");

        final Object actual = handler.handleInternal(givenRequest, givenContext);
        final TestResponsePackage expected = new TestResponsePackage(ResponseStatus.WRONG_PASSWORD);
        assertEquals(expected, actual);

        verify(mockedContextAttributeManager, times(1)).putImei(same(givenContext), same(givenImei));
        verify(mockedContextAttributeManager, times(0)).putTracker(any(ChannelHandlerContext.class), any(Tracker.class));
        verify(mockedContextAttributeManager, times(0)).putLastLocation(any(ChannelHandlerContext.class), any(Location.class));
        verifyNoInteractions(mockedContextManager, mockedLocationService);
    }

    private String mockImei(final String imei, final LoginPackage request) {
        when(mockedImeiFactory.create(same(request))).thenReturn(imei);
        return imei;
    }

    private Tracker mockTracker(final String imei, final String password) {
        final Tracker tracker = Tracker.builder().imei(imei).password(password).build();
        when(mockedTrackerService.findByImei(same(imei))).thenReturn(Optional.of(tracker));
        return tracker;
    }

    private Location mockLastLocation(final Tracker tracker) {
        final Location location = Location.builder().build();
        when(mockedLocationService.findLastFetchingParameters(same(tracker))).thenReturn(Optional.of(location));
        return location;
    }

    private void mockNoTracker(@SuppressWarnings("SameParameterValue") final String imei) {
        when(mockedTrackerService.findByImei(same(imei))).thenReturn(empty());
    }

    private void mockNoLastLocation(final Tracker tracker) {
        when(mockedLocationService.findLastFetchingParameters(same(tracker))).thenReturn(empty());
    }

    @Value
    private static class TestPackage implements LoginPackage {
        String password;

        @Override
        public String getImei() {
            throw new UnsupportedOperationException();
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

        public TestLoginPackageHandler(final TrackerImeiFactory imeiFactory,
                                       final ContextAttributeManager contextAttributeManager,
                                       final TrackerService trackerService,
                                       final ContextManager contextManager,
                                       final LocationService locationService) {
            super(
                    TestPackage.class,
                    imeiFactory,
                    contextAttributeManager,
                    trackerService,
                    contextManager,
                    locationService
            );
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
