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

import static java.util.Arrays.stream;
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

    @Test
    public void addingConnectionInfoByContextShouldBeThreadSafe()
            throws Exception {
        final ConnectionManager givenConnectionManager = this.createConnectionManager();

        final Long firstGivenTrackerId = 255L;
        final ChannelHandlerContext firstGivenContext = this.createContextAssociatedWithTrackerWithGivenId(
                firstGivenTrackerId
        );
        final ChannelHandlerContext secondGivenContext = this.createContextAssociatedWithTrackerWithGivenId(
                firstGivenTrackerId
        );

        final Long secondGivenTrackerId = 256L;
        final ChannelHandlerContext thirdGivenContext = this.createContextAssociatedWithTrackerWithGivenId(
                secondGivenTrackerId
        );
        final ChannelHandlerContext fourthGivenContext = this.createContextAssociatedWithTrackerWithGivenId(
                secondGivenTrackerId
        );

        final Long thirdGivenTrackerId = 257L;
        final ChannelHandlerContext fifthGivenContext = this.createContextAssociatedWithTrackerWithGivenId(
                thirdGivenTrackerId
        );
        final ChannelHandlerContext sixthGivenContext = this.createContextAssociatedWithTrackerWithGivenId(
                thirdGivenTrackerId
        );

        final Thread firstThreadAddingContextsInConnectionManager = createThreadAddingContextsInConnectionManager(
                givenConnectionManager,
                firstGivenContext,
                secondGivenContext
        );
        final Thread secondThreadAddingContextsInConnectionManager = createThreadAddingContextsInConnectionManager(
                givenConnectionManager,
                thirdGivenContext,
                fourthGivenContext
        );
        final Thread thirdThreadAddingContextsInConnectionManager = createThreadAddingContextsInConnectionManager(
                givenConnectionManager,
                fifthGivenContext,
                sixthGivenContext
        );
        startThreads(
                firstThreadAddingContextsInConnectionManager,
                secondThreadAddingContextsInConnectionManager,
                thirdThreadAddingContextsInConnectionManager
        );
        waitUntilFinish(
                firstThreadAddingContextsInConnectionManager,
                secondThreadAddingContextsInConnectionManager,
                thirdThreadAddingContextsInConnectionManager
        );

        final ChannelHandlerContext firstNotClosedContext = verifyOneContextWasClosedAndReturnNotClosed(
                firstGivenContext, secondGivenContext
        );
        final ChannelHandlerContext secondNotClosedContext = verifyOneContextWasClosedAndReturnNotClosed(
                thirdGivenContext, fourthGivenContext
        );
        final ChannelHandlerContext thirdNotClosedContext = verifyOneContextWasClosedAndReturnNotClosed(
                fifthGivenContext, sixthGivenContext
        );

        final Map<Long, ChannelHandlerContext> expectedContextsByTrackerIds = Map.of(
                firstGivenTrackerId, firstNotClosedContext,
                secondGivenTrackerId, secondNotClosedContext,
                thirdGivenTrackerId, thirdNotClosedContext
        );
        final Map<Long, ChannelHandlerContext> actualContextsByTrackersIds
                = findContextsByTrackerIds(givenConnectionManager);
        assertEquals(expectedContextsByTrackerIds, actualContextsByTrackersIds);
    }

    private static Thread createThreadAddingContextsInConnectionManager(final ConnectionManager connectionManager,
                                                                        final ChannelHandlerContext... contexts) {
        return new Thread(createTaskAddingContextsInConnectionManager(connectionManager, contexts));
    }

    private static Runnable createTaskAddingContextsInConnectionManager(final ConnectionManager connectionManager,
                                                                        final ChannelHandlerContext... contexts) {
        return () -> addContextsInConnectionManager(connectionManager, contexts);
    }

    private static void addContextsInConnectionManager(final ConnectionManager connectionManager,
                                                       final ChannelHandlerContext... contexts) {
        stream(contexts).forEach(connectionManager::add);
    }

    private static void startThreads(final Thread... threads) {
        stream(threads).forEach(Thread::start);
    }

    private static void waitUntilFinish(final Thread... threads)
            throws InterruptedException {
        for (final Thread thread : threads) {
            thread.join();
        }
    }

    private ConnectionManager createConnectionManager() {
        return new ConnectionManager(this.mockedContextAttributeManager);
    }

    private ConnectionManager createConnectionManager(final Map<Long, ChannelHandlerContext> contextsByTrackerIds)
            throws Exception {
        final ConnectionManager connectionManager = this.createConnectionManager();
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

    private ChannelHandlerContext createContextAssociatedWithTrackerWithGivenId(final Long trackerId) {
        final Tracker tracker = createTracker(trackerId);
        final ChannelHandlerContext context = mock(ChannelHandlerContext.class);
        when(this.mockedContextAttributeManager.findTracker(context))
                .thenReturn(Optional.of(tracker));
        return context;
    }

    private static Tracker createTracker(final Long id) {
        return Tracker.builder()
                .id(id)
                .build();
    }

    private static ChannelHandlerContext verifyOneContextWasClosedAndReturnNotClosed(final ChannelHandlerContext first,
                                                                                     final ChannelHandlerContext second) {
        try {
            verify(first, times(1)).close();
            return second;
        } catch (final AssertionError assertionError) {
            verify(second, times(1)).close();
            return first;
        }
    }

    @SuppressWarnings("unchecked")
    private static Map<Long, ChannelHandlerContext> findContextsByTrackerIds(final ConnectionManager connectionManager)
            throws Exception {
        final Field fieldContextsByTrackerIds = ConnectionManager.class.getDeclaredField(
                FIELD_NAME_CONTEXTS_BY_TRACKER_IDS
        );
        fieldContextsByTrackerIds.setAccessible(true);
        try {
            return (Map<Long, ChannelHandlerContext>) fieldContextsByTrackerIds.get(connectionManager);
        } finally {
            fieldContextsByTrackerIds.setAccessible(false);
        }
    }
}
