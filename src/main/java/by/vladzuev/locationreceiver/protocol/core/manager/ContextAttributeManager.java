package by.vladzuev.locationreceiver.protocol.core.manager;

import by.vladzuev.locationreceiver.crud.dto.Location;
import by.vladzuev.locationreceiver.crud.dto.Tracker;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.Attribute;
import io.netty.util.AttributeKey;
import org.springframework.stereotype.Component;

import java.util.Optional;

import static io.netty.util.AttributeKey.valueOf;

@Component
public final class ContextAttributeManager {
    static final AttributeKey<String> KEY_TRACKER_IMEI = valueOf("tracker_imei");
    static final AttributeKey<Tracker> KEY_TRACKER = valueOf("tracker");
    static final AttributeKey<Location> KEY_LAST_LOCATION = valueOf("last_location");

    public void putTrackerImei(final ChannelHandlerContext context, final String imei) {
        putAttributeValue(context, KEY_TRACKER_IMEI, imei);
    }

    public void putTracker(final ChannelHandlerContext context, final Tracker tracker) {
        putAttributeValue(context, KEY_TRACKER, tracker);
    }

    public void putLastLocation(final ChannelHandlerContext context, final Location location) {
        putAttributeValue(context, KEY_LAST_LOCATION, location);
    }

    public Optional<String> findTrackerImei(final ChannelHandlerContext context) {
        return findAttributeValue(context, KEY_TRACKER_IMEI);
    }

    public Optional<Tracker> findTracker(final ChannelHandlerContext context) {
        return findAttributeValue(context, KEY_TRACKER);
    }

    public Optional<Location> findLastLocation(final ChannelHandlerContext context) {
        return findAttributeValue(context, KEY_LAST_LOCATION);
    }

    private <V> void putAttributeValue(final ChannelHandlerContext context, final AttributeKey<V> key, final V value) {
        context.channel()
                .attr(key)
                .set(value);
    }

    private <V> Optional<V> findAttributeValue(final ChannelHandlerContext context, final AttributeKey<V> key) {
        return Optional.of(context)
                .map(ChannelHandlerContext::channel)
                .map(channel -> channel.attr(key))
                .map(Attribute::get);
    }
}
