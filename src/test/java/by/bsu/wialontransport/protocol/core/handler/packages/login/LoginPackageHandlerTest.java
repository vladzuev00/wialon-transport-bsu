//package by.bsu.wialontransport.protocol.core.handler.packages.login;
//
//import by.bsu.wialontransport.crud.dto.Data;
//import by.bsu.wialontransport.crud.dto.Tracker;
//import by.bsu.wialontransport.crud.service.DataService;
//import by.bsu.wialontransport.crud.service.TrackerService;
//import by.bsu.wialontransport.protocol.core.connectionmanager.ConnectionManager;
//import by.bsu.wialontransport.protocol.core.contextattributemanager.ContextAttributeManager;
//import by.bsu.wialontransport.protocol.core.model.packages.Package;
//import by.bsu.wialontransport.protocol.core.model.packages.login.LoginPackage;
//import io.netty.channel.ChannelHandlerContext;
//import lombok.Builder;
//import lombok.Value;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.mockito.Mock;
//import org.mockito.junit.MockitoJUnitRunner;
//
//import java.util.Optional;
//
//import static java.util.Optional.empty;
//import static java.util.Optional.ofNullable;
//import static org.mockito.Mockito.*;
//
//@RunWith(MockitoJUnitRunner.class)
//public final class LoginPackageHandlerTest {
//
//    @Mock
//    private ContextAttributeManager mockedContextAttributeManager;
//
//    @Mock
//    private TrackerService mockedTrackerService;
//
//    @Mock
//    private ConnectionManager mockedConnectionManager;
//
//    @Mock
//    private DataService mockedDataService;
//
//    @Test
//    public void concretePackageShouldBeHandledSuccessfullyWithExistingLastData() {
//        final String givenTrackerImei = "11111222223333344444";
//        final TestLoginPackage givenLoginPackage = new TestLoginPackage(givenTrackerImei);
//
//        final ChannelHandlerContext givenContext = mock(ChannelHandlerContext.class);
//
//        final Package givenSuccessResponse = new Package() {
//        };
//        final TestLoginPackageHandler givenHandler = this.createHandlerBySuccessResponse(givenSuccessResponse);
//
//        final Tracker givenTracker = createTracker(255L);
//        when(this.mockedTrackerService.findByImei(same(givenTrackerImei))).thenReturn(Optional.of(givenTracker));
//
//        final Data givenLastData = createData(256L);
//        when(this.mockedDataService.findTrackerLastData(same(givenTracker))).thenReturn(Optional.of(givenLastData));
//
//        givenHandler.handleConcretePackage(givenLoginPackage, givenContext);
//
//        verify(this.mockedContextAttributeManager, times(1)).putTrackerImei(
//                same(givenContext),
//                same(givenTrackerImei)
//        );
//        verify(this.mockedContextAttributeManager, times(1)).putTracker(
//                same(givenContext),
//                same(givenTracker)
//        );
//        verify(this.mockedConnectionManager, times(1)).add(same(givenContext));
//        verify(this.mockedContextAttributeManager, times(1)).putLastData(
//                same(givenContext),
//                same(givenLastData)
//        );
//        verify(givenContext, times(1)).writeAndFlush(same(givenSuccessResponse));
//    }
//
//    @Test
//    public void concretePackageShouldBeHandledSuccessfullyWithNotExistingLastData() {
//        final String givenTrackerImei = "11111222223333344444";
//        final TestLoginPackage givenLoginPackage = new TestLoginPackage(givenTrackerImei);
//
//        final ChannelHandlerContext givenContext = mock(ChannelHandlerContext.class);
//
//        final Package givenSuccessResponse = new Package() {
//        };
//        final TestLoginPackageHandler givenHandler = this.createHandlerBySuccessResponse(givenSuccessResponse);
//
//        final Tracker givenTracker = createTracker(255L);
//        when(this.mockedTrackerService.findByImei(same(givenTrackerImei))).thenReturn(Optional.of(givenTracker));
//
//        when(this.mockedDataService.findTrackerLastData(same(givenTracker))).thenReturn(empty());
//
//        givenHandler.handleConcretePackage(givenLoginPackage, givenContext);
//
//        verify(this.mockedContextAttributeManager, times(1)).putTrackerImei(
//                same(givenContext),
//                same(givenTrackerImei)
//        );
//        verify(this.mockedContextAttributeManager, times(1)).putTracker(
//                same(givenContext),
//                same(givenTracker)
//        );
//        verify(this.mockedConnectionManager, times(1)).add(same(givenContext));
//        verify(this.mockedContextAttributeManager, times(0)).putLastData(
//                any(ChannelHandlerContext.class),
//                any(Data.class)
//        );
//        verify(givenContext, times(1)).writeAndFlush(same(givenSuccessResponse));
//    }
//
//    @Test
//    public void concretePackageShouldBeHandledFailureBecauseOfNoSuchImei() {
//        final String givenTrackerImei = "11111222223333344444";
//        final TestLoginPackage givenLoginPackage = new TestLoginPackage(givenTrackerImei);
//
//        final ChannelHandlerContext givenContext = mock(ChannelHandlerContext.class);
//
//        final Package givenNoSuchImeiResponse = new Package() {
//        };
//        final TestLoginPackageHandler givenHandler = this.createHandlerByNoSuchImeiResponse(givenNoSuchImeiResponse);
//
//        when(this.mockedTrackerService.findByImei(same(givenTrackerImei))).thenReturn(empty());
//
//        givenHandler.handleConcretePackage(givenLoginPackage, givenContext);
//
//        verify(this.mockedContextAttributeManager, times(1)).putTrackerImei(
//                same(givenContext),
//                same(givenTrackerImei)
//        );
//        verify(this.mockedContextAttributeManager, times(0)).putTracker(
//                any(ChannelHandlerContext.class),
//                any(Tracker.class)
//        );
//        verify(this.mockedConnectionManager, times(0)).add(any(ChannelHandlerContext.class));
//        verify(this.mockedDataService, times(0)).findTrackerLastData(any(Tracker.class));
//        verify(this.mockedContextAttributeManager, times(0)).putLastData(
//                any(ChannelHandlerContext.class),
//                any(Data.class)
//        );
//        verify(givenContext, times(1)).writeAndFlush(same(givenNoSuchImeiResponse));
//    }
//
//    @Test
//    public void concretePackageShouldBeHandledFailureInCaseExistingTracker() {
//        final String givenTrackerImei = "11111222223333344444";
//        final TestLoginPackage givenLoginPackage = new TestLoginPackage(givenTrackerImei);
//
//        final ChannelHandlerContext givenContext = mock(ChannelHandlerContext.class);
//
//        final Package givenLoginFailedResponse = new Package() {
//        };
//        final TestLoginPackageHandler givenHandler = this.createHandlerByLoginFailedResponse(givenLoginFailedResponse);
//
//        final Tracker givenTracker = createTracker(259L);
//        when(this.mockedTrackerService.findByImei(same(givenTrackerImei))).thenReturn(Optional.of(givenTracker));
//
//        givenHandler.handleConcretePackage(givenLoginPackage, givenContext);
//
//        verify(this.mockedContextAttributeManager, times(1)).putTrackerImei(
//                same(givenContext),
//                same(givenTrackerImei)
//        );
//        verify(this.mockedContextAttributeManager, times(0)).putTracker(
//                any(ChannelHandlerContext.class),
//                any(Tracker.class)
//        );
//        verify(this.mockedConnectionManager, times(0)).add(any(ChannelHandlerContext.class));
//        verify(this.mockedDataService, times(0)).findTrackerLastData(any(Tracker.class));
//        verify(this.mockedContextAttributeManager, times(0)).putLastData(
//                any(ChannelHandlerContext.class),
//                any(Data.class)
//        );
//        verify(givenContext, times(1)).writeAndFlush(same(givenLoginFailedResponse));
//    }
//
//    private TestLoginPackageHandler createHandlerBySuccessResponse(final Package successResponse) {
//        return this.createHandlerBuilder()
//                .successResponse(successResponse)
//                .build();
//    }
//
//    private TestLoginPackageHandler createHandlerByNoSuchImeiResponse(final Package noSuchImeiResponse) {
//        return this.createHandlerBuilder()
//                .noSuchImeiResponse(noSuchImeiResponse)
//                .build();
//    }
//
//    private TestLoginPackageHandler createHandlerByLoginFailedResponse(final Package loginFailedResponse) {
//        return this.createHandlerBuilder()
//                .loginFailedResponse(loginFailedResponse)
//                .build();
//    }
//
//    private TestLoginPackageHandler.TestLoginPackageHandlerBuilder createHandlerBuilder() {
//        return TestLoginPackageHandler.builder()
//                .contextAttributeManager(this.mockedContextAttributeManager)
//                .trackerService(this.mockedTrackerService)
//                .connectionManager(this.mockedConnectionManager)
//                .dataService(this.mockedDataService);
//    }
//
//    private static Tracker createTracker(final Long id) {
//        return Tracker.builder()
//                .id(id)
//                .build();
//    }
//
//    private static Data createData(final Long id) {
//        return Data.builder()
//                .id(id)
//                .build();
//    }
//
//    @Value
//    private static class TestLoginPackage implements LoginPackage {
//        String imei;
//    }
//
//    private static final class TestLoginPackageHandler extends LoginPackageHandler<TestLoginPackage> {
//        private final Package loginFailedResponse;
//        private final Package noSuchImeiResponse;
//        private final Package successResponse;
//
//        @Builder
//        public TestLoginPackageHandler(final ContextAttributeManager contextAttributeManager,
//                                       final TrackerService trackerService,
//                                       final ConnectionManager connectionManager,
//                                       final DataService dataService,
//                                       final Package noSuchImeiResponse,
//                                       final Package loginFailedResponse,
//                                       final Package successResponse) {
//            super(TestLoginPackage.class, contextAttributeManager, trackerService, connectionManager, dataService);
//            this.noSuchImeiResponse = noSuchImeiResponse;
//            this.loginFailedResponse = loginFailedResponse;
//            this.successResponse = successResponse;
//        }
//
//        @Override
//        protected Optional<Package> checkLoginCreatingResponseIfFailed(final Tracker tracker,
//                                                                       final TestLoginPackage loginPackage) {
//            return ofNullable(this.loginFailedResponse);
//        }
//
//        @Override
//        protected Package createNoSuchImeiResponse() {
//            return this.noSuchImeiResponse;
//        }
//
//        @Override
//        protected Package createSuccessResponse() {
//            return this.successResponse;
//        }
//    }
//}
