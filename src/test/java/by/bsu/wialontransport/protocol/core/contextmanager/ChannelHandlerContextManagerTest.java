package by.bsu.wialontransport.protocol.core.contextmanager;

import by.bsu.wialontransport.crud.dto.Tracker;
import by.bsu.wialontransport.protocol.core.contextattributemanager.ContextAttributeManager;
import io.netty.channel.ChannelHandlerContext;
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
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public final class ChannelHandlerContextManagerTest {
    private static final String FIELD_NAME_CONTEXTS_BY_TRACKER_IDS = "contextsByTrackerIds";

    @Mock
    private ContextAttributeManager mockedAttributeManager;

    @Test
    public void contextShouldBeAddedWithoutClosingOldContext() {
        final ChannelHandlerContextManager givenManager = createManager();
        final Long givenTrackerId = 255L;
        final ChannelHandlerContext givenContext = createContext(givenTrackerId);

        givenManager.add(givenContext);

        final Map<Long, ChannelHandlerContext> actual = getContextsByTrackerIds(givenManager);
        final Map<Long, ChannelHandlerContext> expected = Map.of(givenTrackerId, givenContext);
        assertEquals(expected, actual);

        verifyNoInteractions(givenContext);
    }

    @Test
    public void contextShouldBeAddedWithClosingOldContext() {
        final Long givenTrackerId = 255L;
        final ChannelHandlerContext givenOldContext = mock(ChannelHandlerContext.class);
        final var givenContextsByTrackerIds = new HashMap<>(Map.of(givenTrackerId, givenOldContext));
        final ChannelHandlerContextManager givenManager = createManager(givenContextsByTrackerIds);
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
        final ChannelHandlerContextManager givenManager = createManager(givenContextsByTrackerIds);

        final Optional<ChannelHandlerContext> optionalActual = givenManager.find(givenTrackerId);
        assertTrue(optionalActual.isPresent());
        final ChannelHandlerContext actual = optionalActual.get();
        assertSame(givenContext, actual);

        verifyNoInteractions(givenContext, mockedAttributeManager);
    }

    @Test
    public void contextShouldNotBeFoundByTrackerId() {
        final Long givenTrackerId = 255L;
        final Map<Long, ChannelHandlerContext> givenContextsByTrackerIds = new HashMap<>();
        final ChannelHandlerContextManager givenManager = createManager(givenContextsByTrackerIds);

        final Optional<ChannelHandlerContext> optionalActual = givenManager.find(givenTrackerId);
        assertTrue(optionalActual.isEmpty());

        verifyNoInteractions(mockedAttributeManager);
    }

    @Test
    public void contextShouldBeRemovedByTrackerId() {
        final Long givenTrackerId = 255L;
        final ChannelHandlerContext givenContext = mock(ChannelHandlerContext.class);
        final var givenContextsByTrackerIds = new HashMap<>(Map.of(givenTrackerId, givenContext));
        final ChannelHandlerContextManager givenManager = createManager(givenContextsByTrackerIds);

        givenManager.remove(givenTrackerId);

        assertTrue(givenContextsByTrackerIds.isEmpty());

        verifyNoInteractions(givenContext, mockedAttributeManager);
    }

    @Test
    public void contextShouldNotBeRemovedByNotExistingTrackerId() {
        final Long givenExistingTrackerId = 255L;
        final ChannelHandlerContext givenContext = mock(ChannelHandlerContext.class);
        final var givenContextsByTrackerIds = new HashMap<>(Map.of(givenExistingTrackerId, givenContext));
        final ChannelHandlerContextManager givenManager = createManager(givenContextsByTrackerIds);

        givenManager.remove(MIN_VALUE);

        assertEquals(1, givenContextsByTrackerIds.size());
        assertTrue(givenContextsByTrackerIds.containsKey(givenExistingTrackerId));
        assertTrue(givenContextsByTrackerIds.containsValue(givenContext));

        verifyNoInteractions(givenContext, mockedAttributeManager);
    }

    private ChannelHandlerContextManager createManager() {
        return new ChannelHandlerContextManager(mockedAttributeManager);
    }

    private ChannelHandlerContextManager createManager(final Map<Long, ChannelHandlerContext> contextsByTrackerIds) {
        final ChannelHandlerContextManager channelHandlerContextManager = createManager();
        setProperty(channelHandlerContextManager, FIELD_NAME_CONTEXTS_BY_TRACKER_IDS, contextsByTrackerIds);
        return channelHandlerContextManager;
    }

    private ChannelHandlerContext createContext(final Long trackerId) {
        final ChannelHandlerContext context = mock(ChannelHandlerContext.class);
        final Tracker tracker = Tracker.builder().id(trackerId).build();
        when(mockedAttributeManager.findTracker(same(context))).thenReturn(Optional.of(tracker));
        return context;
    }

    @SuppressWarnings("unchecked")
    private Map<Long, ChannelHandlerContext> getContextsByTrackerIds(final ChannelHandlerContextManager manager) {
        return getProperty(manager, FIELD_NAME_CONTEXTS_BY_TRACKER_IDS, Map.class);
    }
}
