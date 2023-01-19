package by.bsu.wialontransport.protocol.core.service;

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

import static by.bsu.wialontransport.crud.dto.Data.dataBuilder;
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

        final Data givenData = dataBuilder()
                .id(256L)
                .build();
        when(this.mockedDataService.findTrackerLastData(anyLong())).thenReturn(Optional.of(givenData));

        final ResponseLoginPackage actual = this.authorizationTrackerService.authorize(givenPackage, givenContext);
        final ResponseLoginPackage expected = new ResponseLoginPackage(SUCCESS_AUTHORIZATION);
        assertEquals(expected, actual);

        verify(this.mockedContextAttributeManager, times(1))
                .putTrackerImei(this.contextArgumentCaptor.capture(), this.stringArgumentCaptor.capture());
        verify(this.mockedTrackerService, times(1))
                .findByImei(this.stringArgumentCaptor.capture());
        verify(this.mockedContextAttributeManager, times(1))
                .putTracker(this.contextArgumentCaptor.capture(), this.trackerArgumentCaptor.capture());
        verify(this.mockedConnectionManager, times(1))
                .add(this.contextArgumentCaptor.capture());
        verify(this.mockedDataService, times(1))
                .findTrackerLastData(this.longArgumentCaptor.capture());
        verify(this.mockedContextAttributeManager, times(1))
                .putLastData(this.contextArgumentCaptor.capture(), this.dataArgumentCaptor.capture());

        assertEquals(
                List.of(givenContext, givenContext, givenContext, givenContext),
                this.contextArgumentCaptor.getAllValues());
        assertEquals(List.of(givenPackage.getImei(), givenPackage.getImei()), this.stringArgumentCaptor.getAllValues());
        assertSame(givenTracker, this.trackerArgumentCaptor.getValue());
        assertEquals(255L, this.longArgumentCaptor.getValue().longValue());
        assertSame(givenData, this.dataArgumentCaptor.getValue());
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

        when(this.mockedDataService.findTrackerLastData(anyLong())).thenReturn(empty());

        final ResponseLoginPackage actual = this.authorizationTrackerService.authorize(givenPackage, givenContext);
        final ResponseLoginPackage expected = new ResponseLoginPackage(SUCCESS_AUTHORIZATION);
        assertEquals(expected, actual);

        verify(this.mockedContextAttributeManager, times(1))
                .putTrackerImei(this.contextArgumentCaptor.capture(), this.stringArgumentCaptor.capture());
        verify(this.mockedTrackerService, times(1))
                .findByImei(this.stringArgumentCaptor.capture());
        verify(this.mockedContextAttributeManager, times(1))
                .putTracker(this.contextArgumentCaptor.capture(), this.trackerArgumentCaptor.capture());
        verify(this.mockedConnectionManager, times(1))
                .add(this.contextArgumentCaptor.capture());
        verify(this.mockedDataService, times(1))
                .findTrackerLastData(this.longArgumentCaptor.capture());
        verify(this.mockedContextAttributeManager, times(0))
                .putLastData(any(ChannelHandlerContext.class), any(Data.class));

        assertEquals(List.of(givenContext, givenContext, givenContext), this.contextArgumentCaptor.getAllValues());
        assertEquals(List.of(givenPackage.getImei(), givenPackage.getImei()), this.stringArgumentCaptor.getAllValues());
        assertSame(givenTracker, this.trackerArgumentCaptor.getValue());
        assertEquals(255L, this.longArgumentCaptor.getValue().longValue());
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

        final ResponseLoginPackage actual = this.authorizationTrackerService.authorize(givenPackage, givenContext);
        final ResponseLoginPackage expected = new ResponseLoginPackage(ERROR_CHECK_PASSWORD);
        assertEquals(expected, actual);

        verify(this.mockedContextAttributeManager, times(1))
                .putTrackerImei(this.contextArgumentCaptor.capture(), this.stringArgumentCaptor.capture());
        verify(this.mockedTrackerService, times(1))
                .findByImei(this.stringArgumentCaptor.capture());
        verify(this.mockedContextAttributeManager, times(0))
                .putTracker(any(ChannelHandlerContext.class), any(Tracker.class));
        verify(this.mockedConnectionManager, times(0))
                .add(any(ChannelHandlerContext.class));
        verify(this.mockedDataService, times(0))
                .findTrackerLastData(anyLong());
        verify(this.mockedContextAttributeManager, times(0))
                .putLastData(any(ChannelHandlerContext.class), any(Data.class));

        assertSame(givenContext, this.contextArgumentCaptor.getValue());
        assertEquals(List.of(givenPackage.getImei(), givenPackage.getImei()), this.stringArgumentCaptor.getAllValues());
    }

    @Test
    public void authorizationShouldBeFailureBecauseOfGivenImeiDoesNotExist() {
        final RequestLoginPackage givenPackage = new RequestLoginPackage(
                "11111222223333344444", "password");
        final ChannelHandlerContext givenContext = mock(ChannelHandlerContext.class);

        when(this.mockedTrackerService.findByImei(anyString())).thenReturn(empty());

        final ResponseLoginPackage actual = this.authorizationTrackerService.authorize(givenPackage, givenContext);
        final ResponseLoginPackage expected = new ResponseLoginPackage(CONNECTION_FAILURE);
        assertEquals(expected, actual);

        verify(this.mockedContextAttributeManager, times(1))
                .putTrackerImei(this.contextArgumentCaptor.capture(), this.stringArgumentCaptor.capture());
        verify(this.mockedTrackerService, times(1))
                .findByImei(this.stringArgumentCaptor.capture());
        verify(this.mockedContextAttributeManager, times(0))
                .putTracker(any(ChannelHandlerContext.class), any(Tracker.class));
        verify(this.mockedConnectionManager, times(0))
                .add(any(ChannelHandlerContext.class));
        verify(this.mockedDataService, times(0))
                .findTrackerLastData(anyLong());
        verify(this.mockedContextAttributeManager, times(0))
                .putLastData(any(ChannelHandlerContext.class), any(Data.class));

        assertSame(givenContext, this.contextArgumentCaptor.getValue());
        assertEquals(List.of(givenPackage.getImei(), givenPackage.getImei()), this.stringArgumentCaptor.getAllValues());
    }
}
