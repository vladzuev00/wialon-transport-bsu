package by.bsu.wialontransport.protocol.core.contextattributemanager;

import by.bsu.wialontransport.crud.dto.ExtendedData;
import by.bsu.wialontransport.crud.dto.Tracker;
import io.netty.channel.Channel;
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
    private static final String NAME_ATTRIBUTE_KEY_LAST_EXTENDED_DATA = "last_extended_data";

    private final AttributeKey<String> attributeKeyTrackerImei;
    private final AttributeKey<Tracker> attributeKeyTracker;
    private final AttributeKey<ExtendedData> attributeKeyLastExtendedData;

    public ContextAttributeManager() {
        this.attributeKeyTrackerImei = valueOf(NAME_ATTRIBUTE_KEY_TRACKER_IMEI);
        this.attributeKeyTracker = valueOf(NAME_ATTRIBUTE_KEY_TRACKER);
        this.attributeKeyLastExtendedData = valueOf(NAME_ATTRIBUTE_KEY_LAST_EXTENDED_DATA);
    }

    public void putTrackerImei(final ChannelHandlerContext context, final String imei) {
        putAttributeValue(context, this.attributeKeyTrackerImei, imei);
    }

    public Optional<String> findTrackerImei(final ChannelHandlerContext context) {
        return findAttributeValue(context, this.attributeKeyTrackerImei);
    }

    public void putTracker(final ChannelHandlerContext context, final Tracker tracker) {
        putAttributeValue(context, this.attributeKeyTracker, tracker);
    }

    public Optional<Tracker> findTracker(final ChannelHandlerContext context) {
        return findAttributeValue(context, this.attributeKeyTracker);
    }

    public void putLastExtendedData(final ChannelHandlerContext context, final ExtendedData extendedData) {
        putAttributeValue(context, this.attributeKeyLastExtendedData, extendedData);
    }

    public Optional<ExtendedData> findLastExtendedData(final ChannelHandlerContext context) {
        return findAttributeValue(context, this.attributeKeyLastExtendedData);
    }

    private static <ValueType> Optional<ValueType> findAttributeValue(final ChannelHandlerContext context,
                                                                      final AttributeKey<ValueType> attributeKey) {
        final Channel channel = context.channel();
        final Attribute<ValueType> attribute = channel.attr(attributeKey);
        return ofNullable(attribute.get());
    }

    private static <ValueType> void putAttributeValue(final ChannelHandlerContext context,
                                                      final AttributeKey<ValueType> attributeKey,
                                                      final ValueType value) {
        final Channel channel = context.channel();
        final Attribute<ValueType> attribute = channel.attr(attributeKey);
        attribute.set(value);
    }
}
