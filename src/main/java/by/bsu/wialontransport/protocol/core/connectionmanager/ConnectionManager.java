package by.bsu.wialontransport.protocol.core.connectionmanager;

import by.bsu.wialontransport.crud.dto.Tracker;
import by.bsu.wialontransport.protocol.core.contextattributemanager.ContextAttributeManager;
import by.bsu.wialontransport.protocol.core.connectionmanager.exception.TrackerAssociatedWithContextNotExist;
import io.netty.channel.ChannelHandlerContext;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import static java.util.Optional.ofNullable;

@Component
public final class ConnectionManager {
    private final ContextAttributeManager contextAttributeManager;
    private final Map<Long, ChannelHandlerContext> contextsByTrackerIds;

    public ConnectionManager(final ContextAttributeManager contextAttributeManager) {
        this.contextAttributeManager = contextAttributeManager;
        this.contextsByTrackerIds = new ConcurrentHashMap<>();
    }

    public void add(final ChannelHandlerContext context) {
        final Optional<Tracker> optionalAssociatedTracker = this.contextAttributeManager.findTracker(context);
        final Tracker associatedTracker = optionalAssociatedTracker.orElseThrow(
                TrackerAssociatedWithContextNotExist::new);
        this.contextsByTrackerIds.merge(associatedTracker.getId(), context, (oldContext, newContext) -> {
            oldContext.close();
            return newContext;
        });
    }

    public Optional<ChannelHandlerContext> find(final Long deviceId) {
        return ofNullable(this.contextsByTrackerIds.get(deviceId));
    }

    public void remove(final Long deviceId) {
        this.contextsByTrackerIds.remove(deviceId);
    }
}
