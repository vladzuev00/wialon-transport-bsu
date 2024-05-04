package by.bsu.wialontransport.protocol.core.connectionmanager;

import by.bsu.wialontransport.crud.dto.Tracker;
import by.bsu.wialontransport.protocol.core.contextattributemanager.ContextAttributeManager;
import io.netty.channel.ChannelHandlerContext;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static by.bsu.wialontransport.util.ReflectionUtil.getProperty;
import static by.bsu.wialontransport.util.ReflectionUtil.setProperty;
import static java.lang.Long.MIN_VALUE;
import static java.util.Arrays.stream;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public final class ConnectionManagerTest {
    private static final String FIELD_NAME_CONTEXTS_BY_TRACKER_IDS = "contextsByTrackerIds";

    @Mock
    private ContextAttributeManager mockedContextAttributeManager;

    @Test
    public void contextShouldBeAddedWithoutClosingOldContext() {
        final Map<Long, ChannelHandlerContext> givenContextsByTrackerIds = new HashMap<>();
        final ConnectionManager givenManager = createManager(givenContextsByTrackerIds);
        final Long givenTrackerId = 255L;
        final ChannelHandlerContext givenContext = createContext(givenTrackerId);

        givenManager.add(givenContext);

        assertEquals(1, givenContextsByTrackerIds.size());
        assertTrue(givenContextsByTrackerIds.containsKey(givenTrackerId));
        assertTrue(givenContextsByTrackerIds.containsValue(givenContext));
        verifyNoInteractions(givenContext);
    }

    @Test
    public void contextShouldBeAddedWithClosingOldContext() {
        final Long givenTrackerId = 255L;
        final ChannelHandlerContext givenOldContext = mock(ChannelHandlerContext.class);
        final Map<Long, ChannelHandlerContext> givenContextsByTrackerIds = new HashMap<>();
        givenContextsByTrackerIds.put(givenTrackerId, givenOldContext);
        final ConnectionManager givenManager = createManager(givenContextsByTrackerIds);
        final ChannelHandlerContext givenNewContext = createContext(givenTrackerId);

        givenManager.add(givenNewContext);

        assertEquals(1, givenContextsByTrackerIds.size());
        assertTrue(givenContextsByTrackerIds.containsKey(givenTrackerId));
        assertTrue(givenContextsByTrackerIds.containsValue(givenNewContext));
        verify(givenOldContext, times(1)).close();
        verifyNoInteractions(givenNewContext);
    }

    @Test
    public void contextShouldBeFoundByTrackerId() {
        final Long givenTrackerId = 255L;
        final ChannelHandlerContext givenContext = mock(ChannelHandlerContext.class);
        final Map<Long, ChannelHandlerContext> givenContextsByTrackerIds = new HashMap<>();
        givenContextsByTrackerIds.put(givenTrackerId, givenContext);
        final ConnectionManager givenManager = createManager(givenContextsByTrackerIds);

        final Optional<ChannelHandlerContext> optionalActual = givenManager.find(givenTrackerId);

        assertTrue(optionalActual.isPresent());
        final ChannelHandlerContext actual = optionalActual.get();
        assertSame(givenContext, actual);
        verifyNoInteractions(givenContext, mockedContextAttributeManager);
    }

    @Test
    public void contextShouldNotBeFoundByTrackerId() {
        final Long givenTrackerId = 255L;
        final Map<Long, ChannelHandlerContext> givenContextsByTrackerIds = new HashMap<>();
        final ConnectionManager givenManager = createManager(givenContextsByTrackerIds);

        final Optional<ChannelHandlerContext> optionalActual = givenManager.find(givenTrackerId);

        assertTrue(optionalActual.isEmpty());
        verifyNoInteractions(mockedContextAttributeManager);
    }

    @Test
    public void contextShouldBeRemovedByTrackerId() {
        final Map<Long, ChannelHandlerContext> givenContextsByTrackerIds = new HashMap<>();
        final Long givenTrackerId = 255L;
        final ChannelHandlerContext givenContext = mock(ChannelHandlerContext.class);
        givenContextsByTrackerIds.put(givenTrackerId, givenContext);
        final ConnectionManager givenManager = createManager(givenContextsByTrackerIds);

        givenManager.remove(givenTrackerId);

        assertTrue(givenContextsByTrackerIds.isEmpty());
        verifyNoInteractions(givenContext, mockedContextAttributeManager);
    }

    @Test
    public void contextShouldNotBeRemovedByNotExistingTrackerId() {
        final Map<Long, ChannelHandlerContext> givenContextsByTrackerIds = new HashMap<>();
        final Long givenExistingTrackerId = 255L;
        final ChannelHandlerContext givenContext = mock(ChannelHandlerContext.class);
        givenContextsByTrackerIds.put(givenExistingTrackerId, givenContext);
        final ConnectionManager givenManager = createManager(givenContextsByTrackerIds);

        givenManager.remove(MIN_VALUE);

        assertEquals(1, givenContextsByTrackerIds.size());
        assertTrue(givenContextsByTrackerIds.containsKey(givenExistingTrackerId));
        assertTrue(givenContextsByTrackerIds.containsValue(givenContext));
        verifyNoInteractions(givenContext, mockedContextAttributeManager);
    }

    @Test
    public void addingConnectionInfoByContextShouldBeThreadSafe()
            throws Exception {
        final ConnectionManager givenManager = createManager();
        final Long firstGivenTrackerId = 255L;
        final Long secondGivenTrackerId = 256L;
        final Long thirdGivenTrackerId = 257L;
        final ChannelHandlerContext firstGivenContext = createContext(firstGivenTrackerId);
        final ChannelHandlerContext secondGivenContext = createContext(firstGivenTrackerId);
        final ChannelHandlerContext thirdGivenContext = createContext(secondGivenTrackerId);
        final ChannelHandlerContext fourthGivenContext = createContext(secondGivenTrackerId);
        final ChannelHandlerContext fifthGivenContext = createContext(thirdGivenTrackerId);
        final ChannelHandlerContext sixthGivenContext = createContext(thirdGivenTrackerId);

        final Thread firstThread = addContextsAsync(givenManager, firstGivenContext, secondGivenContext);
        final Thread secondThread = addContextsAsync(givenManager, thirdGivenContext, fourthGivenContext);
        final Thread thirdThread = addContextsAsync(givenManager, fifthGivenContext, sixthGivenContext);

        waitUntilFinish(firstThread, secondThread, thirdThread);
        final var firstNotClosedContext = verifyOneClosedReturningNotClosed(firstGivenContext, secondGivenContext);
        final var secondNotClosedContext = verifyOneClosedReturningNotClosed(thirdGivenContext, fourthGivenContext);
        final var thirdNotClosedContext = verifyOneClosedReturningNotClosed(fifthGivenContext, sixthGivenContext);
        final Map<Long, ChannelHandlerContext> expectedContextsByTrackerIds = Map.of(
                firstGivenTrackerId, firstNotClosedContext,
                secondGivenTrackerId, secondNotClosedContext,
                thirdGivenTrackerId, thirdNotClosedContext
        );
        final Map<Long, ChannelHandlerContext> actualContextsByTrackersIds = getContextsByTrackerIds(givenManager);
        assertEquals(expectedContextsByTrackerIds, actualContextsByTrackersIds);
    }

    @Test
    public void findingContextShouldBeThreadSafe()
            throws Exception {
        final ConnectionManager givenManager = createManager();
        final Long givenTrackerId = 255L;
        final ChannelHandlerContext givenContext = createContext(givenTrackerId);
        final Thread addingThread = addContextsAsync(givenManager, givenContext);
        waitUntilFinish(addingThread);

        final Optional<ChannelHandlerContext> optionalActual = givenManager.find(givenTrackerId);

        assertTrue(optionalActual.isPresent());
        final ChannelHandlerContext actual = optionalActual.get();
        assertSame(givenContext, actual);
        verifyNoInteractions(givenContext);
    }

    @Test
    public void removingContextShouldBeThreadSafe()
            throws Exception {
        final ConnectionManager givenManager = createManager();
        final Long givenTrackerId = 255L;
        final ChannelHandlerContext givenContext = createContext(givenTrackerId);
        givenManager.add(givenContext);

        final Thread removingThread = removeContextAsync(givenManager, givenTrackerId);

        waitUntilFinish(removingThread);
        final Map<Long, ChannelHandlerContext> actual = getContextsByTrackerIds(givenManager);
        assertTrue(actual.isEmpty());
        verifyNoInteractions(givenContext);
    }

    private static Tracker createTracker(final Long id) {
        return Tracker.builder()
                .id(id)
                .build();
    }

    private ConnectionManager createManager() {
        return new ConnectionManager(mockedContextAttributeManager);
    }

    private ConnectionManager createManager(final Map<Long, ChannelHandlerContext> contextsByTrackerIds) {
        final ConnectionManager connectionManager = createManager();
        setProperty(connectionManager, FIELD_NAME_CONTEXTS_BY_TRACKER_IDS, contextsByTrackerIds);
        return connectionManager;
    }

    private ChannelHandlerContext createContext(final Long trackerId) {
        final ChannelHandlerContext context = mock(ChannelHandlerContext.class);
        final Tracker tracker = createTracker(trackerId);
        when(mockedContextAttributeManager.findTracker(same(context))).thenReturn(Optional.of(tracker));
        return context;
    }

    private static Thread addContextsAsync(final ConnectionManager manager, final ChannelHandlerContext... contexts) {
        return executeAsync(() -> stream(contexts).forEach(manager::add));
    }

    private static Thread removeContextAsync(final ConnectionManager manager, final Long trackerId) {
        return executeAsync(() -> manager.remove(trackerId));
    }

    private static Thread executeAsync(final Runnable task) {
        final Thread thread = new Thread(task);
        thread.start();
        return thread;
    }

    private static void waitUntilFinish(final Thread... threads)
            throws InterruptedException {
        for (final Thread thread : threads) {
            thread.join();
        }
    }

    private static ChannelHandlerContext verifyOneClosedReturningNotClosed(final ChannelHandlerContext first,
                                                                           final ChannelHandlerContext second) {
        try {
            verify(first, times(1)).close();
            verify(second, times(0)).close();
            return second;
        } catch (final AssertionError assertionError) {
            verify(first, times(0)).close();
            verify(second, times(1)).close();
            return first;
        }
    }

    @SuppressWarnings("unchecked")
    private static Map<Long, ChannelHandlerContext> getContextsByTrackerIds(final ConnectionManager manager) {
        return getProperty(manager, FIELD_NAME_CONTEXTS_BY_TRACKER_IDS, Map.class);
    }
}
