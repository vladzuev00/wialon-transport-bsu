package by.bsu.wialontransport.protocol.core.handler.packages.login;

import by.bsu.wialontransport.crud.dto.Data;
import by.bsu.wialontransport.crud.dto.Tracker;
import by.bsu.wialontransport.crud.service.DataService;
import by.bsu.wialontransport.crud.service.TrackerService;
import by.bsu.wialontransport.protocol.core.connectionmanager.ConnectionManager;
import by.bsu.wialontransport.protocol.core.contextattributemanager.ContextAttributeManager;
import by.bsu.wialontransport.protocol.core.model.packages.Package;
import by.bsu.wialontransport.protocol.core.model.packages.login.LoginPackage;
import io.netty.channel.ChannelHandlerContext;
import lombok.Builder;
import lombok.Value;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Optional;

import static java.util.Optional.empty;
import static java.util.Optional.ofNullable;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public final class LoginPackageHandlerTest {

    @Mock
    private ContextAttributeManager mockedContextAttributeManager;

    @Mock
    private TrackerService mockedTrackerService;

    @Mock
    private ConnectionManager mockedConnectionManager;

    @Mock
    private DataService mockedDataService;

    @Test
    public void packageShouldBeHandledInternallySuccessfullyWithExistingLastData() {
        final String givenImei = "11111222223333344444";
        final TestLoginPackage givenRequest = new TestLoginPackage(givenImei);

        final ChannelHandlerContext givenContext = mock(ChannelHandlerContext.class);

        final Package givenResponse = new Package() {
        };
        final TestLoginPackageHandler givenHandler = createHandlerWithSuccessResponse(givenResponse);

        final Tracker givenTracker = createTracker(255L);
        when(mockedTrackerService.findByImei(same(givenImei))).thenReturn(Optional.of(givenTracker));

        final Data givenLastData = createData(256L);
        when(mockedDataService.findTrackerLastDataFetchingParameters(same(givenTracker)))
                .thenReturn(Optional.of(givenLastData));

        final Package actual = givenHandler.handleInternal(givenRequest, givenContext);
        assertSame(givenResponse, actual);

        verify(mockedContextAttributeManager, times(1)).putTrackerImei(
                same(givenContext),
                same(givenImei)
        );
        verify(mockedContextAttributeManager, times(1)).putTracker(
                same(givenContext),
                same(givenTracker)
        );
        verify(mockedConnectionManager, times(1)).add(same(givenContext));
        verify(mockedContextAttributeManager, times(1)).putLastData(
                same(givenContext),
                same(givenLastData)
        );
        verifyNoInteractions(givenContext);
    }

    @Test
    public void packageShouldBeHandledInternallySuccessfullyWithNotExistingLastData() {
        final String givenImei = "11111222223333344444";
        final TestLoginPackage givenRequest = new TestLoginPackage(givenImei);

        final ChannelHandlerContext givenContext = mock(ChannelHandlerContext.class);

        final Package givenResponse = new Package() {
        };
        final TestLoginPackageHandler givenHandler = createHandlerWithSuccessResponse(givenResponse);

        final Tracker givenTracker = createTracker(255L);
        when(mockedTrackerService.findByImei(same(givenImei))).thenReturn(Optional.of(givenTracker));

        when(mockedDataService.findTrackerLastDataFetchingParameters(same(givenTracker))).thenReturn(empty());

        final Package actual = givenHandler.handleInternal(givenRequest, givenContext);
        assertSame(givenResponse, actual);

        verify(mockedContextAttributeManager, times(1)).putTrackerImei(
                same(givenContext),
                same(givenImei)
        );
        verify(mockedContextAttributeManager, times(1)).putTracker(
                same(givenContext),
                same(givenTracker)
        );
        verify(mockedConnectionManager, times(1)).add(same(givenContext));
        verify(mockedContextAttributeManager, times(0)).putLastData(
                any(ChannelHandlerContext.class),
                any(Data.class)
        );
        verifyNoInteractions(givenContext);
    }

    @Test
    public void packageShouldBeHandledInternallyFailureBecauseOfNoSuchImei() {
        final String givenImei = "11111222223333344444";
        final TestLoginPackage givenRequest = new TestLoginPackage(givenImei);

        final ChannelHandlerContext givenContext = mock(ChannelHandlerContext.class);

        final Package givenResponse = new Package() {
        };
        final TestLoginPackageHandler givenHandler = createHandlerWithNoSuchImeiResponse(givenResponse);

        when(mockedTrackerService.findByImei(same(givenImei))).thenReturn(empty());

        final Package actual = givenHandler.handleInternal(givenRequest, givenContext);
        assertSame(givenResponse, actual);

        verify(mockedContextAttributeManager, times(1)).putTrackerImei(
                same(givenContext),
                same(givenImei)
        );
        verify(mockedContextAttributeManager, times(0)).putTracker(
                any(ChannelHandlerContext.class),
                any(Tracker.class)
        );
        verifyNoInteractions(mockedConnectionManager);
        verifyNoInteractions(mockedDataService);
        verify(mockedContextAttributeManager, times(0)).putLastData(
                any(ChannelHandlerContext.class),
                any(Data.class)
        );
        verifyNoInteractions(givenContext);
    }

    @Test
    public void packageShouldBeHandledInternallyFailureInCaseExistingTracker() {
        final String givenImei = "11111222223333344444";
        final TestLoginPackage givenRequest = new TestLoginPackage(givenImei);

        final ChannelHandlerContext givenContext = mock(ChannelHandlerContext.class);

        final Package givenResponse = new Package() {
        };
        final TestLoginPackageHandler givenHandler = createHandlerWithLoginFailedResponse(givenResponse);

        final Tracker givenTracker = createTracker(259L);
        when(mockedTrackerService.findByImei(same(givenImei))).thenReturn(Optional.of(givenTracker));

        final Package actual = givenHandler.handleInternal(givenRequest, givenContext);
        assertSame(givenResponse, actual);

        verify(mockedContextAttributeManager, times(1)).putTrackerImei(
                same(givenContext),
                same(givenImei)
        );
        verify(mockedContextAttributeManager, times(0)).putTracker(
                any(ChannelHandlerContext.class),
                any(Tracker.class)
        );
        verifyNoInteractions(mockedConnectionManager);
        verifyNoInteractions(mockedDataService);
        verify(mockedContextAttributeManager, times(0)).putLastData(
                any(ChannelHandlerContext.class),
                any(Data.class)
        );
        verifyNoInteractions(givenContext);
    }

    private TestLoginPackageHandler createHandlerWithSuccessResponse(final Package response) {
        return createHandlerBuilder()
                .successResponse(response)
                .build();
    }

    private TestLoginPackageHandler createHandlerWithNoSuchImeiResponse(final Package response) {
        return createHandlerBuilder()
                .noSuchImeiResponse(response)
                .build();
    }

    private TestLoginPackageHandler createHandlerWithLoginFailedResponse(final Package response) {
        return createHandlerBuilder()
                .loginFailedResponse(response)
                .build();
    }

    private TestLoginPackageHandler.TestLoginPackageHandlerBuilder createHandlerBuilder() {
        return TestLoginPackageHandler.builder()
                .contextAttributeManager(mockedContextAttributeManager)
                .trackerService(mockedTrackerService)
                .connectionManager(mockedConnectionManager)
                .dataService(mockedDataService);
    }

    private static Tracker createTracker(final Long id) {
        return Tracker.builder()
                .id(id)
                .build();
    }

    @SuppressWarnings("SameParameterValue")
    private static Data createData(final Long id) {
        return Data.builder()
                .id(id)
                .build();
    }

    @Value
    private static class TestLoginPackage implements LoginPackage {
        String imei;
    }

    private static final class TestLoginPackageHandler extends LoginPackageHandler<TestLoginPackage> {
        private final Package loginFailedResponse;
        private final Package noSuchImeiResponse;
        private final Package successResponse;

        @Builder
        public TestLoginPackageHandler(final ContextAttributeManager contextAttributeManager,
                                       final TrackerService trackerService,
                                       final ConnectionManager connectionManager,
                                       final DataService dataService,
                                       final Package noSuchImeiResponse,
                                       final Package loginFailedResponse,
                                       final Package successResponse) {
            super(TestLoginPackage.class, contextAttributeManager, trackerService, connectionManager, dataService);
            this.noSuchImeiResponse = noSuchImeiResponse;
            this.loginFailedResponse = loginFailedResponse;
            this.successResponse = successResponse;
        }

        @Override
        protected Optional<Package> loginCreatingResponseIfFailed(final Tracker tracker, final TestLoginPackage request) {
            return ofNullable(loginFailedResponse);
        }

        @Override
        protected Package createNoSuchImeiResponse() {
            return noSuchImeiResponse;
        }

        @Override
        protected Package createSuccessResponse() {
            return successResponse;
        }
    }
}
