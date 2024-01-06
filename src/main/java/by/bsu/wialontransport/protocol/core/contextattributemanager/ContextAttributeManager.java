package by.bsu.wialontransport.protocol.core.contextattributemanager;

import by.bsu.wialontransport.crud.dto.Data;
import by.bsu.wialontransport.crud.dto.Tracker;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.Attribute;
import io.netty.util.AttributeKey;
import org.springframework.stereotype.Component;

import java.util.Optional;

import static io.netty.util.AttributeKey.valueOf;
import static java.util.Optional.ofNullable;

@Component
public final class ContextAttributeManager {
    private static final String NAME_ATTRIBUTE_KEY_TRACKER_IMEI = "tracker_imei";
    private static final String NAME_ATTRIBUTE_KEY_TRACKER = "tracker";
    private static final String NAME_ATTRIBUTE_KEY_LAST_DATA = "last_data";

    private final AttributeKey<String> attributeKeyTrackerImei;
    private final AttributeKey<Tracker> attributeKeyTracker;
    private final AttributeKey<Data> attributeKeyLastData;

    public ContextAttributeManager() {
        attributeKeyTrackerImei = valueOf(NAME_ATTRIBUTE_KEY_TRACKER_IMEI);
        attributeKeyTracker = valueOf(NAME_ATTRIBUTE_KEY_TRACKER);
        attributeKeyLastData = valueOf(NAME_ATTRIBUTE_KEY_LAST_DATA);
    }

    public void putTrackerImei(final ChannelHandlerContext context, final String imei) {
        putAttributeValue(context, attributeKeyTrackerImei, imei);
    }

    public Optional<String> findTrackerImei(final ChannelHandlerContext context) {
        return findAttributeValue(context, attributeKeyTrackerImei);
    }

    public void putTracker(final ChannelHandlerContext context, final Tracker tracker) {
        putAttributeValue(context, attributeKeyTracker, tracker);
    }

    public Optional<Tracker> findTracker(final ChannelHandlerContext context) {
        return findAttributeValue(context, attributeKeyTracker);
    }

    public void putLastData(final ChannelHandlerContext context, final Data data) {
        putAttributeValue(context, attributeKeyLastData, data);
    }

    public Optional<Data> findLastData(final ChannelHandlerContext context) {
        return findAttributeValue(context, attributeKeyLastData);
    }

    private static <V> Optional<V> findAttributeValue(final ChannelHandlerContext context, final AttributeKey<V> key) {
        final V attributeValue = context.channel()
                .attr(key)
                .get();
        return ofNullable(attributeValue);
    }

    private static <V> void putAttributeValue(final ChannelHandlerContext context,
                                              final AttributeKey<V> key,
                                              final V value) {
        final Attribute<V> attribute = context.channel().attr(key);
        attribute.set(value);
    }
}
