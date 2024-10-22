package by.bsu.wialontransport.protocol.core.contextattributemanager;

import by.bsu.wialontransport.crud.dto.Location;
import by.bsu.wialontransport.crud.dto.Tracker;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.AttributeKey;
import org.springframework.stereotype.Component;

import java.util.Optional;

import static io.netty.util.AttributeKey.valueOf;
import static java.util.Optional.ofNullable;

@Component
public final class ContextAttributeManager {
    static final String NAME_KEY_TRACKER_IMEI = "tracker_imei";
    static final String NAME_KEY_TRACKER = "tracker";
    static final String NAME_KEY_LAST_LOCATION = "last_location";
    static final AttributeKey<String> KEY_TRACKER_IMEI = valueOf(NAME_KEY_TRACKER_IMEI);
    static final AttributeKey<Tracker> KEY_TRACKER = valueOf(NAME_KEY_TRACKER);
    static final AttributeKey<Location> KEY_LAST_LOCATION = valueOf(NAME_KEY_LAST_LOCATION);

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

    private <V> Optional<V> findAttributeValue(final ChannelHandlerContext context, final AttributeKey<V> key) {
        final V value = context.channel()
                .attr(key)
                .get();
        return ofNullable(value);
    }

    private <V> void putAttributeValue(final ChannelHandlerContext context, final AttributeKey<V> key, final V value) {
        context.channel()
                .attr(key)
                .set(value);
    }
}
