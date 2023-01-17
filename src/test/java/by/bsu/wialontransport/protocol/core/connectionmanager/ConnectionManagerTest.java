package by.bsu.wialontransport.protocol.core.connectionmanager;

import by.bsu.wialontransport.crud.dto.Tracker;
import by.bsu.wialontransport.protocol.core.contextattributemanager.ContextAttributeManager;
import io.netty.channel.ChannelHandlerContext;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public final class ConnectionManagerTest {
    private static final String FIELD_NAME_CONTEXTS_BY_TRACKER_IDS = "contextsByTrackerIds";

    @Mock
    private ContextAttributeManager mockedContextAttributeManager;

    @Captor
    private ArgumentCaptor<ChannelHandlerContext> contextArgumentCaptor;

    @Test
    public void contextShouldBeAddedWithoutClosingOldContext()
            throws Exception {
        final Map<Long, ChannelHandlerContext> givenContextsByTrackerIds = new HashMap<>();
        final ConnectionManager givenConnectionManager = this.createConnectionManager(givenContextsByTrackerIds);

        final Tracker givenTracker = Tracker.builder()
                .id(255L)
                .imei("11111222223333344444")
                .password("password")
                .phoneNumber("447336934")
                .build();
        when(this.mockedContextAttributeManager.findTracker(any(ChannelHandlerContext.class)))
                .thenReturn(Optional.of(givenTracker));

        final ChannelHandlerContext givenContext = mock(ChannelHandlerContext.class);

        givenConnectionManager.add(givenContext);

        assertEquals(1, givenContextsByTrackerIds.size());
        assertTrue(givenContextsByTrackerIds.containsKey(255L));
        assertTrue(givenContextsByTrackerIds.containsValue(givenContext));

        verify(this.mockedContextAttributeManager, times(1))
                .findTracker(this.contextArgumentCaptor.capture());

        assertSame(givenContext, this.contextArgumentCaptor.getValue());
    }

    @Test
    public void contextShouldBeAddedWithClosingOldContext()
            throws Exception {
        final Tracker givenTracker = Tracker.builder()
                .id(255L)
                .imei("11111222223333344444")
                .password("password")
                .phoneNumber("447336934")
                .build();
        final ChannelHandlerContext givenOldContext = mock(ChannelHandlerContext.class);

        final Map<Long, ChannelHandlerContext> givenContextsByTrackerIds = new HashMap<>();
        givenContextsByTrackerIds.put(givenTracker.getId(), givenOldContext);

        final ConnectionManager givenConnectionManager = this.createConnectionManager(givenContextsByTrackerIds);

        when(this.mockedContextAttributeManager.findTracker(any(ChannelHandlerContext.class)))
                .thenReturn(Optional.of(givenTracker));

        final ChannelHandlerContext givenContext = mock(ChannelHandlerContext.class);
        givenConnectionManager.add(givenContext);

        assertEquals(1, givenContextsByTrackerIds.size());
        assertTrue(givenContextsByTrackerIds.containsKey(255L));
        assertTrue(givenContextsByTrackerIds.containsValue(givenContext));

        verify(this.mockedContextAttributeManager, times(1))
                .findTracker(this.contextArgumentCaptor.capture());
        verify(givenOldContext, times(1)).close();

        assertSame(givenContext, this.contextArgumentCaptor.getValue());
    }

    @Test
    public void contextShouldBeFoundByTrackerId()
            throws Exception {
        final Long givenTrackerId = 255L;
        final ChannelHandlerContext givenContext = mock(ChannelHandlerContext.class);

        final Map<Long, ChannelHandlerContext> givenContextsByTrackerIds = new HashMap<>();
        givenContextsByTrackerIds.put(givenTrackerId, givenContext);

        final ConnectionManager givenConnectionManager = this.createConnectionManager(givenContextsByTrackerIds);

        final ChannelHandlerContext actual = givenConnectionManager.find(givenTrackerId).orElseThrow();
        assertSame(givenContext, actual);
    }

    @Test
    public void contextShouldNotBeFoundByTrackerId()
            throws Exception {
        final Long givenTrackerId = 255L;
        final Map<Long, ChannelHandlerContext> givenContextsByTrackerIds = new HashMap<>();
        final ConnectionManager givenConnectionManager = this.createConnectionManager(givenContextsByTrackerIds);
        final Optional<ChannelHandlerContext> optionalActual = givenConnectionManager.find(givenTrackerId);
        assertTrue(optionalActual.isEmpty());
    }

    @Test
    public void contextShouldBeRemovedByTrackerId()
            throws Exception {
        final Map<Long, ChannelHandlerContext> givenContextsByTrackerIds = new HashMap<>();

        final Long givenTrackerId = 255L;
        final ChannelHandlerContext givenContext = mock(ChannelHandlerContext.class);
        givenContextsByTrackerIds.put(givenTrackerId, givenContext);

        final ConnectionManager givenConnectionManager = this.createConnectionManager(givenContextsByTrackerIds);

        givenConnectionManager.remove(givenTrackerId);

        assertTrue(givenContextsByTrackerIds.isEmpty());
    }

    @Test
    public void contextShouldNotBeRemovedByNotExistingTrackerId()
            throws Exception {
        final Map<Long, ChannelHandlerContext> givenContextsByTrackerIds = new HashMap<>();

        final Long givenExistingTrackerId = 255L;
        final ChannelHandlerContext givenContext = mock(ChannelHandlerContext.class);
        givenContextsByTrackerIds.put(givenExistingTrackerId, givenContext);

        final Long givenNotExistingTrackerId = 256L;

        final ConnectionManager givenConnectionManager = this.createConnectionManager(givenContextsByTrackerIds);

        givenConnectionManager.remove(givenNotExistingTrackerId);

        assertEquals(1, givenContextsByTrackerIds.size());
        assertTrue(givenContextsByTrackerIds.containsKey(255L));
        assertTrue(givenContextsByTrackerIds.containsValue(givenContext));
    }

    private ConnectionManager createConnectionManager(final Map<Long, ChannelHandlerContext> contextsByTrackerIds)
            throws Exception {
        final ConnectionManager connectionManager = new ConnectionManager(this.mockedContextAttributeManager);
        final Field fieldContextsByTrackerIds = ConnectionManager.class.getDeclaredField(
                FIELD_NAME_CONTEXTS_BY_TRACKER_IDS);
        fieldContextsByTrackerIds.setAccessible(true);
        try {
            fieldContextsByTrackerIds.set(connectionManager, contextsByTrackerIds);
            return connectionManager;
        } finally {
            fieldContextsByTrackerIds.setAccessible(false);
        }
    }
}
