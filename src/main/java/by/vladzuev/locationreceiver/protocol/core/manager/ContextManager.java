package by.vladzuev.locationreceiver.protocol.core.manager;

import by.vladzuev.locationreceiver.crud.dto.Tracker;
import io.netty.channel.ChannelHandlerContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

import static java.util.Optional.ofNullable;

@Component
@RequiredArgsConstructor
public final class ContextManager {
    private final ContextAttributeManager attributeManager;
    private final Map<Long, ChannelHandlerContext> contextsByTrackerIds = new ConcurrentHashMap<>();

    public void add(final ChannelHandlerContext context) {
        contextsByTrackerIds.merge(
                getTrackerId(context),
                context,
                (existing, replacement) -> {
                    existing.close();
                    return replacement;
                }
        );
    }

    public Optional<ChannelHandlerContext> find(final Long trackerId) {
        return ofNullable(contextsByTrackerIds.get(trackerId));
    }

    public void remove(final Long trackerId) {
        contextsByTrackerIds.remove(trackerId);
    }

    private Long getTrackerId(final ChannelHandlerContext context) {
        return attributeManager.findTracker(context)
                .map(Tracker::getId)
                .orElseThrow(() -> new IllegalArgumentException("No tracker in context"));
    }
}
