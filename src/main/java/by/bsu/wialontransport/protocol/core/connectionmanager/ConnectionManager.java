package by.bsu.wialontransport.protocol.core.connectionmanager;

import by.bsu.wialontransport.crud.dto.Tracker;
import by.bsu.wialontransport.protocol.core.contextattributemanager.ContextAttributeManager;
import io.netty.channel.ChannelHandlerContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import static java.util.Optional.ofNullable;

@Component
@RequiredArgsConstructor
public final class ConnectionManager {
    private final ContextAttributeManager contextAttributeManager;
    private final Map<Long, ChannelHandlerContext> contextsByTrackerIds = new ConcurrentHashMap<>();

    public void add(final ChannelHandlerContext context) {
        contextsByTrackerIds.merge(getTrackerId(context), context, ConnectionManager::closeOldReturningNew);
    }

    public Optional<ChannelHandlerContext> find(final Long trackerId) {
        return ofNullable(contextsByTrackerIds.get(trackerId));
    }

    public void remove(final Long trackerId) {
        contextsByTrackerIds.remove(trackerId);
    }

    private Long getTrackerId(final ChannelHandlerContext context) {
        return contextAttributeManager.findTracker(context)
                .map(Tracker::getId)
                .orElseThrow(NoTrackerInContextException::new);
    }

    private static ChannelHandlerContext closeOldReturningNew(final ChannelHandlerContext old,
                                                              final ChannelHandlerContext fresh) {
        old.close();
        return fresh;
    }

    static final class NoTrackerInContextException extends RuntimeException {
        public NoTrackerInContextException() {

        }

        @SuppressWarnings("unused")
        public NoTrackerInContextException(final String description) {
            super(description);
        }

        @SuppressWarnings("unused")
        public NoTrackerInContextException(final Exception cause) {
            super(cause);
        }

        @SuppressWarnings("unused")
        public NoTrackerInContextException(final String description, final Exception cause) {
            super(description, cause);
        }
    }
}
