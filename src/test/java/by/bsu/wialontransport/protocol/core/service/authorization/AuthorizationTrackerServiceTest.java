package by.bsu.wialontransport.protocol.core.service.authorization;

import by.bsu.wialontransport.crud.dto.Data;
import by.bsu.wialontransport.crud.dto.Tracker;
import by.bsu.wialontransport.crud.service.DataService;
import by.bsu.wialontransport.crud.service.TrackerService;
import by.bsu.wialontransport.protocol.core.connectionmanager.ConnectionManager;
import by.bsu.wialontransport.protocol.core.contextattributemanager.ContextAttributeManager;
import by.bsu.wialontransport.protocol.wialon.wialonpackage.login.RequestLoginPackage;
import by.bsu.wialontransport.protocol.wialon.wialonpackage.login.ResponseLoginPackage;
import io.netty.channel.ChannelHandlerContext;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.List;
import java.util.Optional;

import static by.bsu.wialontransport.protocol.wialon.wialonpackage.login.ResponseLoginPackage.Status.*;
import static java.util.Optional.empty;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public final class AuthorizationTrackerServiceTest {

    @Mock
    private ContextAttributeManager mockedContextAttributeManager;

    @Mock
    private TrackerService mockedTrackerService;

    @Mock
    private ConnectionManager mockedConnectionManager;

    @Mock
    private DataService mockedDataService;

    private AuthorizationTrackerService authorizationTrackerService;

    @Captor
    private ArgumentCaptor<ChannelHandlerContext> contextArgumentCaptor;

    @Captor
    private ArgumentCaptor<String> stringArgumentCaptor;

    @Captor
    private ArgumentCaptor<Tracker> trackerArgumentCaptor;

    @Captor
    private ArgumentCaptor<Long> longArgumentCaptor;

    @Captor
    private ArgumentCaptor<Data> dataArgumentCaptor;

    @Captor
    private ArgumentCaptor<ResponseLoginPackage> responseLoginPackageArgumentCaptor;

    @Before
    public void initializeAuthorizationDeviceService() {
        this.authorizationTrackerService = new AuthorizationTrackerService(this.mockedContextAttributeManager,
                this.mockedTrackerService, this.mockedConnectionManager, this.mockedDataService);
    }

    @Test
    public void authorizationShouldBeSuccessAndLastDataShouldBePutInContextAsAttribute() {
        final RequestLoginPackage givenPackage = new RequestLoginPackage(
                "11111222223333344444", "password");
        final ChannelHandlerContext givenContext = mock(ChannelHandlerContext.class);

        final Tracker givenTracker = Tracker.builder()
                .id(255L)
                .imei(givenPackage.getImei())
                .password(givenPackage.getPassword())
                .phoneNumber("447336934")
                .build();
        when(this.mockedTrackerService.findByImei(anyString())).thenReturn(Optional.of(givenTracker));

        final Data givenData = Data.builder()
                .id(256L)
                .build();
        when(this.mockedDataService.findTrackerLastDataByTrackerId(anyLong())).thenReturn(Optional.of(givenData));

        this.authorizationTrackerService.authorize(givenPackage, givenContext);

        verify(this.mockedContextAttributeManager, times(1))
                .putTrackerImei(this.contextArgumentCaptor.capture(), this.stringArgumentCaptor.capture());
        verify(this.mockedTrackerService, times(1))
                .findByImei(this.stringArgumentCaptor.capture());
        verify(this.mockedContextAttributeManager, times(1))
                .putTracker(this.contextArgumentCaptor.capture(), this.trackerArgumentCaptor.capture());
        verify(this.mockedConnectionManager, times(1))
                .add(this.contextArgumentCaptor.capture());
        verify(this.mockedDataService, times(1))
                .findTrackerLastDataByTrackerId(this.longArgumentCaptor.capture());
        verify(this.mockedContextAttributeManager, times(1))
                .putLastData(this.contextArgumentCaptor.capture(), this.dataArgumentCaptor.capture());
        verify(givenContext, times(1))
                .writeAndFlush(this.responseLoginPackageArgumentCaptor.capture());

        assertEquals(
                List.of(givenContext, givenContext, givenContext, givenContext),
                this.contextArgumentCaptor.getAllValues());
        assertEquals(List.of(givenPackage.getImei(), givenPackage.getImei()), this.stringArgumentCaptor.getAllValues());
        assertSame(givenTracker, this.trackerArgumentCaptor.getValue());
        assertEquals(255L, this.longArgumentCaptor.getValue().longValue());
        assertSame(givenData, this.dataArgumentCaptor.getValue());
        assertEquals(
                new ResponseLoginPackage(SUCCESS_AUTHORIZATION),
                this.responseLoginPackageArgumentCaptor.getValue()
        );
    }

    @Test
    public void authorizationShouldBeSuccessAndLastDataShouldNotBePutInContextAsAttribute() {
        final RequestLoginPackage givenPackage = new RequestLoginPackage(
                "11111222223333344444", "password");
        final ChannelHandlerContext givenContext = mock(ChannelHandlerContext.class);

        final Tracker givenTracker = Tracker.builder()
                .id(255L)
                .imei(givenPackage.getImei())
                .password(givenPackage.getPassword())
                .phoneNumber("447336934")
                .build();
        when(this.mockedTrackerService.findByImei(anyString())).thenReturn(Optional.of(givenTracker));

        when(this.mockedDataService.findTrackerLastDataByTrackerId(anyLong())).thenReturn(empty());

        this.authorizationTrackerService.authorize(givenPackage, givenContext);

        verify(this.mockedContextAttributeManager, times(1))
                .putTrackerImei(this.contextArgumentCaptor.capture(), this.stringArgumentCaptor.capture());
        verify(this.mockedTrackerService, times(1))
                .findByImei(this.stringArgumentCaptor.capture());
        verify(this.mockedContextAttributeManager, times(1))
                .putTracker(this.contextArgumentCaptor.capture(), this.trackerArgumentCaptor.capture());
        verify(this.mockedConnectionManager, times(1))
                .add(this.contextArgumentCaptor.capture());
        verify(this.mockedDataService, times(1))
                .findTrackerLastDataByTrackerId(this.longArgumentCaptor.capture());
        verify(this.mockedContextAttributeManager, times(0))
                .putLastData(any(ChannelHandlerContext.class), any(Data.class));
        verify(givenContext, times(1))
                .writeAndFlush(this.responseLoginPackageArgumentCaptor.capture());

        assertEquals(List.of(givenContext, givenContext, givenContext), this.contextArgumentCaptor.getAllValues());
        assertEquals(List.of(givenPackage.getImei(), givenPackage.getImei()), this.stringArgumentCaptor.getAllValues());
        assertSame(givenTracker, this.trackerArgumentCaptor.getValue());
        assertEquals(255L, this.longArgumentCaptor.getValue().longValue());
        assertEquals(
                new ResponseLoginPackage(SUCCESS_AUTHORIZATION),
                this.responseLoginPackageArgumentCaptor.getValue()
        );
    }

    @Test
    public void authorizationShouldBeFailureBecauseOfWrongPassword() {
        final RequestLoginPackage givenPackage = new RequestLoginPackage(
                "11111222223333344444", "password");
        final ChannelHandlerContext givenContext = mock(ChannelHandlerContext.class);

        final Tracker givenTracker = Tracker.builder()
                .id(255L)
                .imei(givenPackage.getImei())
                .password("second password")
                .phoneNumber("447336934")
                .build();
        when(this.mockedTrackerService.findByImei(anyString())).thenReturn(Optional.of(givenTracker));

        this.authorizationTrackerService.authorize(givenPackage, givenContext);

        verify(this.mockedContextAttributeManager, times(1))
                .putTrackerImei(this.contextArgumentCaptor.capture(), this.stringArgumentCaptor.capture());
        verify(this.mockedTrackerService, times(1))
                .findByImei(this.stringArgumentCaptor.capture());
        verify(this.mockedContextAttributeManager, times(0))
                .putTracker(any(ChannelHandlerContext.class), any(Tracker.class));
        verify(this.mockedConnectionManager, times(0))
                .add(any(ChannelHandlerContext.class));
        verify(this.mockedDataService, times(0))
                .findTrackerLastDataByTrackerId(anyLong());
        verify(this.mockedContextAttributeManager, times(0))
                .putLastData(any(ChannelHandlerContext.class), any(Data.class));
        verify(givenContext, times(1))
                .writeAndFlush(this.responseLoginPackageArgumentCaptor.capture());

        assertSame(givenContext, this.contextArgumentCaptor.getValue());
        assertEquals(List.of(givenPackage.getImei(), givenPackage.getImei()), this.stringArgumentCaptor.getAllValues());
        assertEquals(
                new ResponseLoginPackage(ERROR_CHECK_PASSWORD),
                this.responseLoginPackageArgumentCaptor.getValue()
        );
    }

    @Test
    public void authorizationShouldBeFailureBecauseOfGivenImeiDoesNotExist() {
        final RequestLoginPackage givenPackage = new RequestLoginPackage(
                "11111222223333344444", "password");
        final ChannelHandlerContext givenContext = mock(ChannelHandlerContext.class);

        when(this.mockedTrackerService.findByImei(anyString())).thenReturn(empty());

        this.authorizationTrackerService.authorize(givenPackage, givenContext);

        verify(this.mockedContextAttributeManager, times(1))
                .putTrackerImei(this.contextArgumentCaptor.capture(), this.stringArgumentCaptor.capture());
        verify(this.mockedTrackerService, times(1))
                .findByImei(this.stringArgumentCaptor.capture());
        verify(this.mockedContextAttributeManager, times(0))
                .putTracker(any(ChannelHandlerContext.class), any(Tracker.class));
        verify(this.mockedConnectionManager, times(0))
                .add(any(ChannelHandlerContext.class));
        verify(this.mockedDataService, times(0))
                .findTrackerLastDataByTrackerId(anyLong());
        verify(this.mockedContextAttributeManager, times(0))
                .putLastData(any(ChannelHandlerContext.class), any(Data.class));
        verify(givenContext, times(1))
                .writeAndFlush(this.responseLoginPackageArgumentCaptor.capture());

        assertSame(givenContext, this.contextArgumentCaptor.getValue());
        assertEquals(List.of(givenPackage.getImei(), givenPackage.getImei()), this.stringArgumentCaptor.getAllValues());
        assertEquals(
                new ResponseLoginPackage(CONNECTION_FAILURE),
                this.responseLoginPackageArgumentCaptor.getValue()
        );
    }
}
