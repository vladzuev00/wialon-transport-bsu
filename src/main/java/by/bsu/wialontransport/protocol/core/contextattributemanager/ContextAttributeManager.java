package by.bsu.wialontransport.protocol.core.contextattributemanager;

import by.bsu.wialontransport.crud.dto.Data;
import by.bsu.wialontransport.crud.dto.Tracker;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.AttributeKey;
import org.springframework.stereotype.Component;

import java.util.Optional;

import static io.netty.util.AttributeKey.valueOf;
import static java.util.Optional.ofNullable;

@Component
public final class ContextAttributeManager {
    static final String NAME_ATTRIBUTE_KEY_TRACKER_IMEI = "tracker_imei";
    static final String NAME_ATTRIBUTE_KEY_TRACKER = "tracker";
    static final String NAME_ATTRIBUTE_KEY_LAST_DATA = "last_data";
    static final AttributeKey<String> ATTRIBUTE_KEY_TRACKER_IMEI = valueOf(NAME_ATTRIBUTE_KEY_TRACKER_IMEI);
    static final AttributeKey<Tracker> ATTRIBUTE_KEY_TRACKER = valueOf(NAME_ATTRIBUTE_KEY_TRACKER);
    static final AttributeKey<Data> ATTRIBUTE_KEY_LAST_DATA = valueOf(NAME_ATTRIBUTE_KEY_LAST_DATA);

    public void putTrackerImei(final ChannelHandlerContext context, final String imei) {
        putAttributeValue(context, ATTRIBUTE_KEY_TRACKER_IMEI, imei);
    }

    public Optional<String> findTrackerImei(final ChannelHandlerContext context) {
        return findAttributeValue(context, ATTRIBUTE_KEY_TRACKER_IMEI);
    }

    public void putTracker(final ChannelHandlerContext context, final Tracker tracker) {
        putAttributeValue(context, ATTRIBUTE_KEY_TRACKER, tracker);
    }

    public Optional<Tracker> findTracker(final ChannelHandlerContext context) {
        return findAttributeValue(context, ATTRIBUTE_KEY_TRACKER);
    }

    public void putLastData(final ChannelHandlerContext context, final Data data) {
        putAttributeValue(context, ATTRIBUTE_KEY_LAST_DATA, data);
    }

    public Optional<Data> findLastData(final ChannelHandlerContext context) {
        return findAttributeValue(context, ATTRIBUTE_KEY_LAST_DATA);
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
