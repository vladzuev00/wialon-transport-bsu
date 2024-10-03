package by.bsu.wialontransport.protocol.core.connectionmanager;

import by.bsu.wialontransport.crud.dto.Tracker;
import by.bsu.wialontransport.protocol.core.contextattributemanager.ContextAttributeManager;
import io.netty.channel.ChannelHandlerContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static by.bsu.wialontransport.util.ReflectionUtil.getProperty;
import static by.bsu.wialontransport.util.ReflectionUtil.setProperty;
import static java.lang.Long.MIN_VALUE;
import static java.util.Arrays.stream;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public final class ConnectionManagerTest {
    private static final String FIELD_NAME_CONTEXTS_BY_TRACKER_IDS = "contextsByTrackerIds";

    @Mock
    private ContextAttributeManager mockedContextAttributeManager;

    private ConnectionManager connectionManager;

    @BeforeEach
    public void initializeConnectionManager() {
        connectionManager = new ConnectionManager(mockedContextAttributeManager);
    }

    @Test
    public void contextShouldBeAddedWithoutClosingOldContext() {
        final ChannelHandlerContext givenContext = mock(ChannelHandlerContext.class);

        final Long givenTrackerId = 255L;
        mockTrackerFor(givenContext, givenTrackerId);

        connectionManager.add(givenContext);

        final Map<Long, ChannelHandlerContext> actual = getContextsByTrackerIds();
        final Map<Long, ChannelHandlerContext> expected = Map.of(givenTrackerId, givenContext);
        assertEquals(expected, actual);

        verifyNoInteractions(givenContext);
    }

    @Test
    public void contextShouldBeAddedWithClosingOldContext() {
        final Long givenTrackerId = 255L;
        final ChannelHandlerContext givenOldContext = mock(ChannelHandlerContext.class);
        final var givenContextsByTrackerIds = new HashMap<>(Map.of(givenTrackerId, givenOldContext));
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
        final var givenContextsByTrackerIds = new HashMap<>(Map.of(givenTrackerId, givenContext));
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
        final Long givenTrackerId = 255L;
        final ChannelHandlerContext givenContext = mock(ChannelHandlerContext.class);
        final var givenContextsByTrackerIds = new HashMap<>(Map.of(givenTrackerId, givenContext));
        final ConnectionManager givenManager = createManager(givenContextsByTrackerIds);

        givenManager.remove(givenTrackerId);

        assertTrue(givenContextsByTrackerIds.isEmpty());

        verifyNoInteractions(givenContext, mockedContextAttributeManager);
    }

    @Test
    public void contextShouldNotBeRemovedByNotExistingTrackerId() {
        final Long givenExistingTrackerId = 255L;
        final ChannelHandlerContext givenContext = mock(ChannelHandlerContext.class);
        final var givenContextsByTrackerIds = new HashMap<>(Map.of(givenExistingTrackerId, givenContext));
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
        final Map<Long, ChannelHandlerContext> actualContextsByTrackersIds = getContextsByTrackerIds();
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
        final Map<Long, ChannelHandlerContext> actual = getContextsByTrackerIds();
        assertTrue(actual.isEmpty());

        verifyNoInteractions(givenContext);
    }

    private Tracker mockTrackerFor(final ChannelHandlerContext context, final Long trackerId) {
        final Tracker tracker = Tracker.builder().id(trackerId).build();
        when(mockedContextAttributeManager.findTracker(same(context))).thenReturn(Optional.of(tracker));
        return tracker;
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
        final Tracker tracker = Tracker.builder().id(trackerId).build();
        when(mockedContextAttributeManager.findTracker(same(context))).thenReturn(Optional.of(tracker));
        return context;
    }

    private Thread addContextsAsync(final ConnectionManager manager, final ChannelHandlerContext... contexts) {
        return executeAsync(() -> stream(contexts).forEach(manager::add));
    }

    private Thread removeContextAsync(final ConnectionManager manager, final Long trackerId) {
        return executeAsync(() -> manager.remove(trackerId));
    }

    private Thread executeAsync(final Runnable task) {
        final Thread thread = new Thread(task);
        thread.start();
        return thread;
    }

    private void waitUntilFinish(final Thread... threads)
            throws InterruptedException {
        for (final Thread thread : threads) {
            thread.join();
        }
    }

    private ChannelHandlerContext verifyOneClosedReturningNotClosed(final ChannelHandlerContext first,
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
    private Map<Long, ChannelHandlerContext> getContextsByTrackerIds() {
        return getProperty(connectionManager, FIELD_NAME_CONTEXTS_BY_TRACKER_IDS, Map.class);
    }
}
