package by.vladzuev.locationreceiver.protocol.core.contextattributemanager;

import by.vladzuev.locationreceiver.crud.dto.Location;
import by.vladzuev.locationreceiver.crud.dto.Tracker;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.Attribute;
import io.netty.util.AttributeKey;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static by.vladzuev.locationreceiver.protocol.core.contextattributemanager.ContextAttributeManager.*;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public final class ContextAttributeManagerTest {
    private final ContextAttributeManager manager = new ContextAttributeManager();

    @Test
    public void trackerImeiShouldBePutAsAttribute() {
        testPuttingAttribute("11111222223333344444", KEY_TRACKER_IMEI, ContextAttributeManager::putTrackerImei);
    }

    @Test
    public void trackerShouldBePutAsAttribute() {
        testPuttingAttribute(Tracker.builder().build(), KEY_TRACKER, ContextAttributeManager::putTracker);
    }

    @Test
    public void locationShouldBePutAsAttribute() {
        testPuttingAttribute(Location.builder().build(), KEY_LAST_LOCATION, ContextAttributeManager::putLastLocation);
    }

    @Test
    public void trackerImeiShouldBeFoundInContextAsAttribute() {
        testSuccessFindingAttribute("11111222223333344444", KEY_TRACKER_IMEI, ContextAttributeManager::findTrackerImei);
    }

    @Test
    public void trackerImeiShouldNotBeFoundInContextAsAttribute() {
        testFailFindingAttribute(KEY_TRACKER_IMEI, ContextAttributeManager::findTrackerImei);
    }

    @Test
    public void trackerShouldBeFoundInContextAsAttribute() {
        testSuccessFindingAttribute(Tracker.builder().build(), KEY_TRACKER, ContextAttributeManager::findTracker);
    }

    @Test
    public void trackerShouldNotBeFoundInContextAsAttribute() {
        testFailFindingAttribute(KEY_TRACKER, ContextAttributeManager::findTracker);
    }

    @Test
    public void lastLocationShouldBeFoundInContextAsAttribute() {
        testSuccessFindingAttribute(
                Location.builder().build(),
                KEY_LAST_LOCATION,
                ContextAttributeManager::findLastLocation
        );
    }

    @Test
    public void lastLocationShouldNotBeFoundInContextAsAttribute() {
        testFailFindingAttribute(KEY_LAST_LOCATION, ContextAttributeManager::findLastLocation);
    }

    private <T> void testPuttingAttribute(final T givenObject,
                                          final AttributeKey<T> expectedKey,
                                          final AttributePutOperation<T> operation) {
        final ChannelHandlerContext givenContext = mock(ChannelHandlerContext.class);
        final Attribute<T> givenAttribute = mockAttribute(givenContext, expectedKey);
        operation.execute(manager, givenContext, givenObject);
        verify(givenAttribute, times(1)).set(same(givenObject));
    }

    private <T> Attribute<T> mockAttribute(final ChannelHandlerContext context, final AttributeKey<T> key) {
        final Channel channel = mock(Channel.class);
        when(context.channel()).thenReturn(channel);
        @SuppressWarnings("unchecked") final Attribute<T> attribute = mock(Attribute.class);
        when(channel.attr(same(key))).thenReturn(attribute);
        return attribute;
    }

    private <T> void testSuccessFindingAttribute(final T expectedObject,
                                                 final AttributeKey<T> expectedKey,
                                                 final AttributeFindOperation<T> operation) {
        final Optional<T> optionalActual = testFindingAttribute(expectedObject, expectedKey, operation);
        assertTrue(optionalActual.isPresent());
        final T actual = optionalActual.get();
        assertSame(expectedObject, actual);
    }

    private <T> void testFailFindingAttribute(final AttributeKey<T> expectedKey,
                                              final AttributeFindOperation<T> operation) {
        final Optional<T> optionalActual = testFindingAttribute(null, expectedKey, operation);
        assertTrue(optionalActual.isEmpty());
    }

    private <T> Optional<T> testFindingAttribute(final T expectedObject,
                                                 final AttributeKey<T> expectedKey,
                                                 final AttributeFindOperation<T> operation) {
        final ChannelHandlerContext givenContext = mock(ChannelHandlerContext.class);
        final Attribute<T> givenAttribute = mockAttribute(givenContext, expectedKey);
        when(givenAttribute.get()).thenReturn(expectedObject);
        return operation.execute(manager, givenContext);
    }

    @FunctionalInterface
    private interface AttributePutOperation<T> {
        void execute(ContextAttributeManager manager, ChannelHandlerContext context, T object);
    }

    @FunctionalInterface
    private interface AttributeFindOperation<T> {
        Optional<T> execute(ContextAttributeManager manager, ChannelHandlerContext context);
    }
}
